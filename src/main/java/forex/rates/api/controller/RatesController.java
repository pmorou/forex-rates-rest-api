package forex.rates.api.controller;

import forex.rates.api.model.response.DailyRatesResponse;
import forex.rates.api.model.ExchangeRates;
import forex.rates.api.model.ExchangeRatesRequest;
import forex.rates.api.model.response.SeriesRatesResponse;
import forex.rates.api.service.DateTimeProviderService;
import forex.rates.api.service.ExchangeRatesService;
import forex.rates.api.validation.annotation.Base;
import forex.rates.api.validation.annotation.Currencies;
import forex.rates.api.validation.annotation.Date;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rates")
public class RatesController {

    private final DateTimeProviderService dateTimeProviderService;
    private final ExchangeRatesService exchangeRatesService;

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

	ExchangeRates exchangeRates = exchangeRatesService.perform(new ExchangeRatesRequest(base, date, currencies));

	return new DailyRatesResponse(dateTimeProviderService.getCurrentTimestamp(), exchangeRates);
    }

    @GetMapping("series")
    public SeriesRatesResponse getSeriesRates(
	    @Currencies String[] currencies,
	    @Base String base,
	    @Date @RequestParam String startDate,
	    @Date @RequestParam String endDate
    ) throws Exception {

	ExchangeRates exchangeRates = exchangeRatesService.perform(new ExchangeRatesRequest(base, startDate, endDate, currencies));

	return new SeriesRatesResponse(dateTimeProviderService.getCurrentTimestamp(), exchangeRates);
    }

}
