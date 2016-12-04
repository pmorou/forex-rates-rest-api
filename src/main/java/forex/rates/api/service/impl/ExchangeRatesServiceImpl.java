package forex.rates.api.service.impl;

import forex.rates.api.dataset.DataSetContext;
import forex.rates.api.model.exchange.ExchangeRates;
import forex.rates.api.model.exchange.Rates;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.model.request.ExchangeRatesRequest;
import forex.rates.api.service.CurrencyDefinitionService;
import forex.rates.api.service.CurrencyRateService;
import forex.rates.api.service.ExchangeRatesService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private final CurrencyRateService currencyRateService;
    private final CurrencyDefinitionService currencyDefinitionService;
    private final DataSetContext dataSetContext;

    public ExchangeRatesServiceImpl(CurrencyRateService currencyRateService, CurrencyDefinitionService currencyDefinitionService, DataSetContext dataSetContext) {
	this.currencyRateService = currencyRateService;
	this.currencyDefinitionService = currencyDefinitionService;
	this.dataSetContext = dataSetContext;
    }

    @Override
    public ExchangeRates perform(ExchangeRatesRequest request) {
	Map<LocalDate, Rates> ratesByDate = new HashMap<>();
	List<String> requestedCurrencies = new ArrayList<>(request.getCurrencies());
	String dataSetBaseCurrency = dataSetContext.getBaseCurrency();
	String requestedBaseCurrency = request.getBase();

	// Remove data set's reference currency (if exists) as there is no such value in db.
	// If TRUE is returned, rate for this currency should be calculated out of actual base requested by user.
	boolean baseCurrencyRemoved = requestedCurrencies.remove(dataSetBaseCurrency);
	boolean removeExcessiveCurrency = requestedCurrencies.remove(requestedBaseCurrency);
	BigDecimal baseExchangeRate = BigDecimal.ONE;
	List<CurrencyDefinition> currencyDefinitions = currencyDefinitionService.getAllByCodeNameIn(requestedCurrencies);

	for (LocalDate date : request.getDateRange()) {
	    List<CurrencyRate> requestedCurrencyRates = currencyRateService.getAllByDateAndCurrencyIn(date, currencyDefinitions);
	    Rates rates = new Rates();

	    if (isNotDataSetsBaseCurrency(request)) {
		CurrencyDefinition baseCurrencyDefinition = currencyDefinitionService.getOneByCodeName(request.getBase());
		CurrencyRate baseCurrencyRate = currencyRateService.getOneByDateAndCurrency(date, baseCurrencyDefinition);
		baseExchangeRate = inverse(baseCurrencyRate.getExchangeRate(), baseCurrencyRate.getCurrency().getPrecision());

		if (baseCurrencyRemoved) {
		    rates.add(dataSetBaseCurrency, baseExchangeRate);
		}
	    }

	    for (CurrencyRate currencyRate : requestedCurrencyRates) {
		CurrencyDefinition currencyDefinition = currencyRate.getCurrency();
		BigDecimal exchangeRate = currencyRate.getExchangeRate();
		exchangeRate = multiply(exchangeRate, baseExchangeRate, currencyDefinition.getPrecision());
		rates.add(currencyDefinition.getCodeName(), exchangeRate);
	    }

	    ratesByDate.put(date, rates);
	}

	return new ExchangeRates(request.getStartDate(), request.getEndDate(), request.getBase(), ratesByDate);
    }

    private boolean isNotDataSetsBaseCurrency(ExchangeRatesRequest request) {
	return !request.getBase().equals(dataSetContext.getBaseCurrency());
    }

    private BigDecimal multiply(BigDecimal first, BigDecimal second, int precision) {
	return first.multiply(second, new MathContext(precision + 1, RoundingMode.HALF_UP));
    }

    private BigDecimal inverse(BigDecimal exchangeRate, int precision) {
	return BigDecimal.ONE.divide(exchangeRate, precision, BigDecimal.ROUND_HALF_UP);
    }

}
