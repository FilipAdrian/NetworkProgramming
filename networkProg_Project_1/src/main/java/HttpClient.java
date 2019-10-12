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
    protected String accessToken;
    DataManager dataManager;
    //    private static List <String> list = new ArrayList <> ( );
    public int requestNumber = 0;
    List <String>  dataList = new ArrayList <> ();



    HttpClient() {
        try {
            this.dataManager = new DataManager ();
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

    public List<String> get(String uri) throws IOException {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create ( ).build ( );
        List <String>  linkList = new ArrayList <> ();
        HttpGet request = new HttpGet (this.baseUri + uri);
        request.setHeader ("X-Access-Token", accessToken);
        HttpResponse response = client.execute (request);
        if (response.getStatusLine ( ).getStatusCode ( ) == 200) {
            BufferedReader reader = new BufferedReader (new InputStreamReader (response.getEntity ( ).getContent ( )));
            String text = dataManager.readStreamFromReader (reader);
            String contentType = response.getEntity ( ).getContentType ( ).getValue ( );
            if (contentType.equals ("application/json")) {
                if (!uri.equals ("/register")) {
                    linkList = searchJsonKey ("link", JsonParser.parseString (text), linkList);

                } else {
                    dataList = searchJsonKey ("access_token", JsonParser.parseString (text), dataList);
                }

                logger.info ("Content was received from : " + uri + "\n" + text);
                logger.info ("Element from list : " + Arrays.asList (linkList));
            }
        } else {
            logger.error ("Http Request Failed with status code " + uri + ": " + response.getStatusLine ( ).getStatusCode ( ));
        }
        return linkList;
    }

    private List<String> searchJsonKey(String key, JsonElement jsonElement, List <String> list) {

        if (jsonElement.isJsonArray ( )) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray ( )) {
                searchJsonKey (key, jsonElement1, list);
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
                            if (key.equals ("access_token")) {
                                accessToken = value.replace ("\"", "");
                                logger.info ("Access Token Received : " + accessToken);
                            } else {
                                list.add (value.replace ("\"", ""));
                            }

                        }

                    }
                    searchJsonKey (key, entry.getValue ( ), list);
                }
            } else {
                if (jsonElement.toString ( ).equals (key)) {
                    list.add (jsonElement.toString ( ));
                }
            }
        }

return list;
    }


}
