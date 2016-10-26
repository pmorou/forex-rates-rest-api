package forex.rates.api.service.impl;

import forex.rates.api.configuration.DataSetContext;
import forex.rates.api.model.ExchangeRates;
import forex.rates.api.model.Rates;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static java.util.Arrays.asList;
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
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));
	when(currencyDefinitionService.getOneByCodeName("EUR")).thenReturn(EUR_DEFINITION);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("USD", "1.4902"));
    }

    @Test
    public void shouldReturnRatesForEurWithBaseUsd() throws Exception {
	// Given
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, Collections.emptyList())).thenReturn(Collections.emptyList());
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, USD_DEFINITION)).thenReturn(USD_RATE);
	when(currencyDefinitionService.getOneByCodeName("USD")).thenReturn(USD_DEFINITION);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("EUR"))).thenReturn(asList(EUR_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("USD", "2001-01-01", new String[]{"EUR"}));

	// Then
	assertThat(result.getBase()).isEqualTo("USD");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("EUR", "0.6711"));
    }

    @Test
    public void shouldReturnRatesForUsdWithBasePln() throws Exception {
	// Given
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyDefinitionService.getOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("PLN", "2001-01-01", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("PLN");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("USD", "1.3544"));
    }

    @Test
    public void shouldReturnRatesForUsdAndPlnWithBaseEur() throws Exception {
	// Given
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION, PLN_DEFINITION))).thenReturn(asList(USD_RATE, PLN_RATE));
	when(currencyDefinitionService.getOneByCodeName("EUR")).thenReturn(EUR_DEFINITION);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD", "PLN"))).thenReturn(asList(USD_DEFINITION, PLN_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"USD", "PLN"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("USD", "1.4902", "PLN", "1.1002"));
    }

    @Test
    public void shouldReturnRatesForEurAndUsdWithBasePln() throws Exception {
	// Given
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));
	when(currencyDefinitionService.getOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("PLN", "2001-01-01", new String[]{"EUR", "USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("PLN");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("EUR", "0.9089", "USD", "1.3544"));
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
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("USD", "1.4902"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_02)).isEqualTo(createRates("USD", "1.4815"));

    }

    @Test
    public void shouldReturnRatesForUsdWithBasePlnBetweenTwoDates() throws Exception {
	// Given
	when(currencyDefinitionService.getAllByCodeNameIn(asList("USD"))).thenReturn(asList(USD_DEFINITION));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE));
	when(currencyRateService.getAllByDateAndCurrencyIn(DATE_2001_01_02, asList(USD_DEFINITION))).thenReturn(asList(USD_RATE_NEXT_DAY));
	when(currencyDefinitionService.getOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyRateService.getOneByDateAndCurrency(DATE_2001_01_02, PLN_DEFINITION)).thenReturn(PLN_RATE_NEXT_DAY);

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("PLN", "2001-01-01", "2001-01-02", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("PLN");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_02);
	assertThat(result.getRatesByDate()).hasSize(2);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("USD", "1.3544"));
	assertThat(result.getRatesByDate().get(DATE_2001_01_02)).isEqualTo(createRates("USD", "1.3468"));

    }

    private CurrencyRate createCurrencyRate(CurrencyDefinition currencyDefinition, BigDecimal exchangeRate, LocalDate date) {
	return new CurrencyRate(exchangeRate, date, currencyDefinition);
    }

    private CurrencyDefinition createCurrencyDefinition(String codeName, int precision) {
	return new CurrencyDefinition(codeName, precision);
    }

    private Rates createRates(String... currencyValuePairs) {
	Rates expectedRates = new Rates();
	Iterator<String> iterator = Arrays.stream(currencyValuePairs).iterator();
	while (iterator.hasNext()) {
	    expectedRates.addRate(iterator.next(), new BigDecimal(iterator.next()));
	}
	return expectedRates;
    }

}
