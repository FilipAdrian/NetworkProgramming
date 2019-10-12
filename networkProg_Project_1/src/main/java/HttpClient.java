import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.jar.JarException;

public class HttpClient {
    static Logger logger = Logger.getLogger (HttpClient.class.getName ( ));
    private String baseUri;
    private String accessToken;
    private static List <String> list = new ArrayList <> ( );


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
        request.setHeader ("X-Access-Token", accessToken);
        HttpResponse response = client.execute (request);
        if (response.getStatusLine ( ).getStatusCode ( ) == 200) {
            BufferedReader reader = new BufferedReader (new InputStreamReader (response.getEntity ( ).getContent ( )));
            String text = readStreamFromReader (reader);
            String contentType = response.getEntity ( ).getContentType ( ).getValue ( );
            if (contentType.equals ("application/json")) {
                logger.info ("ContentType : " + contentType);
                searchJsonKey ("link", JsonParser.parseString (text));
            }
            logger.info ("Content Was Received \n" + text);
        } else {
            logger.error ("Http Request Failed with status code : " + response.getStatusLine ( ).getStatusCode ( ));
        }
    }

    private void searchJsonKey(String key, JsonElement jsonElement) {

        if (jsonElement.isJsonArray ( )) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray ( )) {
                searchJsonKey (key, jsonElement1);
            }
        } else {
            if (jsonElement.isJsonObject ( )) {
                Set <Map.Entry <String, JsonElement>> entrySet = jsonElement
                        .getAsJsonObject ( ).entrySet ( );
                for (Map.Entry <String, JsonElement> entry : entrySet) {
                    String key1 = entry.getKey ( );
                    if (key1.equals (key)) {
                        String value = entry.getValue ( ).toString ( );
                        try {
                            JSONObject jsonKeyObj = new JSONObject (value);
                            Iterator <String> keySet = jsonKeyObj.keys ( );
                            while (keySet.hasNext ( )) {
                                list.add (jsonKeyObj.get (keySet.next ( )).toString ( ));
                            }
                        } catch (JSONException e) {
                            logger.warn (e.getMessage ( ));
                            list.add (value.replace ("\"", ""));
                        }

                    }
                    searchJsonKey (key, entry.getValue ( ));
                }
            } else {
                if (jsonElement.toString ( ).equals (key)) {
                    list.add (jsonElement.toString ( ));
                }
            }
        }


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
