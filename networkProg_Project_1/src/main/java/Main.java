import org.apache.log4j.Logger;

import java.io.IOException;



public class Main {
    static Logger logger = Logger.getLogger (Main.class.getName ( ));

    public static void main(String[] args) throws IOException {
        logger.info ("== Program Started ==");

        if (new Request ( ).initiate ( )) {
            logger.info ("== Program Finished ==");
        }

    }
}
