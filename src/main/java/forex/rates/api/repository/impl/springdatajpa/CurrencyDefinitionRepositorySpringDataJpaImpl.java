package forex.rates.api.repository.impl.springdatajpa;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;

@Profile("spring-data-jpa")
public interface CurrencyDefinitionRepositorySpringDataJpaImpl extends CurrencyDefinitionRepository, Repository<CurrencyDefinition, Long> {
}
