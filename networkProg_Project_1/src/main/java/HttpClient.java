import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class HttpClient {
    static Logger logger = Logger.getLogger (HttpClient.class.getName ( ));
    private String baseUri;

    HttpClient() {
        try {
            InputStream inputStream = new FileInputStream (".\\src\\main\\resources\\application.properties");
            Properties properties = new Properties ( );
            properties.load (inputStream);
            this.baseUri = "http://" + properties.getProperty ("http.host") + ":" + properties.getProperty ("http.port");
        } catch (FileNotFoundException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }

    public void get(String uri) throws IOException {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create ( ).build ( );
        HttpGet request = new HttpGet (this.baseUri + uri);
        HttpResponse response = client.execute (request);
        BufferedReader reader = new BufferedReader (new InputStreamReader (response.getEntity ( ).getContent ( )));
        String text = readStreamFromReader (reader);
        logger.info (text);


    }

    public String readStreamFromReader(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder ( );
        int cp;
        while ((cp = reader.read ( )) != -1) {
            stringBuilder.append ((char) cp);
        }

        return stringBuilder.toString ( );
    }
}
