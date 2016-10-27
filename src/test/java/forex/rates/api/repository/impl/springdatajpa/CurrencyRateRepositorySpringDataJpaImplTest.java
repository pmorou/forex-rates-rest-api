package forex.rates.api.repository.impl.springdatajpa;

import forex.rates.api.repository.impl.AbstractCurrencyRateRepositoryImplTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	"spring.profiles.active=spring-data-jpa,repository-test",
	"dataSet.source.persist.enabled=false",
	"spring.jpa.show-sql=true"
})
public class CurrencyRateRepositorySpringDataJpaImplTest extends AbstractCurrencyRateRepositoryImplTest {

}