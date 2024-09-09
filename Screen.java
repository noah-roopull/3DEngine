import java.awt.Color;

public class Screen {
  public int[][] map;
  public int mapWidth,mapHeight,width,height;
  public Texture[] textures;
  
  public Screen(int[][] m,int mapW,int mapH,Texture[] tex,int w,int h) {
    map=m;
    mapWidth=mapW;
    mapHeight=mapH;
    textures=tex;
    width=w;
    height=h;
  }
  
  public int[] update(Camera camera,int[] pixels) {
    for(int n=0;n<pixels.length/2;n++) {
      if(pixels[n]!=Color.DARK_GRAY.getRGB()) pixels[n]=Color.DARK_GRAY.getRGB();
    }
    for(int i=pixels.length/2;i<pixels.length;i++){
      if(pixels[i]!=Color.GRAY.getRGB()) pixels[i]=Color.gray.getRGB();
    }  
    for(int x=0;x<width;x=x+1) {
      double cameraX=2*x/(double)(width) -1;
      double rayDirX=camera.xDir+camera.xPlane*cameraX;
      double rayDirY=camera.yDir+camera.yPlane*cameraX;
      //map position
      int mapX=(int)camera.xPos;
      int mapY=(int)camera.yPos;
      //length of ray from current position to next x or y-side
      double sideDistX;
      double sideDistY;
      //length of ray from one side to next in map
      double deltaDistX=Math.sqrt(1+(rayDirY*rayDirY)/(rayDirX*rayDirX));
      double deltaDistY=Math.sqrt(1+(rayDirX*rayDirX)/(rayDirY*rayDirY));
      double perpWallDist;
      //direction to go in x and y
      int stepX,stepY;
      boolean hit=false;//was a wall hit
      int side=0;//was the wall vertical or horizontal
      //figure out the step direction and initial distance to a side
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
      while(!hit) {
        if (sideDistX<sideDistY) {
          sideDistX+=deltaDistX;
          mapX+=stepX;
          side=0;
        } else {
          sideDistY+=deltaDistY;
          mapY+=stepY;
          side=1;
        }
        if(map[mapX][mapY]>0) hit=true;
      }
      //calculate distance to the point of impact
      if(side==0) perpWallDist=Math.abs((mapX-camera.xPos+(1-stepX)/2)/rayDirX);
      else perpWallDist=Math.abs((mapY-camera.yPos+(1-stepY)/2)/rayDirY);  
      //now calculate the height of the wall based on the distance from the camera
      int lineHeight;
      if(perpWallDist>0) lineHeight=Math.abs((int)(height/perpWallDist));
      else lineHeight=height;
      //calculate lowest and highest pixel to fill in current stripe
      int drawStart=-lineHeight/2+ height/2;
      if(drawStart<0) drawStart=0;
      int drawEnd=lineHeight/2+height/2;
      if(drawEnd>=height) drawEnd=height-1;
      //add a texture
      int texNum=map[mapX][mapY]-1;
      double wallX; //exact position of where wall was hit
      if(side==1) { //if its a y-axis wall
        wallX=(camera.xPos+((mapY-camera.yPos+(1-stepY)/2)/rayDirY)*rayDirX);
      } else { //x-axis wall
        wallX=(camera.yPos+((mapX-camera.xPos+(1-stepX)/2)/rayDirX)*rayDirY);
      }
      wallX-=Math.floor(wallX);
      //x coordinate on the texture
      int texX=(int)(wallX*(textures[texNum].SIZE));
      if(side==0&&rayDirX>0) texX=textures[texNum].SIZE-texX-1;
      if(side==1&&rayDirY<0) texX=textures[texNum].SIZE-texX-1;
      //calculate y coordinate on texture
      for(int y=drawStart;y<drawEnd;y++) {
        int texY=(((y*2-height+lineHeight)<<6)/lineHeight)/2;
        int color;
        if(side==0) color=textures[texNum].pixels[texX+(texY*textures[texNum].SIZE)];
        else color=(textures[texNum].pixels[texX+(texY*textures[texNum].SIZE)]>>1)&8355711; //make y sides darker
        pixels[x+y*(width)]=color;
      }
    }
    return pixels;
  }
}
