import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.XML;


public class Main {

    public static void main(String[] args) {
        try {
            // Step 1: Create a URL for the JSON API.
            String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=Raiffeisen";

            // Step 2: Create an HTTP connection to the server.
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            // Step 3: Set the request method to "GET".
            connection.setRequestMethod("GET");

            // Step 4: Get the input stream of the connection to read the response.
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // Step 5: Convert the JSON response to XML format.
            JSONObject jsonObject = new JSONObject(responseBuilder.toString());
            // Step 5.1: remove batchcomplete and continue, they were both root elements
            jsonObject.remove("batchcomplete");
            jsonObject.remove("continue");

            String jsonString = jsonObject.toString();
            String xmlString = XML.toString(new JSONObject(jsonString));
            // Step 5.2: use this in JSONObject for adding root as an root element if not removing batchcomplete and continue
            //"{\"root\": " + jsonString + "}"

            // Step 6: Write the XML and JSON to a file.
            //JSON
            BufferedWriter jsonFileWriter = new BufferedWriter(new FileWriter("output.json"));
            jsonObject.write(jsonFileWriter);
            jsonFileWriter.close();
            //XML
            BufferedWriter xmlFileWriter = new BufferedWriter(new FileWriter("output.xml"));
            xmlFileWriter.write(xmlString);
            xmlFileWriter.close();

            // Step 7: Print the formatted strings to the console.
            System.out.println("JSON String:");
            System.out.println(jsonString);
            System.out.println("XML String:");
            System.out.println(xmlString);

            System.out.println("XML written to output.xml");
            System.out.println("JSON written to output.json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
