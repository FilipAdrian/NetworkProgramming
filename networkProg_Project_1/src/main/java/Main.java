import org.apache.log4j.Logger;

import java.io.IOException;

public class Main {
    static Logger logger = Logger.getLogger (Main.class.getName ());
    public static void main(String[] args) {
        logger.info ("== Program Started ==");
        HttpClient httpClient = new HttpClient ();
        try {
            httpClient.get ("");
        } catch (IOException e) {
            e.printStackTrace ( );
        }


//        System.out.println (" == Program Finished == " );
        logger.info ("== Program Finished ==");
    }
}
