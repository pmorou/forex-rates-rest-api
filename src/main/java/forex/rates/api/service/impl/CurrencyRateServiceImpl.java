package forex.rates.api.service.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRateRepository;
import forex.rates.api.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;

    @Autowired
    public CurrencyRateServiceImpl(CurrencyRateRepository currencyRateRepository) {
	this.currencyRateRepository = currencyRateRepository;
    }

    @Override
    public List<CurrencyRate> getAllByDateAndCurrencyIn(LocalDate date, List<CurrencyDefinition> currencies) {
	return currencyRateRepository.findAllByDateAndCurrencyIn(date, currencies);
    }

    @Override
    public CurrencyRate getOneByDateAndCurrency(LocalDate date, CurrencyDefinition currency) {
	return currencyRateRepository.findOneByDateAndCurrency(date, currency);
    }

    @Transactional
    @Override
    public void save(CurrencyRate currencyRate) {
	currencyRateRepository.save(currencyRate);
    }

    @Transactional
    @Override
    public void save(Iterable<CurrencyRate> currencyRates) {
	currencyRateRepository.save(currencyRates);
    }

}
