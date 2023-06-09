import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HtmlRequester implements HtmlRequesterInterface {
    public String request(String urlString) {
        try {
            return tryToRequest(urlString);
        } catch (IOException exception) {
            throw new ConnectionException();
        }
    }

    private String tryToRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();
        return sb.toString();
    }
}
