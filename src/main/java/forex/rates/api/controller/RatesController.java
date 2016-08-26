package forex.rates.api.controller;

import forex.rates.api.model.DailyRatesResponse;
import forex.rates.api.model.ErrorResponse;
import forex.rates.api.model.ExchangeRates;
import forex.rates.api.service.DateTimeProviderService;
import forex.rates.api.service.ExchangeRatesService;
import forex.rates.api.validation.annotation.Base;
import forex.rates.api.validation.annotation.Currencies;
import forex.rates.api.validation.annotation.Date;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
	    @Currencies List<String> currencies,
	    @Base String base,
	    @Date String date
    ) throws Exception {

	LocalDate parsedDate = LocalDate.parse(date);
	ExchangeRates exchangeRates = exchangeRatesService.getExchangeRatesFor(base, currencies, parsedDate);

	if (exchangeRates.isEmpty()) {
	    throw new IllegalArgumentException("Rates for the requested date are not available: " + exchangeRates.getDate());
	}

	return new DailyRatesResponse(dateTimeProviderService.getCurrentTimestamp(), exchangeRates);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(Exception e) {
	return new ErrorResponse(dateTimeProviderService.getCurrentTimestamp(), HttpStatus.BAD_REQUEST,	e);
    }

}
