package forex.rates.api.service.impl;

import forex.rates.api.dataset.DataSetContext;
import forex.rates.api.model.exchange.ExchangeRates;
import forex.rates.api.model.exchange.Rates;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.model.request.ExchangeRatesRequest;
import forex.rates.api.service.CurrencyDefinitionService;
import forex.rates.api.service.CurrencyRateService;
import forex.rates.api.service.ExchangeRatesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ExchangeRatesServiceImplTest {

    private final LocalDate DATE_2001_01_01 = LocalDate.of(2001, 1, 1);
    private final LocalDate DATE_2001_01_02 = LocalDate.of(2001, 1, 2);
    private final CurrencyDefinition USD_DEFINITION = createCurrencyDefinition("USD", 4);
    private final CurrencyDefinition PLN_DEFINITION = createCurrencyDefinition("PLN", 4);
    private final CurrencyDefinition EUR_DEFINITION = createCurrencyDefinition("EUR", 4);
    private final CurrencyRate USD_RATE = createCurrencyRate(USD_DEFINITION, new BigDecimal("1.4902000"), DATE_2001_01_01);
    private final CurrencyRate PLN_RATE = createCurrencyRate(PLN_DEFINITION, new BigDecimal("1.1002000"), DATE_2001_01_01);
    private final CurrencyRate USD_RATE_NEXT_DAY = createCurrencyRate(USD_DEFINITION, new BigDecimal("1.4815000"), DATE_2001_01_02);
    private final CurrencyRate PLN_RATE_NEXT_DAY = createCurrencyRate(PLN_DEFINITION, new BigDecimal("1.1000000"), DATE_2001_01_02);

    private @Mock CurrencyRateService currencyRateService;
    private @Mock CurrencyDefinitionService currencyDefinitionService;
    private @Mock DataSetContext dataSetContext;

    private ExchangeRatesService service;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	when(dataSetContext.getBaseCurrency()).thenReturn("EUR");
	service = new ExchangeRatesServiceImpl(currencyRateService, currencyDefinitionService, dataSetContext);
    }

    @Test
    public void shouldReturnRatesForUsdWithBaseEur() throws Exception {
	// Given
	when(currencyDefinitionService.getOneByCodeName("EUR")).thenReturn(EUR_DEFINITION);
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePair = singletonMap("USD", new BigDecimal("1.4902"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePair));
    }

    @Test
    public void shouldReturnRatesForEurWithBaseUsd() throws Exception {
	// Given
	when(currencyDefinitionService.getOneByCodeName("USD")).thenReturn(USD_DEFINITION);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, USD_DEFINITION)).thenReturn(USD_RATE);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("EUR"))).thenReturn(asList(EUR_DEFINITION));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(EUR_DEFINITION))).thenReturn(asList(USD_RATE));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("USD", "2001-01-01", new String[]{"EUR"}));

	// Then
	assertThat(result.getBase()).isEqualTo("USD");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePair = singletonMap("EUR", new BigDecimal("0.6711"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePair));
    }

    @Test
    public void shouldReturnRatesForUsdWithoutEurWithBaseEur() throws Exception {
	// Given
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"EUR", "USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePair = singletonMap("USD", new BigDecimal("1.4902"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePair));
    }

    @Test
    public void shouldReturnRatesForEurWithoutUsdWithBaseUsd() throws Exception {
	// Given
	when(currencyDefinitionService.getOneByCodeName("USD")).thenReturn(USD_DEFINITION);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, USD_DEFINITION)).thenReturn(USD_RATE);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("USD", "2001-01-01", new String[]{"EUR", "USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("USD");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePair = singletonMap("EUR", new BigDecimal("0.6711"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePair));
    }

    @Test
    public void shouldReturnEmptyRatesWithBaseEur() throws Exception {
	// Given
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, emptyList())).thenReturn(emptyList());
	when(currencyDefinitionService.getAllByCodeNameIn(asList("EUR"))).thenReturn(asList(EUR_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"EUR"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePair = Collections.emptyMap();
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePair));
    }

    @Test
    public void shouldReturnEmptyRatesWithBaseUsd() throws Exception {
	// Given
	when(currencyDefinitionService.getOneByCodeName("USD")).thenReturn(USD_DEFINITION);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, USD_DEFINITION)).thenReturn(USD_RATE);
	when(currencyDefinitionService.getAllByCodeNameIn(emptyList())).thenReturn(emptyList());
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, emptyList())).thenReturn(emptyList());

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("USD", "2001-01-01", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("USD");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePair = Collections.emptyMap();
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePair));
    }

    @Test
    public void shouldReturnRatesForUsdWithBasePln() throws Exception {
	// Given
	when(currencyDefinitionService.getOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("PLN", "2001-01-01", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("PLN");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePair = singletonMap("USD", new BigDecimal("1.3544"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePair));
    }

    @Test
    public void shouldReturnRatesForUsdAndPlnWithBaseEur() throws Exception {
	// Given
	when(currencyDefinitionService.getOneByCodeName("EUR")).thenReturn(EUR_DEFINITION);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD", "PLN"))).thenReturn(asList(USD_DEFINITION, PLN_DEFINITION));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION, PLN_DEFINITION))).thenReturn(asList(USD_RATE, PLN_RATE));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"USD", "PLN"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePairs = new HashMap() {{
	    put("USD", new BigDecimal("1.4902"));
	    put("PLN", new BigDecimal("1.1002"));
	}};
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePairs));
    }

    @Test
    public void shouldReturnRatesForEurAndUsdWithBasePln() throws Exception {
	// Given
	when(currencyDefinitionService.getOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("PLN", "2001-01-01", new String[]{"EUR", "USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("PLN");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	Map<String, BigDecimal> expectedCurrencyNameValuePairs = new HashMap() {{
	    put("EUR", new BigDecimal("0.9089"));
	    put("USD", new BigDecimal("1.3544"));
	}};
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePairs));
    }

    @Test
    public void shouldReturnRatesForUsdWithBaseEurBetweenTwoDates() throws Exception {
	// Given
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_02, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE_NEXT_DAY));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", "2001-01-02", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_02);
	Map<String, BigDecimal> expectedCurrencyNameValuePairFirstDay = singletonMap("USD", new BigDecimal("1.4902"));
	Map<String, BigDecimal> expectedCurrencyNameValuePairSecondDay = singletonMap("USD", new BigDecimal("1.4815"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePairFirstDay));
	assertThat(result.getRatesByDate().get(DATE_2001_01_02)).isEqualTo(createRates(expectedCurrencyNameValuePairSecondDay));

    }

    @Test
    public void shouldReturnRatesForUsdWithBasePlnBetweenTwoDates() throws Exception {
	// Given
	when(currencyDefinitionService.getOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_02, PLN_DEFINITION)).thenReturn(PLN_RATE_NEXT_DAY);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_02, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE_NEXT_DAY));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("PLN", "2001-01-01", "2001-01-02", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("PLN");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_02);
	assertThat(result.getRatesByDate()).hasSize(2);
	Map<String, BigDecimal> expectedCurrencyNameValuePairFirstDay = singletonMap("USD", new BigDecimal("1.3544"));
	Map<String, BigDecimal> expectedCurrencyNameValuePairSecondDay = singletonMap("USD", new BigDecimal("1.3468"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates(expectedCurrencyNameValuePairFirstDay));
	assertThat(result.getRatesByDate().get(DATE_2001_01_02)).isEqualTo(createRates(expectedCurrencyNameValuePairSecondDay));
    }

    private CurrencyRate createCurrencyRate(CurrencyDefinition currencyDefinition, BigDecimal exchangeRate, LocalDate date) {
	return new CurrencyRate(exchangeRate, date, currencyDefinition);
    }

    private CurrencyDefinition createCurrencyDefinition(String codeName, Integer precision) {
	return new CurrencyDefinition(codeName, precision);
    }

    private Rates createRates(Map<String, BigDecimal> currencyValuePairs) {
	return currencyValuePairs.entrySet().stream().collect(Rates::new,
		(rates, entry) -> rates.addRate(entry.getKey(), entry.getValue()),
		(rates1, rates2) -> rates2.getRates().entrySet().stream()
			.forEach(entry -> rates1.addRate(entry.getKey(), entry.getValue())));
    }

}
