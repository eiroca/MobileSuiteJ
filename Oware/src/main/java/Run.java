import net.eiroca.j2me.external.oware.midlet.OwareMIDlet;
import net.eiroca.j2me.host.J2meHost;

public class Run {

  public static void main(String[] args) {
    Class<?> app = OwareMIDlet.class;
    J2meHost host = new J2meHost(app);
    host.run();
  }

}
