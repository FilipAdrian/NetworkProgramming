import java.io.IOException;

public class Task implements Runnable {
    private String uri ;
    private HttpClient httpClient;
    Task(String uri,HttpClient httpClient){
        this.uri = uri;
        this.httpClient = httpClient;
    }
    @Override
    public void run() {
        try {
            httpClient.get (uri);
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }
}
