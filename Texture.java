import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {
  public int[] pixels;
  private String loc;
  public final int SIZE;
  
  public Texture(String location,int size) {
    loc=location;
    SIZE=size;
    pixels=new int[SIZE*SIZE];
    load();
  }
  
  private void load() {
    try {
      BufferedImage image=ImageIO.read(new File(loc));
      int w=image.getWidth();
      int h=image.getHeight();
      image.getRGB(0,0,w,h,pixels,0,w);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static Texture[] texList=new Texture[]{
    new Texture("res/grass.jpg",64),
    new Texture("res/bleh.jpg",64),
    new Texture("res/sus.jpg",64),
    new Texture("res/rock.jpg",64),
    new Texture("res/bread.jpg",64),
    new Texture("res/angel.jpg",64),
  };
}