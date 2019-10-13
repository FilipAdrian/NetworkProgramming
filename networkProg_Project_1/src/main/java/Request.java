import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.BindException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Request {
    static Logger logger = Logger.getLogger (Request.class.getName ( ));

    private static HttpClient httpClient;
    private String uri = "/register";
    private static int index = 0;
    private static List <String> linkList = new ArrayList <> ( );
    private static ExecutorService executor;
    public boolean isFinished ;

    Request() {
        this.httpClient = new HttpClient ( );
        this.executor = Executors.newCachedThreadPool ( );
    }

    Request(String uri, HttpClient httpClient) {
        this.uri = uri;
        this.httpClient = httpClient;
        this.executor = Executors.newCachedThreadPool ( );
    }

    public void initiate() throws IOException {
        Future future = executor.submit (new Thread (uri));
//        while (true) {
//            logger.info ("Is Running");
//            if (index > 11) {
//                logger.info ("Finished");
//                executor.shutdown ( );
//                isFinished = true;
//                return;
//
//            }
//        }
    }

    static class Thread implements Runnable {
        static Logger logger = Logger.getLogger (Thread.class.getName ( ));
        private String uri;


        Thread(String uri) {
            this.uri = uri;
            logger.info (uri);

        }

        @Override
        public void run() {
            try {
                linkList.addAll (httpClient.get (uri));
            } catch (IOException e) {
                e.printStackTrace ( );
            }
            logger.info (index);

            for (int i = index; i < linkList.size ( ); i++) {
                logger.info ("List" + Arrays.asList (linkList));
                executor.submit (new Thread (linkList.get (i)));
                index++;

            }
        }
    }
}
