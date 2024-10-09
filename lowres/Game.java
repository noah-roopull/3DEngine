import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {
  public final int screenWidth=600;
  public final int screenHeight=screenWidth*3/4;
  public final double planeSize=0.7;
  public int mapSize=100;
  private Thread thread;
  public boolean running;
  private BufferedImage image;
  public int[] pixels;
  public Texture[] wallTex;
  public Camera camera;
  public Screen screen;
  public int[][] map;
  public Game() {
    wallTex=Texture.walls;
    Maze m=new Maze(mapSize*2+1,mapSize*2+1,true);
    map=m.cells; //reference for cell array
    for (int t=1;t<wallTex.length;t++) { //for each texture besides the first and last (first is default wall, last is floor
      for (int i=0;i<Math.sqrt(mapSize*mapSize);i++) { //approx. same amount of replacements for any size
        int rx=(int)(Math.random()*mapSize*4+3); //pick random x
        int ry=(int)(Math.random()*mapSize*4+3); //pick random y
        if (map[ry][rx]==1) { //make sure replacement tile is a default wall
          map[ry][rx]=t;
        }
      }
    }
    map[mapSize*4+1][mapSize*4+2]=wallTex.length; //set bottom right corner to angel
    map[mapSize*4+2][mapSize*4+1]=wallTex.length;
    System.out.println(m); //display modified maze
    thread=new Thread(this);
    image=new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_RGB);
    pixels=((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    double startPos=1.5+(double)mapSize*2.0; //center
    int rotation=(int)(Math.random()*4); //Rotates the player to a random 90deg angle
    double rx,ry,px,py;
    if (rotation==0) {
      rx=1;ry=0;
      px=0;py=-planeSize;
    } else if (rotation==1) {
      rx=0;ry=1;
      px=planeSize;py=0;
    } else if (rotation==2) {
      rx=-1;ry=0;
      px=0;py=planeSize;
    } else {
      rx=0;ry=-1;
      px=-planeSize;py=0;
    }
    camera=new Camera(this,startPos,startPos,rx,ry,px,py);
    screen=new Screen(map,mapSize*2+1,mapSize*2+1,screenWidth,screenHeight);
    addKeyListener(camera);
    setSize(screenWidth,screenHeight);
    setResizable(false); //avoid resolution change
    setTitle("3D Engine");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBackground(Color.BLACK);
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
      createBufferStrategy(2);
      return;
    }
    Graphics g=bs.getDrawGraphics();
    g.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
    bs.show();
  }
  public void run() {
    long lastTime=System.nanoTime();
    final double ns=Math.pow(10.0,9.0)/15.0;//60 times per second
    double delta=0;
    requestFocus();
    while(running) {
      long now=System.nanoTime();
      delta=delta+((now-lastTime)/ns);
      lastTime=now;
      while (delta>=1) {//update max 60fps
        //handle all logic restricted time
        screen.update(camera,pixels,0.004);
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
