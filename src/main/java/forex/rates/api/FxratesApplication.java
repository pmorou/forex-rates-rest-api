package forex.rates.api;

import forex.rates.api.configuration.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfig.class)
public class FxratesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxratesApplication.class, args);
	}
}
