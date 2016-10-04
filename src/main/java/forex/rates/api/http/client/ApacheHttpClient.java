package forex.rates.api.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ApacheHttpClient implements HttpClient {

    private final org.apache.http.client.HttpClient client;

    public ApacheHttpClient() {
	client = HttpClientBuilder.create().build();
    }

    @Override
    public InputStream getInputStream(String url) throws IOException {
	HttpGet request = new HttpGet(url);
	HttpResponse response = client.execute(request);
	return response.getEntity().getContent();
    }

}