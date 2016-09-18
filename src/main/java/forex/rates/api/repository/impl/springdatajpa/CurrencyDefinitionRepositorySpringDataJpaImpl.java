package forex.rates.api.repository.impl.springdatajpa;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyDefinitionRepositorySpringDataJpaImpl extends CurrencyDefinitionRepository, JpaRepository<CurrencyDefinition, Long> {
}
