package forex.rates.api.configuration;

import forex.rates.api.dataset.DataSetSource;
import forex.rates.api.dataset.DataSetUpdate;
import forex.rates.api.dataset.ExtractedCurrencyDefinition;
import forex.rates.api.dataset.ExtractedCurrencyRate;
import forex.rates.api.schedule.NewRatesSchedule;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Profile("repository-test")
@Configuration
public class RepositoryTestConfig {

    @Bean
    @Primary
    public NewRatesSchedule newRatesSchedule() {
	return Mockito.mock(NewRatesSchedule.class);
    }

    @Bean
    @Primary
    public DataSetSource dataSetSource() {
	return Mockito.mock(DataSetSource.class);
    }

    @Bean
    @Primary
    public DataSetUpdate dataSetUpdate() {
	return Mockito.mock(DataSetUpdate.class);
    }

    @Bean
    @Primary
    public ExtractedCurrencyDefinition extractedCurrencyDefinition() {
	return Mockito.mock(ExtractedCurrencyDefinition.class);
    }

    @Bean
    @Primary
    public ExtractedCurrencyRate extractedCurrencyRate() {
	return Mockito.mock(ExtractedCurrencyRate.class);
    }

    @Bean
    @Primary
    public DataSource dataSource() {
	return new EmbeddedDatabaseBuilder()
		.generateUniqueName(true)
		.setType(H2)
		.setScriptEncoding("UTF-8")
		.ignoreFailedDrops(true)
		.addScripts("scheme.sql", "data.sql")
		.build();
    }

}
