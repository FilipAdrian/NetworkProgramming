import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class DataManager {
    static Logger logger = Logger.getLogger (DataManager.class.getName ( ));

    public DataManager() {
    }

    public String readStreamFromReader(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder ( );
        int cp;
        while ((cp = reader.read ( )) != -1) {
            stringBuilder.append ((char) cp);
        }

        return stringBuilder.toString ( );
    }

    public String csvToJson(String csv) {
        try {
            CsvSchema csvSchema = CsvSchema.builder ( ).setUseHeader (true).build ( );
            CsvMapper csvMapper = new CsvMapper ( );
            List <Object> readAll = null;
            readAll = csvMapper.readerFor (Map.class).with (csvSchema).readValues (csv).readAll ( );
            ObjectMapper mapper = new ObjectMapper ( );
            return mapper.writerWithDefaultPrettyPrinter ( ).writeValueAsString (readAll);
        } catch (IOException e) {
            e.printStackTrace ( );
            return null;
        }
    }

    public String toJson(String type, String data) throws IOException {
        String json = "";
        ObjectMapper jsonMapper = new ObjectMapper ( );
        switch (type) {
            case "application/json": {
                return data;
            }
            case "application/xml": {
                XmlMapper xmlMapper = new XmlMapper ( );
                JsonNode node = xmlMapper.readTree (data.getBytes ( ));
                json = jsonMapper.writeValueAsString (node);
                break;
            }
            case "text/csv": {
                json = csvToJson (data);
                break;
            }
            case "application/x-yaml": {
                YAMLMapper yamlMapper = new YAMLMapper ( );
                JsonNode node = yamlMapper.readTree (data.getBytes ( ));
                json = jsonMapper.writeValueAsString (node);
                break;
            }
            default: {
                logger.error ("Unknown data type : " + type);
                break;
            }
        }

        return json;
    }

}
