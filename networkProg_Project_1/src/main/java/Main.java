import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    static Logger logger = Logger.getLogger (Main.class.getName ( ));
    private static List <String> linkList = new ArrayList <> ( );

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime ( );
        logger.info ("== Program Started ==");
        new Request ().initiate ();

        logger.info ("== Program Finished ==");
        long endTime = System.nanoTime ( );
        long totalTime = endTime - startTime;
        System.out.println (totalTime);
    }
}
