package forex.rates.api.service.impl;

import forex.rates.api.model.ExchangeRates;
import forex.rates.api.model.request.ExchangeRatesRequest;
import forex.rates.api.model.Rates;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import forex.rates.api.repository.CurrencyRateRepository;
import forex.rates.api.service.ExchangeRatesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ExchangeRatesServiceImplTest {

    private final LocalDate DATE_2001_01_01 = LocalDate.of(2001, 1, 1);
    private final LocalDate DATE_2001_01_02 = LocalDate.of(2001, 1, 2);
    private final CurrencyDefinition USD_DEFINITION = createCurrencyDefinition("USD", 4);
    private final CurrencyDefinition PLN_DEFINITION = createCurrencyDefinition("PLN", 4);
    private final CurrencyDefinition EUR_DEFINITION = createCurrencyDefinition("EUR", 4);
    private final CurrencyRate USD_RATE = createCurrencyRate(USD_DEFINITION, new BigDecimal("1.4902"), DATE_2001_01_01);
    private final CurrencyRate PLN_RATE = createCurrencyRate(PLN_DEFINITION, new BigDecimal("1.1002"), DATE_2001_01_01);
    private final CurrencyRate USD_RATE_NEXT_DAY = createCurrencyRate(USD_DEFINITION, new BigDecimal("1.4815"), DATE_2001_01_02);
    private final CurrencyRate PLN_RATE_NEXT_DAY = createCurrencyRate(PLN_DEFINITION, new BigDecimal("1.1000"), DATE_2001_01_02);

    private @Mock CurrencyRateRepository currencyRateRepository;
    private @Mock CurrencyDefinitionRepository currencyDefinitionRepository;

    private ExchangeRatesService service;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	service = new ExchangeRatesServiceImpl(currencyRateRepository, currencyDefinitionRepository);
    }

    @Test
    public void shouldGetRatesForUSD_withBaseEUR() throws Exception {
	// Given
	List<CurrencyRate> currencyRates = new ArrayList<>();
	currencyRates.add(createCurrencyRate(USD_DEFINITION, new BigDecimal("1.4902"), DATE_2001_01_01));
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01, Arrays.asList(USD_DEFINITION))).thenReturn(currencyRates);
	when(currencyDefinitionRepository.findOneByCodeName("EUR")).thenReturn(EUR_DEFINITION);
	when(currencyDefinitionRepository.findAllByCodeNameIn(Arrays.asList("USD"))).thenReturn(Arrays.asList(USD_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("USD", "1.4902"));
    }

    @Test
    public void shouldGetRatesForEUR_withBaseUSD() throws Exception {
	// Given
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01, Collections.emptyList())).thenReturn(Collections.emptyList());
	when(currencyRateRepository.findOneByDateAndCurrency(DATE_2001_01_01, USD_DEFINITION)).thenReturn(USD_RATE);
	when(currencyDefinitionRepository.findOneByCodeName("USD")).thenReturn(USD_DEFINITION);
	when(currencyDefinitionRepository.findAllByCodeNameIn(Arrays.asList("EUR"))).thenReturn(Arrays.asList(EUR_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("USD", "2001-01-01", new String[]{"EUR"}));

	// Then
	assertThat(result.getBase()).isEqualTo("USD");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("EUR", "0.6711"));
    }

    @Test
    public void shouldGetRatesForUSD_withBasePLN() throws Exception {
	// Given
	List<CurrencyRate> currencyRates = new ArrayList<>();
	currencyRates.add(createCurrencyRate(USD_DEFINITION, new BigDecimal("1.4902"), DATE_2001_01_01));
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01, singletonList(USD_DEFINITION))).thenReturn(currencyRates);
	when(currencyRateRepository.findOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyDefinitionRepository.findOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyDefinitionRepository.findAllByCodeNameIn(Arrays.asList("USD"))).thenReturn(Arrays.asList(USD_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("PLN", "2001-01-01", new String[]{"USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("PLN");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("USD", "1.3544"));
    }

    @Test
    public void shouldGetRatesForUSDPLN_withBaseEUR() throws Exception {
	// Given
	List<CurrencyRate> currencyRates = new ArrayList<>(Arrays.asList(USD_RATE, PLN_RATE));
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01, Arrays.asList(USD_DEFINITION, PLN_DEFINITION))).thenReturn(currencyRates);
	when(currencyDefinitionRepository.findOneByCodeName("EUR")).thenReturn(EUR_DEFINITION);
	when(currencyDefinitionRepository.findAllByCodeNameIn(Arrays.asList("USD", "PLN"))).thenReturn(Arrays.asList(USD_DEFINITION, PLN_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"USD", "PLN"}));

	// Then
	assertThat(result.getBase()).isEqualTo("EUR");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("USD", "1.4902", "PLN", "1.1002"));
    }

    @Test
    public void shouldGetRatesForEURUSD_withBasePLN() throws Exception {
	// Given
	List<CurrencyRate> currencyRates = new ArrayList<>(Arrays.asList(USD_RATE));
	when(currencyRateRepository.findOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01, Arrays.asList(USD_DEFINITION))).thenReturn(currencyRates);
	when(currencyDefinitionRepository.findOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyDefinitionRepository.findAllByCodeNameIn(Arrays.asList("USD"))).thenReturn(Arrays.asList(USD_DEFINITION));

	// When
	ExchangeRates result = service.perform(new ExchangeRatesRequest("PLN", "2001-01-01", new String[]{"EUR", "USD"}));

	// Then
	assertThat(result.getBase()).isEqualTo("PLN");
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getEndDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getRatesByDate().get(DATE_2001_01_01)).isEqualTo(createRates("EUR", "0.9089", "USD", "1.3544"));
    }

    @Test
    public void shouldGetRatesForUsdWithBaseEurBetweenTwoDates() throws Exception {
	// Given
	when(currencyDefinitionRepository.findAllByCodeNameIn(Arrays.asList("USD"))).thenReturn(Arrays.asList(USD_DEFINITION));
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01, Arrays.asList(USD_DEFINITION))).thenReturn(Arrays.asList(USD_RATE));
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_02, Arrays.asList(USD_DEFINITION))).thenReturn(Arrays.asList(USD_RATE_NEXT_DAY));

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
    public void shouldGetRatesForUsdWithBasePlnBetweenTwoDates() throws Exception {
	// Given
	when(currencyDefinitionRepository.findAllByCodeNameIn(Arrays.asList("USD"))).thenReturn(Arrays.asList(USD_DEFINITION));
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01, Arrays.asList(USD_DEFINITION))).thenReturn(Arrays.asList(USD_RATE));
	when(currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_02, Arrays.asList(USD_DEFINITION))).thenReturn(Arrays.asList(USD_RATE_NEXT_DAY));
	when(currencyDefinitionRepository.findOneByCodeName("PLN")).thenReturn(PLN_DEFINITION);
	when(currencyRateRepository.findOneByDateAndCurrency(DATE_2001_01_01, PLN_DEFINITION)).thenReturn(PLN_RATE);
	when(currencyRateRepository.findOneByDateAndCurrency(DATE_2001_01_02, PLN_DEFINITION)).thenReturn(PLN_RATE_NEXT_DAY);

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
	CurrencyRate currencyRate = new CurrencyRate();
	currencyRate.setExchangeRate(exchangeRate);
	currencyRate.setCurrency(currencyDefinition);
	currencyRate.setDate(date);
	return currencyRate;
    }

    private CurrencyDefinition createCurrencyDefinition(String codeName, int precision) {
	CurrencyDefinition currencyDefinition = new CurrencyDefinition();
	currencyDefinition.setCodeName(codeName);
	currencyDefinition.setPrecision(precision);
	return currencyDefinition;
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
