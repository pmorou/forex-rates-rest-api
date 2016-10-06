package forex.rates.api.controller;

import forex.rates.api.model.ExchangeRates;
import forex.rates.api.model.request.ExchangeRatesRequest;
import forex.rates.api.model.Rates;
import forex.rates.api.service.DateTimeProviderService;
import forex.rates.api.service.ExchangeRatesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RatesControllerTest {

    private @Mock DateTimeProviderService dateTimeProviderService;
    private @Mock ExchangeRatesService exchangeRatesService;

    private MockMvc mockMvc;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	RatesController ratesController = new RatesController(dateTimeProviderService, exchangeRatesService);
	mockMvc = MockMvcBuilders.standaloneSetup(ratesController).build();
    }

    @Test
    public void shouldReturnCorrectDailyRatesResponse() throws Exception {
	ExchangeRatesRequest request = new ExchangeRatesRequest("USD", "2001-01-01", new String[]{"EUR","PLN"});
	ExchangeRates result = createExchangeRates("USD", LocalDate.of(2001,1,1), LocalDate.of(2001,1,1), "EUR", "PLN");

	when(dateTimeProviderService.getCurrentTimestamp()).thenReturn(1234L);
	when(exchangeRatesService.perform(request)).thenReturn(result);

	mockMvc.perform(get("/rates/daily?base=USD&date=2001-01-01&currencies=EUR,PLN")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'date':2001-01-01}"))
		.andExpect(content().json("{'base':'USD'}"))
		.andExpect(content().json("{'rates':{'EUR':1.0001,'PLN':1.0001}}"));
    }

    @Test
    public void shouldReturnCorrectSeriesRatesResponse() throws Exception {
	ExchangeRatesRequest request = new ExchangeRatesRequest("USD", "2001-01-01", "2001-01-03", new String[]{"EUR","PLN"});
	ExchangeRates result = createExchangeRates("USD", LocalDate.of(2001,1,1), LocalDate.of(2001,1,3), "EUR", "PLN");

	when(dateTimeProviderService.getCurrentTimestamp()).thenReturn(1234L);
	when(exchangeRatesService.perform(request)).thenReturn(result);

	mockMvc.perform(get("/rates/series?base=USD&startDate=2001-01-01&endDate=2001-01-03&currencies=EUR,PLN")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'startDate':2001-01-01}"))
		.andExpect(content().json("{'endDate':2001-01-03}"))
		.andExpect(content().json("{'base':'USD'}"))
		.andExpect(content().json("{'rates':"
			+ "{'2001-01-01':{'EUR':1.0001,'PLN':1.0001}"
			+ ",'2001-01-02':{'EUR':1.0001,'PLN':1.0001}"
			+ ",'2001-01-03':{'EUR':1.0001,'PLN':1.0001}"
			+ "}}"));
    }

    private ExchangeRates createExchangeRates(String base, LocalDate startDate, LocalDate endDate, String... currencies) {
	Map<LocalDate, Rates> ratesByDate = new HashMap<>();
	long daysNumber = startDate.until(endDate, ChronoUnit.DAYS);
	LocalDate date = startDate;
	for (int i = 0; i <= daysNumber; i++) {
	    Rates rate = new Rates();
	    Arrays.stream(currencies).forEach(c -> rate.addRate(c, new BigDecimal("1.0001")));
	    ratesByDate.put(date, rate);
	    date = date.plusDays(1);
	}
	ExchangeRates exchangeRates = new ExchangeRates();
	exchangeRates.setRatesByDate(ratesByDate);
	exchangeRates.setBase(base);
	exchangeRates.setStartDate(startDate);
	exchangeRates.setEndDate(endDate);
	return exchangeRates;
    }

}
