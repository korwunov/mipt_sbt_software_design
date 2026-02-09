import org.springframework.web.client.RestClient;

public class Main {
    private static final String HOST = "http://localhost:8080";

    private static final String ENDPOINT = "/api/currency";

    public static void main(String[] args) throws InterruptedException {
        RestClient client = RestClient.create(HOST + ENDPOINT);

        while (true) {
            CurrencyResponse response = client.get().retrieve().body(CurrencyResponse.class);
            System.out.println(String.format("Получен курс %s на момент времени %s", response.value, response.calculationDatetime));
            Thread.sleep(5000);
        }
    }
}