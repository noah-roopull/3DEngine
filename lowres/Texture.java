import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {
  public int[] pixels;
  private String loc;

  public Texture(String location) {
    loc=location;
    pixels=new int[256];
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
  public static final Texture floor=new Texture("res/wood.jpg");
  public static final Texture ceil=new Texture("res/skycrackledark.jpg");
  public static final Texture[] walls=new Texture[]{
    new Texture("res/stonebrick.jpg"), //first texture is the default
    new Texture("res/mossybrick.jpg"),
    new Texture("res/redbrick.jpg"),
    new Texture("res/bleh.jpg"),
    new Texture("res/sus.jpg"),
    new Texture("res/rock.jpg"),
    new Texture("res/bread.jpg"),
    new Texture("res/cl.jpg"),
    new Texture("res/banana.jpg"),
    new Texture("res/dog.jpg"),
    new Texture("res/zodiac.jpg"),
    new Texture("res/ultrakill.jpg"),
    new Texture("res/angel.jpg") //last texture is used to denote the exit
  };
}
