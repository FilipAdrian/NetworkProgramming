import org.apache.log4j.Logger;

import java.io.IOException;


public class Main {
    static Logger logger = Logger.getLogger (Main.class.getName ( ));
    private static final Integer SERVER_PORT = 6666;

    public static void main(String[] args)  {
        logger.info ("== Program Started ==");

        if (new Request ( ).initiate ( )) {
            logger.info ("== All data were collected ==");
        }
        TcpMultiServer tcpMultiServer = new TcpMultiServer ();
        tcpMultiServer.start (SERVER_PORT);

    }
}
