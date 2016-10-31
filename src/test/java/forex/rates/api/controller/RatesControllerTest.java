package forex.rates.api.controller;

import forex.rates.api.model.ExchangeRates;
import forex.rates.api.model.Rates;
import forex.rates.api.model.request.ExchangeRatesRequest;
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
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RatesControllerTest {

    private @Mock ExchangeRatesService exchangeRatesService;

    private MockMvc mockMvc;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	RatesController ratesController = new RatesController(exchangeRatesService);
	mockMvc = MockMvcBuilders.standaloneSetup(ratesController).build();
    }

    @Test
    public void shouldReturnCorrectDailyRatesResponse() throws Exception {
	ExchangeRatesRequest request = new ExchangeRatesRequest("USD", "2001-01-01", new String[]{"EUR","PLN"});
	ExchangeRates result = createExchangeRates("USD", LocalDate.of(2001,1,1), LocalDate.of(2001,1,1), "EUR", "PLN");

	when(exchangeRatesService.perform(request)).thenReturn(result);

	mockMvc.perform(get("/rates/daily?base=USD&date=2001-01-01&currencies=EUR,PLN")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'date':2001-01-01}"))
		.andExpect(content().json("{'base':'USD'}"))
		.andExpect(content().json("{'rates':{'EUR':1.0001,'PLN':1.0001}}"));
    }

    @Test
    public void shouldReturnCorrectSeriesRatesResponse() throws Exception {
	ExchangeRatesRequest request = new ExchangeRatesRequest("USD", "2001-01-01", "2001-01-03", new String[]{"EUR","PLN"});
	ExchangeRates result = createExchangeRates("USD", LocalDate.of(2001,1,1), LocalDate.of(2001,1,3), "EUR", "PLN");

	when(exchangeRatesService.perform(request)).thenReturn(result);

	mockMvc.perform(get("/rates/series?base=USD&startDate=2001-01-01&endDate=2001-01-03&currencies=EUR,PLN")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
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
	final long daysNumber = startDate.until(endDate, ChronoUnit.DAYS);
	Map<LocalDate, Rates> ratesByDate = IntStream.rangeClosed(0, (int) daysNumber).boxed()
		.collect(toMap(i -> startDate.plusDays(i), i -> createRates(currencies)));
	return new ExchangeRates(startDate, endDate, base, ratesByDate, false);
    }

    private Rates createRates(String[] currencies) {
	return Arrays.stream(currencies).collect(Rates::new,
		(rates, currency) -> rates.addRate(currency, new BigDecimal("1.0001")),
		(rates1, rates2) -> rates2.getRates().entrySet().stream()
			    .forEach(entry -> rates1.addRate(entry.getKey(), entry.getValue())));
    }

}
