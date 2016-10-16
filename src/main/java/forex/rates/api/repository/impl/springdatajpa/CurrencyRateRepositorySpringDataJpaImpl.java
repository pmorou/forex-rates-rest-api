package forex.rates.api.repository.impl.springdatajpa;

import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRateRepository;
import org.springframework.data.repository.Repository;

public interface CurrencyRateRepositorySpringDataJpaImpl extends CurrencyRateRepository, Repository<CurrencyRate, Long> {
}
