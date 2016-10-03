package forex.rates.api.http.client;

import java.io.IOException;
import java.io.InputStream;

public interface HttpClient {

    InputStream getInputStream(String url) throws IOException;

}
