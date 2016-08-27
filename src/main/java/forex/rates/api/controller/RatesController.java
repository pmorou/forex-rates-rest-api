package forex.rates.api.controller;

import forex.rates.api.model.DailyRatesResponse;
import forex.rates.api.model.ExchangeRates;
import forex.rates.api.service.DateTimeProviderService;
import forex.rates.api.service.ExchangeRatesService;
import forex.rates.api.validation.annotation.Base;
import forex.rates.api.validation.annotation.Currencies;
import forex.rates.api.validation.annotation.Date;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("rates")
public class RatesController {

    private DateTimeProviderService dateTimeProviderService;
    private ExchangeRatesService exchangeRatesService;

    public RatesController(DateTimeProviderService dateTimeProviderService, ExchangeRatesService exchangeRatesService) {
	this.dateTimeProviderService = dateTimeProviderService;
	this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping("daily")
    public DailyRatesResponse getDailyRates(
	    @Currencies String[] currencies,
	    @Base String base,
	    @Date String date
    ) throws Exception {

	List<String> currenciesList = Arrays.asList(currencies);
	LocalDate parsedDate = LocalDate.parse(date);
	ExchangeRates exchangeRates = exchangeRatesService.getExchangeRatesFor(base, currenciesList, parsedDate);

	return new DailyRatesResponse(dateTimeProviderService.getCurrentTimestamp(), exchangeRates);
    }

}
