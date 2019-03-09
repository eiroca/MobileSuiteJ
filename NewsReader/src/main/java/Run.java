import org.recompile.freej2me.J2MEHost;
import net.eiroca.j2me.RSSReader.NewsReader;

public class Run {

  public static void main(String[] args) {
    J2MEHost host = new J2MEHost(NewsReader.class);
  }

}
