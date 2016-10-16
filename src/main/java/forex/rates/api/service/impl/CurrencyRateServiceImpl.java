package forex.rates.api.service.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRatesRepository;
import forex.rates.api.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRatesRepository currencyRatesRepository;

    @Autowired
    public CurrencyRateServiceImpl(CurrencyRatesRepository currencyRatesRepository) {
	this.currencyRatesRepository = currencyRatesRepository;
    }

    @Override
    public List<CurrencyRate> getAllByDateAndCurrencyIn(LocalDate date, List<CurrencyDefinition> currencies) {
	return currencyRatesRepository.findAllByDateAndCurrencyIn(date, currencies);
    }

    @Override
    public CurrencyRate getOneByDateAndCurrency(LocalDate date, CurrencyDefinition currency) {
	return currencyRatesRepository.findOneByDateAndCurrency(date, currency);
    }

    @Transactional
    @Override
    public void save(CurrencyRate currencyRate) {
	currencyRatesRepository.save(currencyRate);
    }

    @Transactional
    @Override
    public void save(Iterable<CurrencyRate> currencyRates) {
	currencyRatesRepository.save(currencyRates);
    }

}
