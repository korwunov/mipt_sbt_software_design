import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    private static final String HOST = "http://localhost:8080";

    private static final String ENDPOINT = "/api/currency";

    public static void main(String[] args) throws InterruptedException, IOException {
        HttpClient client = HttpClient.newBuilder().build();

        while (true) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HOST + ENDPOINT))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Получен ответ от currencyProvider " + response.body());
            Thread.sleep(5000);
        }
    }
}