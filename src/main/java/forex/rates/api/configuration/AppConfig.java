package forex.rates.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@Import(SwaggerConfig.class)
public class AppConfig {

    @Bean
    public TaskScheduler threadPoolTaskScheduler() {
	return new ThreadPoolTaskScheduler();
    }

}
