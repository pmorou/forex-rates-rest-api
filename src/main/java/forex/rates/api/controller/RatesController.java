package forex.rates.api.controller;

import forex.rates.api.model.DailyRatesResponse;
import forex.rates.api.model.ErrorResponse;
import forex.rates.api.model.ExchangeRates;
import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.service.ExchangeRatesService;
import forex.rates.api.service.DateTimeProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("rates")
public class RatesController {

    private AvailableCurrenciesService availableCurrenciesService;
    private DateTimeProviderService dateTimeProviderService;
    private ExchangeRatesService exchangeRatesService;

    public RatesController(DateTimeProviderService dateTimeProviderService, AvailableCurrenciesService availableCurrenciesService, ExchangeRatesService exchangeRatesService) {
	this.dateTimeProviderService = dateTimeProviderService;
	this.availableCurrenciesService = availableCurrenciesService;
	this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping("daily")
    public DailyRatesResponse getDailyRates(
	    @RequestParam(required = false, defaultValue = "USD") String base,
	    @RequestParam(required = false) List<String> currencies,
	    @RequestParam(required = false) String date
    ) throws Exception {

	if (!availableCurrenciesService.getList().contains(base)) {
	    throw new IllegalArgumentException("The base you requested is invalid: " + base);
	}

	if (currencies != null) {
	    for (String currency : currencies) {
		if (!availableCurrenciesService.getList().contains(currency)) {
		    throw new IllegalArgumentException("The currency you requested is invalid: " + currency);
		}
	    }
	} else {
	    currencies = availableCurrenciesService.getList();
	}

	LocalDate parsedDate = null;
	if (date != null) {
	    try {
		parsedDate = LocalDate.parse(date);
	    } catch (DateTimeParseException e) {
		throw new IllegalArgumentException("The date you requested is invalid: " + date);
	    }
	} else {
	    parsedDate = dateTimeProviderService.getTodaysDate();
	}

	if (parsedDate.isAfter(dateTimeProviderService.getTodaysDate())) {
	    throw new IllegalArgumentException("The date you requested is out of range: " + parsedDate);
	}

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
