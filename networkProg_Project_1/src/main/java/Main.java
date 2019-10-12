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
        HttpClient httpClient = new HttpClient ( );
        httpClient.get ("/register");
        linkList.addAll (httpClient.get ("/home"));
        ExecutorService executor = Executors.newCachedThreadPool ( );
        for (int i = 0; i < linkList.size ( ); i++) {
            executor.submit (new Task (linkList.get (i),httpClient));
        }
        executor.shutdown ();

        logger.info ("== Program Finished ==");
        long endTime = System.nanoTime ( );
        long totalTime = endTime - startTime;
        System.out.println (totalTime);
    }
}
