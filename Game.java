import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {
  public final int screenWidth=800;
  public final int screenHeight=600;
  public int mapWidth=15;
  public int mapHeight=15;
  private Thread thread;
  public boolean running;
  private BufferedImage image;
  public int[] pixels;
  public Texture[] textures;
  public Camera camera;
  public Screen screen;
  public int[][] map;
  public Game() {
    textures=Texture.texList;
    map=new Maze(mapWidth*2+1,mapHeight*2+1,true).cells;
    for (int t=1;t<textures.length;t++) {
      for (int i=0;i<Math.sqrt(mapWidth*mapHeight);i++) {
        int rx=(int)(Math.random()*mapWidth*4+3);
        int ry=(int)(Math.random()*mapHeight*4+3);
        if (map[ry][rx]==1) {
          map[ry][rx]=t;
        }
      }
    }
    map[mapHeight*4+1][mapWidth*4+2]=textures.length;
    map[mapHeight*4+2][mapWidth*4+1]=textures.length;
    for (int j=0;j<mapWidth*4+3;j++) {
      for (int i=0;i<mapHeight*4+3;i++) {
        if (map[j][i]>0) {
          System.out.print(""+map[j][i]+map[j][i]);
        } else {
          System.out.print("  ");
        }
      }
      System.out.println();
    }
    thread=new Thread(this);
    image=new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_RGB);
    pixels=((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    double startX=((mapWidth%2==0)?0.5:1.5)+(double)mapWidth*2.0;
    double startY=((mapHeight%2==0)?0.5:1.5)+(double)mapHeight*2.0;
    int rotation=(int)(Math.random()*4);
    System.out.println("startX:"+startX+" startY:"+startY);
    String[] direction={"NORTH","EAST","SOUTH","WEST"};
    System.out.println("rotation:"+direction[rotation]);
    double rx,ry,px,py;
    if (rotation==0) {
      rx=1;
      ry=0;
      px=0;
      py=-0.7;
    } else if (rotation==1) {
      rx=0;
      ry=1;
      px=0.7;
      py=0;
    } else if (rotation==2) {
      rx=-1;
      ry=0;
      px=0;
      py=0.7;
    } else {
      rx=0;
      ry=-1;
      px=-0.7;
      py=0;
    }
    camera=new Camera(this,startX,startY,rx,ry,px,py);
    screen=new Screen(map,mapWidth*2+1,mapHeight*2+1,textures,screenWidth,screenHeight);
    addKeyListener(camera);
    setSize(screenWidth,screenHeight);
    setResizable(false);
    setTitle("3D Engine");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBackground(Color.black);
    setLocationRelativeTo(null);
    setVisible(true);
    this.requestFocus();
    start();
  }
  private synchronized void start() {
    running=true;
    thread.start();
  }
  public synchronized void stop() {
    running=false;
    try {
      thread.join();
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }
  public void render() {
    BufferStrategy bs=getBufferStrategy();
    if(bs==null) {
      createBufferStrategy(3);
      return;
    }
    Graphics g=bs.getDrawGraphics();
    g.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
    bs.show();
  }
  public void run() {
    long lastTime=System.nanoTime();
    final double ns=Math.pow(10.0,9.0)/60.0;//60 times per second
    double delta=0;
    requestFocus();
    while(running) {
      long now=System.nanoTime();
      delta=delta+((now-lastTime)/ns);
      lastTime=now;
      while (delta>=1) {//update max 60fps
        //handles all of the logic restricted time
        screen.update(camera,pixels);
        camera.update(map);
        delta--;
      }
      render();//displays to the screen unrestricted time
    }
  }
  public static void main(String[] args) {
    Game game=new Game();
  }
}
