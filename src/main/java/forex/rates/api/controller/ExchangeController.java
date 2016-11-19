package forex.rates.api.controller;

import forex.rates.api.model.exchange.ExchangeRates;
import forex.rates.api.model.exchange.ExchangeTransaction;
import forex.rates.api.model.request.ExchangeRatesRequest;
import forex.rates.api.model.response.DailyExchangeResponse;
import forex.rates.api.model.response.SeriesExchangeResponse;
import forex.rates.api.service.ExchangeRatesService;
import forex.rates.api.validation.annotation.Base;
import forex.rates.api.validation.annotation.Currencies;
import forex.rates.api.validation.annotation.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("exchange")
public class ExchangeController {

    private final ExchangeRatesService exchangeRatesService;

    @Autowired
    public ExchangeController(ExchangeRatesService exchangeRatesService) {
	this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping("daily")
    public DailyExchangeResponse dailyExchangeResponse(
	    @Date String date,
	    String amount,
	    @Base String from,
	    @Currencies String[] to
    ) {
	ExchangeRatesRequest exchangeRatesRequest = new ExchangeRatesRequest(from, date, to);
	ExchangeRates exchangeRates = exchangeRatesService.perform(exchangeRatesRequest);
	int parsedAmount = Integer.parseInt(amount);
	ExchangeTransaction exchangeTransaction = new ExchangeTransaction(exchangeRates, parsedAmount);
	return new DailyExchangeResponse(exchangeTransaction);
    }

    @GetMapping("series")
    public SeriesExchangeResponse seriesExchangeResponse(
	    @Date @RequestParam String startDate,
	    @Date @RequestParam String endDate,
	    String amount,
	    @Base String from,
	    @Currencies String[] to
    ) {
	ExchangeRatesRequest exchangeRatesRequest = new ExchangeRatesRequest(from, startDate, endDate, to);
	ExchangeRates exchangeRates = exchangeRatesService.perform(exchangeRatesRequest);
	int parsedAmount = Integer.parseInt(amount);
	ExchangeTransaction exchangeTransaction = new ExchangeTransaction(exchangeRates, parsedAmount);
	return new SeriesExchangeResponse(exchangeTransaction);
    }

}
