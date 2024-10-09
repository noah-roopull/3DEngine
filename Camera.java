import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

public class Camera implements KeyListener{
  public Game frame;
  public int mazeSize;
  public double xPos,yPos,xDir,yDir,xPlane,yPlane,oXPos,oYPos,oXDir,oYDir,oXPlane,oYPlane;
  public boolean left,right,forward,back,sleft,sright,resetpos; //turnleft,turnright,moveforward,moveback,strafeleft,straferight,resetposition
  public final double move_speed=0.08;
  public final double sprint_speed=0.12;
  public final double rotation_speed=0.07;
  public double ms,m;
  public Camera(Game f,int ms,double x,double y,double xd,double yd,double xp,double yp) {
    frame=f;
    mazeSize=ms;
    xPos=x;
    oXPos=x;
    yPos=y;
    oYPos=y;
    xDir=xd;
    oXDir=xd;
    yDir=yd;
    oYDir=yd;
    xPlane=xp;
    oXPlane=xp;
    yPlane=yp;
    oYPlane=yp;
    m=move_speed;
  }
  public void keyPressed(KeyEvent key) {
    if (key.getKeyCode()==KeyEvent.VK_LEFT) left=true;
    if (key.getKeyCode()==KeyEvent.VK_RIGHT) right=true;
    if (key.getKeyCode()==KeyEvent.VK_W) forward=true;
    if (key.getKeyCode()==KeyEvent.VK_S) back=true;
    if (key.getKeyCode()==KeyEvent.VK_R) resetpos=true;
    if (key.getKeyCode()==KeyEvent.VK_A) sleft=true;
    if (key.getKeyCode()==KeyEvent.VK_D) sright=true;
    if (key.getKeyCode()==KeyEvent.VK_SHIFT) m=sprint_speed;
    if (key.getKeyCode()==KeyEvent.VK_P) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); //close window cleanly
  }
  public void keyReleased(KeyEvent key) {
    if (key.getKeyCode()==KeyEvent.VK_LEFT) left=false;
    if (key.getKeyCode()==KeyEvent.VK_RIGHT) right=false;
    if (key.getKeyCode()==KeyEvent.VK_W) forward=false;
    if (key.getKeyCode()==KeyEvent.VK_S) back=false;
    if (key.getKeyCode()==KeyEvent.VK_R) resetpos=false;
    if (key.getKeyCode()==KeyEvent.VK_A) sleft=false;
    if (key.getKeyCode()==KeyEvent.VK_D) sright=false;
    if (key.getKeyCode()==KeyEvent.VK_SHIFT) m=move_speed;
  }
  public void update(int[][] map) {
    if (resetpos) {
      xPos=oXPos;
      yPos=oYPos;
      xDir=oXDir;
      yDir=oYDir;
      xPlane=oXPlane;
      yPlane=oYPlane;
    }
    if ((forward||back)&&(sleft||sright)) {
      ms=m*0.71; //~sqrt(2)/2, avoid faster movement while moving diagonally
    } else {
      ms=m;
    }
    double dx=0.0;
    double dy=0.0;
    //Moving forward/backwards, strafing left/right, and looking left/right are mutually exclusive
    if (forward&&!back) {
      dx+=xDir;
      dy+=yDir;
    } else if (!forward&&back) {
      dx-=xDir;
      dy-=yDir;
    }
    if (sleft&&!sright) {
      dx-=yDir;
      dy+=xDir;
    } else if (!sleft&&sright) {
      dx+=yDir;
      dy-=xDir;
    }
    if (map[(int)(xPos+dx*ms)][(int)yPos]<=0) xPos+=dx*ms;
    if (map[(int)xPos][(int)(yPos+dy*ms)]<=0) yPos+=dy*ms;
    if (right&&!left) {
      double oldxDir=xDir;
      xDir=xDir*Math.cos(-rotation_speed)-yDir*Math.sin(-rotation_speed);
      yDir=oldxDir*Math.sin(-rotation_speed)+yDir*Math.cos(-rotation_speed);
      double oldxPlane=xPlane;
      xPlane=xPlane*Math.cos(-rotation_speed)-yPlane*Math.sin(-rotation_speed);
      yPlane=oldxPlane*Math.sin(-rotation_speed)+yPlane*Math.cos(-rotation_speed);
    } else if (left&&!right) {
      double oldxDir=xDir;
      xDir=xDir*Math.cos(rotation_speed)-yDir*Math.sin(rotation_speed);
      yDir=oldxDir*Math.sin(rotation_speed)+yDir*Math.cos(rotation_speed);
      double oldxPlane=xPlane;
      xPlane=xPlane*Math.cos(rotation_speed)-yPlane*Math.sin(rotation_speed);
      yPlane=oldxPlane*Math.sin(rotation_speed)+yPlane*Math.cos(rotation_speed);
    }
    if ((int)xPos==mazeSize*4+3&&(int)yPos==mazeSize*4+3) {
      System.out.println("Finished!");
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    } else {
      System.out.println(""+xPos+","+yPos);
    }
  }
  public void keyTyped(KeyEvent key) {} //Override but ignore
}
