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
  public static Texture floor=new Texture("res/wood.jpg",64);
  public static Texture[] decor=new Texture[]{
    new Texture("res/stonebrick.jpg",64), //first texture is the default
    new Texture("res/mossybrick.jpg",64),
    new Texture("res/redbrick.jpg",64),
    new Texture("res/bleh.jpg",64),
    new Texture("res/sus.jpg",64),
    new Texture("res/rock.jpg",64),
    new Texture("res/bread.jpg",64),
    new Texture("res/cl.jpg",64),
    new Texture("res/banana.jpg",64),
    new Texture("res/dog.jpg",64),
    new Texture("res/zodiac.jpg",64),
    new Texture("res/ultrakill.jpg",64),
    new Texture("res/angel.jpg",64)
  };
}
