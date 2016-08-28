package forex.rates.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SwaggerConfig.class)
public class FxratesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxratesApplication.class, args);
	}
}
