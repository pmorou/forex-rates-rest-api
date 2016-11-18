package forex.rates.api.controller;

import forex.rates.api.model.ExchangeRates;
import forex.rates.api.model.Rates;
import forex.rates.api.model.request.ExchangeRatesRequest;
import forex.rates.api.service.ExchangeRatesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExchangeControllerTest {

    public static final LocalDate DATE = LocalDate.of(2001, 1, 1);

    private @Mock ExchangeRatesService exchangeRatesService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(new ExchangeController(exchangeRatesService)).build();
    }

    @Test
    public void shouldReturnUsdToJpyDailyExchangeResponse() throws Exception {
	Rates jpyRates = createRates(singletonMap("JPY", new BigDecimal("1")));
	ExchangeRates jpyExchangeRates = new ExchangeRates(DATE, DATE, "USD", singletonMap(DATE, jpyRates), false);
	ExchangeRatesRequest exchangeRatesRequest = new ExchangeRatesRequest("USD", DATE.toString(), new String[]{"JPY"});
	when(exchangeRatesService.perform(exchangeRatesRequest)).thenReturn(jpyExchangeRates);

	mockMvc.perform(get("/exchange/daily?date=2001-01-01&amount=10&from=USD&to=JPY"))
		.andExpect(status().isOk())
		.andExpect(content().json("{'date':2001-01-01}"))
		.andExpect(content().json("{'amount':10}"))
		.andExpect(content().json("{'from':'USD'}"))
		.andExpect(content().json("{'to':{'JPY':10}}"));
    }

    private Rates createRates(Map<String, BigDecimal> currencyValuePairs) {
	return currencyValuePairs.entrySet().stream().collect(Rates::new,
		(rates, entry) -> rates.addRate(entry.getKey(), entry.getValue()),
		(rates1, rates2) -> rates2.getRates().entrySet().stream()
			.forEach(entry -> rates1.addRate(entry.getKey(), entry.getValue())));
    }

}