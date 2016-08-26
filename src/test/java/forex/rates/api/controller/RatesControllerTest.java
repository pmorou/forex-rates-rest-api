package forex.rates.api.controller;

import forex.rates.api.model.ExchangeRates;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
    public void test_getDailyRates_returnValues() throws Exception {
	List<String> currencies = Arrays.asList("EUR","PLN");
	ExchangeRates result = createExchangeRates("BASE", "DATE", "RATE1", "RATE2");

	when(dateTimeProviderService.getCurrentTimestamp()).thenReturn(1234L);
	when(exchangeRatesService.getExchangeRatesFor(eq("USD"), eq(currencies), any(LocalDate.class)))
		.thenReturn(result);

	mockMvc.perform(get("/rates/daily?base=USD&date=2001-01-01&currencies=EUR,PLN")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'date':DATE}"))
		.andExpect(content().json("{'base':'BASE'}"))
		.andExpect(content().json("{'rates':{'RATE1':1.0001,'RATE2':1.0001}}"));
    }

    private ExchangeRates createExchangeRates(String base, String date, String... currencies) {
	ExchangeRates exchangeRates = new ExchangeRates();
	Map<String, BigDecimal> rates = Arrays.stream(currencies)
		.collect(toMap(e -> e, e -> new BigDecimal("1.0001")));
	exchangeRates.setRates(rates);
	exchangeRates.setBase(base);
	exchangeRates.setDate(date);
	return exchangeRates;
    }

}
