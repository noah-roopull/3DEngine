import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Camera implements KeyListener{
  public double xPos,yPos,xDir,yDir,xPlane,yPlane,oXPos,oYPos,oXDir,oYDir,oXPlane,oYPlane;
  public boolean left,right,forward,back,sleft,sright,resetpos; //turnleft,turnright,moveforward,moveback,strafeleft,straferight,resetposition
  public double move_speed=0.08;
  public double rotation_speed=0.06;
  public Camera(double x,double y,double xd,double yd,double xp,double yp) {
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
    if((key.getKeyCode()==KeyEvent.VK_SHIFT)) move_speed=0.12;
  }
  public void keyReleased(KeyEvent key) {
    if((key.getKeyCode()==KeyEvent.VK_LEFT)) left=false;
    if((key.getKeyCode()==KeyEvent.VK_RIGHT)) right=false;
    if((key.getKeyCode()==KeyEvent.VK_W)) forward=false;
    if((key.getKeyCode()==KeyEvent.VK_S)) back=false;
    if((key.getKeyCode()==KeyEvent.VK_R)) resetpos=false;
    if((key.getKeyCode()==KeyEvent.VK_A)) sleft=false;
    if((key.getKeyCode()==KeyEvent.VK_D)) sright=false;
    if((key.getKeyCode()==KeyEvent.VK_SHIFT)) move_speed=0.08;
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
      ms=move_speed*0.71;
    } else {
      ms=move_speed;
    }
    if (forward) {
      if (map[(int)(xPos+xDir*ms)][(int)yPos]<=0) xPos+=xDir*ms;
      if (map[(int)xPos][(int)(yPos+yDir*ms)]<=0) yPos+=yDir*ms;
    } else if (back) {
      if (map[(int)(xPos-xDir*ms)][(int)yPos]<=0) xPos-=xDir*ms;
      if (map[(int)xPos][(int)(yPos-yDir*ms)]<=0) yPos-=yDir*ms;
    }
    if (sleft&&!sright) {
      if (map[(int)(xPos-yDir*ms)][(int)yPos]<=0) xPos-=yDir*ms;
      if (map[(int)xPos][(int)(yPos+xDir*ms)]<=0) yPos+=xDir*ms;
    } else if (!sleft&&sright) {
      if (map[(int)(xPos+yDir*ms)][(int)yPos]<=0) xPos+=yDir*ms;
      if (map[(int)xPos][(int)(yPos-xDir*ms)]<=0) yPos-=xDir*ms;
    }
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
  public void keyTyped(KeyEvent key) {}
}