package forex.rates.api.repository.impl.jpa;

import forex.rates.api.repository.impl.AbstractCurrencyDefinitionRepositoryImplTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	"spring.profiles.active=hibernate-jpa,repository-test",
	"dataSet.source.persist.enabled=false",
	"spring.jpa.show-sql=true"
})
public class CurrencyDefinitionRepositoryJpaImplTest extends AbstractCurrencyDefinitionRepositoryImplTest {

}