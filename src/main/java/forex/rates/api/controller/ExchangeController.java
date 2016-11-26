package forex.rates.api.controller;

import forex.rates.api.model.exchange.ExchangeRates;
import forex.rates.api.model.exchange.ExchangeTransactions;
import forex.rates.api.model.request.ExchangeRatesRequest;
import forex.rates.api.model.response.DailyExchangeResponse;
import forex.rates.api.model.response.SeriesExchangeResponse;
import forex.rates.api.service.ExchangeRatesService;
import forex.rates.api.validation.annotation.Amount;
import forex.rates.api.validation.annotation.Base;
import forex.rates.api.validation.annotation.Currencies;
import forex.rates.api.validation.annotation.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
	    @Amount String amount,
	    @Base String from,
	    @Currencies String[] to
    ) {
	ExchangeRatesRequest exchangeRatesRequest = new ExchangeRatesRequest(from, date, to);
	ExchangeTransactions exchangeTransactions = createExchangeTransactions(amount, exchangeRatesRequest);
	return new DailyExchangeResponse(exchangeTransactions);
    }

    @GetMapping("series")
    public SeriesExchangeResponse seriesExchangeResponse(
	    @Date @RequestParam String startDate,
	    @Date @RequestParam String endDate,
	    @Amount String amount,
	    @Base String from,
	    @Currencies String[] to
    ) {
	ExchangeRatesRequest exchangeRatesRequest = new ExchangeRatesRequest(from, startDate, endDate, to);
	ExchangeTransactions exchangeTransactions = createExchangeTransactions(amount, exchangeRatesRequest);
	return new SeriesExchangeResponse(exchangeTransactions);
    }

    private ExchangeTransactions createExchangeTransactions(String amount, ExchangeRatesRequest exchangeRatesRequest) {
	ExchangeRates exchangeRates = exchangeRatesService.perform(exchangeRatesRequest);
	return new ExchangeTransactions(exchangeRates, new BigDecimal(amount));
    }

}
