package forex.rates.api.repository.impl.springdatajpa;

import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRatesRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRatesRepositorySpringDataJpaImpl extends CurrencyRatesRepository, JpaRepository<CurrencyRate, Long> {
}
