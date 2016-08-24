package forex.rates.api.controller;

import forex.rates.api.model.ExchangeRates;
import forex.rates.api.service.AvailableCurrenciesService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RatesControllerTestDaily {

    private static final List<String> AVAILABLE_CURRENCIES = new ArrayList<>(Arrays.asList("EUR", "USD", "JPY"));
    private static final LocalDate DATE_2010_01_20 = LocalDate.of(2010, 01, 20);

    private @Mock AvailableCurrenciesService availableCurrenciesService;
    private @Mock DateTimeProviderService dateTimeProviderService;
    private @Mock ExchangeRatesService exchangeRatesService;

    private MockMvc mockMvc;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	RatesController ratesController = new RatesController(dateTimeProviderService, availableCurrenciesService, exchangeRatesService);
	mockMvc = MockMvcBuilders.standaloneSetup(ratesController).build();

	when(availableCurrenciesService.getList()).thenReturn(AVAILABLE_CURRENCIES);
	when(dateTimeProviderService.getCurrentTimestamp()).thenReturn(1234L);
	when(dateTimeProviderService.getTodaysDate()).thenReturn(DATE_2010_01_20);
	when(dateTimeProviderService.getTodaysDateAsString()).thenReturn(DATE_2010_01_20.toString());
    }

    @Test
    public void test_getDailyRates_defaultParams() throws Exception {
	ExchangeRates exchangeRates = createExchangeRates("USD", "2010-01-20", "EUR", "JPY");

	when(exchangeRatesService.getExchangeRatesFor(eq("USD"), anyList(), eq(DATE_2010_01_20)))
		.thenReturn(exchangeRates);

	mockMvc.perform(get("/rates/daily")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'date':2010-01-20}"))
		.andExpect(content().json("{'base':'USD'}"))
		.andExpect(content().json("{'rates':{'EUR':1.0001,'JPY':1.0001}}"));
    }

    @Test
    public void test_getDailyRates_eurBase() throws Exception {
	ExchangeRates exchangeRates = createExchangeRates("EUR", "2010-01-20", "USD", "JPY");

	when(exchangeRatesService.getExchangeRatesFor(eq("EUR"), anyList(), eq(DATE_2010_01_20)))
		.thenReturn(exchangeRates);

	mockMvc.perform(get("/rates/daily?base=EUR")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'date':2010-01-20}"))
		.andExpect(content().json("{'base':'EUR'}"))
		.andExpect(content().json("{'rates':{'USD':1.0001,'JPY':1.0001}}"));
    }

    @Test
    public void test_getDailyRates_invalidBase() throws Exception {
	mockMvc.perform(get("/rates/daily?base=EURO")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest())
		.andExpect(content().json("{'error':true}"))
		.andExpect(content().json("{'httpStatus':400}"))
		.andExpect(content().json("{'message':'Bad Request'}"))
		.andExpect(content().json("{'description':'The base you requested is invalid: EURO'}"));
    }

    @Test
    public void test_getDailyRates_eurCurrency() throws Exception {
	ExchangeRates exchangeRates = createExchangeRates("USD", "2010-01-20", "EUR");

	when(exchangeRatesService.getExchangeRatesFor(eq("USD"), anyList(), eq(DATE_2010_01_20)))
		.thenReturn(exchangeRates);

	mockMvc.perform(get("/rates/daily?currencies=EUR")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'date':2010-01-20}"))
		.andExpect(content().json("{'base':'USD'}"))
		.andExpect(content().json("{'rates':{'EUR':1.0001}}"));
    }

    @Test
    public void test_getDailyRates_usdJpyCurrencies() throws Exception {
	ExchangeRates exchangeRates = createExchangeRates("USD", "2010-01-20", "USD", "JPY");

	when(exchangeRatesService.getExchangeRatesFor(eq("USD"), anyList(), eq(DATE_2010_01_20)))
		.thenReturn(exchangeRates);

	mockMvc.perform(get("/rates/daily?currencies=USD,JPY")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'date':2010-01-20}"))
		.andExpect(content().json("{'base':'USD'}"))
		.andExpect(content().json("{'rates':{'USD':1.0001,'JPY':1.0001}}"));
    }

    @Test
    public void test_getDailyRates_invalidCurrency() throws Exception {
	mockMvc.perform(get("/rates/daily?currencies=USDD")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest())
		.andExpect(content().json("{'error':true}"))
		.andExpect(content().json("{'httpStatus':400}"))
		.andExpect(content().json("{'message':'Bad Request'}"))
		.andExpect(content().json("{'description':'The currency you requested is invalid: USDD'}"));
    }

    @Test
    public void test_getDailyRates_validDate() throws Exception {
	ExchangeRates exchangeRates = createExchangeRates("USD", "2009-01-20", "USD", "JPY");

	when(exchangeRatesService.getExchangeRatesFor(eq("USD"), anyList(), eq(DATE_2010_01_20.minusYears(1))))
		.thenReturn(exchangeRates);

	mockMvc.perform(get("/rates/daily?date=2009-01-20")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'date':2009-01-20}"))
		.andExpect(content().json("{'base':'USD'}"))
		.andExpect(content().json("{'rates':{'USD':1.0001,'JPY':1.0001}}"));
    }

    @Test
    public void test_getDailyRates_invalidDate_wrongFormat() throws Exception {
	mockMvc.perform(get("/rates/daily?date=2010-0120")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest())
		.andExpect(content().json("{'error':true}"))
		.andExpect(content().json("{'httpStatus':400}"))
		.andExpect(content().json("{'message':'Bad Request'}"))
		.andExpect(content().json("{'description':'The date you requested is invalid: 2010-0120'}"));
    }

    @Test
    public void test_getDailyRates_invalidDate_noData() throws Exception {
	ExchangeRates exchangeRates = createExchangeRates("USD", "2010-01-20");
	exchangeRates.setEmpty();

	when(exchangeRatesService.getExchangeRatesFor(eq("USD"), anyList(), eq(DATE_2010_01_20)))
		.thenReturn(exchangeRates);

	mockMvc.perform(get("/rates/daily?date=2010-01-20")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest())
		.andExpect(content().json("{'error':true}"))
		.andExpect(content().json("{'httpStatus':400}"))
		.andExpect(content().json("{'message':'Bad Request'}"))
		.andExpect(content().json("{'description':'Rates for the requested date are not available: 2010-01-20'}"));
    }

    @Test
    public void test_getDailyRates_invalidDate_fromFuture() throws Exception {
	mockMvc.perform(get("/rates/daily?date=2020-01-20")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest())
		.andExpect(content().json("{'error':true}"))
		.andExpect(content().json("{'httpStatus':400}"))
		.andExpect(content().json("{'message':'Bad Request'}"))
		.andExpect(content().json("{'description':'The date you requested is out of range: 2020-01-20'}"));
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
