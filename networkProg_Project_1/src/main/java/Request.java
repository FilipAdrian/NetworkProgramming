import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Request extends Thread {
    private HttpClient httpClient;
    private String uri = "/register";
    private int index = 0;
    private static List <String> linkList = new ArrayList <> ( );
    Request( ) {
        this.httpClient = new HttpClient ( );
    }

    Request(String uri, HttpClient httpClient) {
        this.uri = uri;
        this.httpClient = httpClient;

    }

    @Override
    public void run() {

        try {
            linkList.addAll (httpClient.get (uri));
            System.out.println (Arrays.asList (linkList) );
        } catch (IOException e) {
            e.printStackTrace ( );
        }


//        Collections.reverse (linkList);
        for (int i = index; i<linkList.size ();i++){
//            System.out.println ("start" );
//            System.out.println(Thread.currentThread().getName() + " " + uri  );
//            HttpClient hc = new HttpClient ();
//            hc.setAccessToken (httpClient.accessToken);
            new Request (linkList.get (i),httpClient).start ();
index++;
System.out.println ("removed" );
        }

    }
}
