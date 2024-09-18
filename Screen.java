import java.awt.Color;
public class Screen {
  public int[][] map;
  public int mapWidth,mapHeight,width,height;
  public static Texture[] textures=Texture.decor;
  public static Texture ceilTex=Texture.ceil;
  public static Texture floorTex=Texture.floor;
  private static final double fadeDist=4.0;
  private static final double maxDist=Math.pow(fadeDist,2);
  private double time;
  public Screen(int[][] m,int mapW,int mapH,int w,int h) {
    map=m;
    mapWidth=mapW;
    mapHeight=mapH;
    width=w;
    height=h;
    time=0.0;
  }
  public int[] update(Camera camera,int[] pixels,double delta) {
    time+=delta;
    for (int i=0;i<pixels.length;i++) {
      if (pixels[i]!=0) pixels[i]=0;
    }
    for (int x=0;x<width;x++) {
      double cameraX=2*x/(double)(width)-1;
      double rayDirX=camera.xDir+camera.xPlane*cameraX;
      double rayDirY=camera.yDir+camera.yPlane*cameraX;
      int mapX=(int)camera.xPos;
      int mapY=(int)camera.yPos;
      double sideDistX;
      double sideDistY;
      double deltaDistX=Math.sqrt(1+(rayDirY*rayDirY)/(rayDirX*rayDirX));
      double deltaDistY=Math.sqrt(1+(rayDirX*rayDirX)/(rayDirY*rayDirY));
      double perpWallDist;
      int stepX,stepY;
      boolean hit=false;
      int side=0;
      if (rayDirX<0) {
        stepX=-1;
        sideDistX=(camera.xPos-mapX)*deltaDistX;
      } else {
        stepX=1;
        sideDistX=(mapX+1.0-camera.xPos)*deltaDistX;
      }
      if (rayDirY<0) {
        stepY=-1;
        sideDistY=(camera.yPos-mapY)*deltaDistY;
      } else {
        stepY=1;
        sideDistY=(mapY+1.0-camera.yPos)*deltaDistY;
      }
      double dt=0.0;
      while (!hit&&dt<maxDist) {
        if (sideDistX<sideDistY) {
          sideDistX+=deltaDistX;
          mapX+=stepX;
          dt+=stepX;
          side=0;
         } else {
          sideDistY+=deltaDistY;
          mapY+=stepY;
          dt+=stepY;
          side=1;
        }
        if (map[mapX][mapY]>0) hit=true;
      }
      if (side==0) perpWallDist=Math.abs((mapX-camera.xPos+(1-stepX)/2)/rayDirX);
      else perpWallDist=Math.abs((mapY-camera.yPos+(1-stepY)/2)/rayDirY);
      int lineHeight;
      if (perpWallDist>0) lineHeight=Math.abs((int)(height/perpWallDist));
      else lineHeight=height;
      int drawStart=-lineHeight/2+height/2;
      if (drawStart<0) drawStart=0;
      int drawEnd=lineHeight/2+height/2;
      if (drawEnd>=height) drawEnd=height;
      int texNum=Math.max(map[mapX][mapY]-1,0);
      double wallX;
      if (side==1) {
        wallX=(camera.xPos+((mapY-camera.yPos+(1-stepY)/2)/rayDirY)*rayDirX);
      } else {
        wallX=(camera.yPos+((mapX-camera.xPos+(1-stepX)/2)/rayDirX)*rayDirY);
      }
      wallX-=Math.floor(wallX);
      int wallTexX=(int)(wallX*textures[texNum].SIZE);
      if (side==0&&rayDirX>0) wallTexX=textures[texNum].SIZE-wallTexX-1;
      if (side==1&&rayDirY<0) wallTexX=textures[texNum].SIZE-wallTexX-1;
      for (int y=0;y<drawStart;y++) { //ceil
        double currentDist=height/(height-2.0*y); //distance from the player to the ceiling at this y-coordinate
        double ceilX=camera.xPos+currentDist*(camera.xDir+camera.xPlane*(2*x/(double)width-1));
        double ceilY=camera.yPos+currentDist*(camera.yDir+camera.yPlane*(2*x/(double)width-1));
        int ceilTexX=(int)((ceilX+time)%1*ceilTex.SIZE);
        int ceilTexY=(int)((ceilY+time)%1*ceilTex.SIZE);
        ceilTexX=(ceilTexX+ceilTex.SIZE)%ceilTex.SIZE;
        ceilTexY=(ceilTexY+ceilTex.SIZE)%ceilTex.SIZE;
        int color=ceilTex.pixels[ceilTexX+ceilTexY*ceilTex.SIZE];
        pixels[x+y*width]=color;
      }
      for (int y=drawStart;y<drawEnd;y++) { //walls
        int texY=(((y*2-height+lineHeight)<<6)/lineHeight)/2;
        int color=textures[texNum].pixels[wallTexX+(texY*textures[texNum].SIZE)];
        double fadeFactor=Math.max(0,Math.min(1,1-(perpWallDist/fadeDist)));
        int r=(color>>16)&0xFF;
        int g=(color>>8)&0xFF;
        int b=color&0xFF;
        r=(int)(r*fadeFactor);
        g=(int)(g*fadeFactor);
        b=(int)(b*fadeFactor);
        color=(r<<16)|(g<<8)|b;
        pixels[x+y*width]=color;
      }
      for (int y=drawEnd;y<height;y++) { //floor
        double currentDist=height/(2.0*y-height); //distance from the player to the floor at this y-coordinate
        double floorX=camera.xPos+currentDist*(camera.xDir+camera.xPlane*(2*x/(double)width-1));
        double floorY=camera.yPos+currentDist*(camera.yDir+camera.yPlane*(2*x/(double)width-1));
        int floorTexX=(int)(floorX%1*floorTex.SIZE);
        int floorTexY=(int)(floorY%1*floorTex.SIZE);
        floorTexX=(floorTexX+floorTex.SIZE)%floorTex.SIZE;
        floorTexY=(floorTexY+floorTex.SIZE)%floorTex.SIZE;
        int color=floorTex.pixels[floorTexX+floorTexY*floorTex.SIZE];
        double fadeFactor=Math.max(0,Math.min(1,1-(currentDist/fadeDist)));
        int r=(color>>16)&0xFF;
        int g=(color>>8)&0xFF;
        int b=color&0xFF;
        r=(int)(r*fadeFactor);
        g=(int)(g*fadeFactor);
        b=(int)(b*fadeFactor);
        color=(r<<16)|(g<<8)|b;
        pixels[x+y*width]=color;
      }
    }
    return pixels;
  }
}
