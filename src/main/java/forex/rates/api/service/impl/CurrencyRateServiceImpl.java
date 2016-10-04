package forex.rates.api.service.impl;

import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRatesRepository;
import forex.rates.api.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRatesRepository currencyRatesRepository;

    @Autowired
    public CurrencyRateServiceImpl(CurrencyRatesRepository currencyRatesRepository) {
	this.currencyRatesRepository = currencyRatesRepository;
    }

    @Override
    public void save(CurrencyRate currencyRate) {
	currencyRatesRepository.save(currencyRate);
    }

}
