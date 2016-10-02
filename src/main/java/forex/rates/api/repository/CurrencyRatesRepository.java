package forex.rates.api.repository;

import forex.rates.api.model.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRatesRepository {

    List<CurrencyRate> findAllByDateAndCurrenciesIn(List<String> currencies, LocalDate date);

    CurrencyRate findOneByDateAndCurrenciesIn(String currency, LocalDate startDate);

    CurrencyRate save(CurrencyRate currencyRate);

}
