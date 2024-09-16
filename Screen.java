import java.awt.Color;
public class Screen {
  public int[][] map;
  public int mapWidth,mapHeight,width,height;
  public Texture[] textures;
  private static final int dark1=127;
  private static final int dark2=191;
  private static final double fade_dist=6.0;
  public Screen(int[][] m, int mapW, int mapH, Texture[] tex, int w, int h) {
    map=m;
    mapWidth=mapW;
    mapHeight=mapH;
    textures=tex;
    width=w;
    height=h;
  }
  public int[] update(Camera camera, int[] pixels) {
    for (int n=0;n<pixels.length/2;n++) {
      if (pixels[n]!=0) pixels[n]=0;
    }
    for (int i=pixels.length/2;i<pixels.length;i++) {
      if (pixels[i]!=0) pixels[i]=0;
    }
    for (int x=0;x<width;x++) {
      double cameraX=2*x/(double)(width)-1;
      double rayDirX=camera.xDir+camera.xPlane*cameraX;
      double rayDirY=camera.yDir+camera.yPlane*cameraX;
      int mapX=(int) camera.xPos;
      int mapY=(int) camera.yPos;
      double sideDistX;
      double sideDistY;
      double deltaDistX=Math.sqrt(1+(rayDirY*rayDirY)/(rayDirX*rayDirX));
      double deltaDistY=Math.sqrt(1+(rayDirX*rayDirX)/(rayDirY*rayDirY));
      double perpWallDist;
      int stepX, stepY;
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
      while (!hit) {
        if (sideDistX<sideDistY) {
          sideDistX+=deltaDistX;
          mapX+=stepX;
          side=0;
        } else {
          sideDistY+=deltaDistY;
          mapY+=stepY;
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
      if (drawEnd >= height) drawEnd=height-1;
      int texNum=map[mapX][mapY]-1;
      double wallX;
      if (side==1) {
        wallX=(camera.xPos+((mapY-camera.yPos+(1-stepY)/2)/rayDirY)*rayDirX);
      } else {
        wallX=(camera.yPos+((mapX-camera.xPos+(1-stepX)/2)/rayDirX)*rayDirY);
      }
      wallX -= Math.floor(wallX);
      int texX=(int)(wallX*textures[texNum].SIZE);
      if (side==0&&rayDirX>0) texX=textures[texNum].SIZE-texX-1;
      if (side==1&&rayDirY<0) texX=textures[texNum].SIZE-texX-1;
      for (int y=drawStart;y<drawEnd;y++) {
        int texY=(((y*2-height+lineHeight)<<6)/lineHeight)/2;
        int color=textures[texNum].pixels[texX+(texY*textures[texNum].SIZE)];
        // Calculate fade factor based on distance
        double fadeFactor=Math.max(0, Math.min(1, 1-(perpWallDist/fade_dist)));
        int r=(color>>16)&0xFF;
        int g=(color>>8)&0xFF;
        int b=color&0xFF;
        // Apply fade
        r=(int)(r*fadeFactor);
        g=(int)(g*fadeFactor);
        b=(int)(b*fadeFactor);
        color=(r<<16)|(g<<8)|b;
        if (side==0&&stepX>0) {
          r*=dark1;
          g*=dark1;
          b*=dark1;
        } else if (side==1) {
          r*=dark2;
          g*=dark2;
          b*=dark2;
        }
        pixels[x+y*width]=color;
      }
    }
    return pixels;
  }
}
