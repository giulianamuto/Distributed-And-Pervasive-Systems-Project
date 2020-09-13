import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;


public class ServerRest {

    static Logger log = Logger.getLogger("global");

    public static void main(String[] args) throws URISyntaxException, IOException {

        URI uri = new URI("http://localhost:6060/");
        log.info("Connected to: "+ uri);

        ResourceConfig rc = new ResourceConfig();
        rc.packages("gateway.services");

        HttpServer server = JdkHttpServerFactory.createHttpServer(uri, rc, false);
        server.start();
        log.info("Server Gateway (SERVER REST) connected");

        System.out.println("To shut down the server press any button");
        System.in.read();
        server.stop(0);
    }

}
