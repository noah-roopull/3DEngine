import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Camera implements KeyListener{
  public Game frame;
  public double xPos,yPos,xDir,yDir,xPlane,yPlane,oXPos,oYPos,oXDir,oYDir,oXPlane,oYPlane;
  public boolean left,right,forward,back,sleft,sright,resetpos; //turnleft,turnright,moveforward,moveback,strafeleft,straferight,resetposition
  public double move_speed=0.08;
  public double rotation_speed=0.06;
  public Camera(Game f,double x,double y,double xd,double yd,double xp,double yp) {
    frame=f;
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
  }
  public void keyPressed(KeyEvent key) {
    if((key.getKeyCode()==KeyEvent.VK_LEFT)) left=true;
    if((key.getKeyCode()==KeyEvent.VK_RIGHT)) right=true;
    if((key.getKeyCode()==KeyEvent.VK_W)) forward=true;
    if((key.getKeyCode()==KeyEvent.VK_S)) back=true;
    if((key.getKeyCode()==KeyEvent.VK_R)) resetpos=true;
    if((key.getKeyCode()==KeyEvent.VK_A)) sleft=true;
    if((key.getKeyCode()==KeyEvent.VK_D)) sright=true;
    if((key.getKeyCode()==KeyEvent.VK_SHIFT)) move_speed=0.12; //150% base speed
    if((key.getKeyCode()==KeyEvent.VK_P)) {frame.running=false;frame.dispose();} //close window (almost) clenanly
  }
  public void keyReleased(KeyEvent key) {
    if((key.getKeyCode()==KeyEvent.VK_LEFT)) left=false;
    if((key.getKeyCode()==KeyEvent.VK_RIGHT)) right=false;
    if((key.getKeyCode()==KeyEvent.VK_W)) forward=false;
    if((key.getKeyCode()==KeyEvent.VK_S)) back=false;
    if((key.getKeyCode()==KeyEvent.VK_R)) resetpos=false;
    if((key.getKeyCode()==KeyEvent.VK_A)) sleft=false;
    if((key.getKeyCode()==KeyEvent.VK_D)) sright=false;
    if((key.getKeyCode()==KeyEvent.VK_SHIFT)) move_speed=0.08; //revert to original speed
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
    double ms=0.0;
    if ((forward||back)&&(sleft||sright)) {
      ms=move_speed*0.71; //~sqrt(2)/2, avoid faster movement while moving diagonally
    } else {
      ms=move_speed;
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
      rotation_speed+=0.0005;
    } else if (left&&!right) {
      double oldxDir=xDir;
      xDir=xDir*Math.cos(rotation_speed)-yDir*Math.sin(rotation_speed);
      yDir=oldxDir*Math.sin(rotation_speed)+yDir*Math.cos(rotation_speed);
      double oldxPlane=xPlane;
      xPlane=xPlane*Math.cos(rotation_speed)-yPlane*Math.sin(rotation_speed);
      yPlane=oldxPlane*Math.sin(rotation_speed)+yPlane*Math.cos(rotation_speed);
      rotation_speed+=0.0005;
    } else {
      rotation_speed=0.06;
    }
  }
  public void keyTyped(KeyEvent key) {} //Override but ignore
}
