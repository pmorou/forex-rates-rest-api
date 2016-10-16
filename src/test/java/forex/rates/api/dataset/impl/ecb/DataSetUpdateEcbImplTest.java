package forex.rates.api.dataset.impl.ecb;

import forex.rates.api.configuration.DataSetContext;
import forex.rates.api.dataset.DataSetUpdate;
import forex.rates.api.dataset.ExtractedCurrencyRate;
import forex.rates.api.http.client.HttpClient;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.service.CurrencyDefinitionService;
import forex.rates.api.service.DateTimeProviderService;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DataSetUpdateEcbImplTest {

    private final CurrencyDefinition USD_DEFINITION = createCurrencyDefinition("USD", 4);
    private final CurrencyDefinition JPY_DEFINITION = createCurrencyDefinition("JPY", 2);
    private final Map.Entry<String, String> USD_ENTRY = createEntry("2001-01-01", "1.1185");
    private final Map.Entry<String, String> JPY_ENTRY = createEntry("2001-01-01", "116.00");
    private final CurrencyRate JPY_CURRENCY_RATE = createCurrencyRate(JPY_DEFINITION, JPY_ENTRY);
    private final CurrencyRate USD_CURRENCY_RATE = createCurrencyRate(USD_DEFINITION, USD_ENTRY);

    private @Mock HttpClient httpClient;
    private @Mock DataSetContext dataSetContext;
    private @Mock ExtractedCurrencyRate extractedCurrencyRate;
    private @Mock CurrencyDefinitionService currencyDefinitionService;
    private @Mock DateTimeProviderService dateTimeProviderService;

    private DataSetUpdate dataSetUpdateEcb;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
	dataSetUpdateEcb = new DataSetUpdateEcbImpl(httpClient, dataSetContext, extractedCurrencyRate,
		currencyDefinitionService, dateTimeProviderService);
    }

    @Test
    public void shouldReturnUsdAndJpyCurrencyRates() throws Exception {
	// Given
	when(httpClient.getInputStream(any())).thenReturn(getTestInputStream());
	when(dateTimeProviderService.getTodaysDate()).thenReturn(LocalDate.of(2001,1,1));
	when(currencyDefinitionService.getOneByCodeName("USD")).thenReturn(USD_DEFINITION);
	when(currencyDefinitionService.getOneByCodeName("JPY")).thenReturn(JPY_DEFINITION);
	when(extractedCurrencyRate.getCurrencyRate(USD_DEFINITION, USD_ENTRY)).thenReturn(USD_CURRENCY_RATE);
	when(extractedCurrencyRate.getCurrencyRate(JPY_DEFINITION, JPY_ENTRY)).thenReturn(JPY_CURRENCY_RATE);

	// When
	List<CurrencyRate> currencyRates = dataSetUpdateEcb.getNewCurrencyRates();

	// Then
	assertThat(currencyRates).isNotNull();
	assertThat(currencyRates).containsOnly(USD_CURRENCY_RATE, JPY_CURRENCY_RATE);
    }

    private InputStream getTestInputStream() throws IOException {
	return FileUtils.openInputStream(new File("src/test/resources/testDataSetUpdate_ecb.xml"));
    }

    private Map.Entry<String, String> createEntry(String date, String rate) {
	return Collections.singletonMap(date, rate).entrySet().iterator().next();
    }

    private CurrencyDefinition createCurrencyDefinition(String codeName, int precision) {
	CurrencyDefinition currencyDefinition = new CurrencyDefinition();
	currencyDefinition.setCodeName(codeName);
	currencyDefinition.setPrecision(precision);
	return currencyDefinition;
    }

    private CurrencyRate createCurrencyRate(CurrencyDefinition currencyDefinition, Map.Entry<String, String> entry) {
	CurrencyRate currencyRate = new CurrencyRate();
	String date = entry.getKey();
	String rate = entry.getValue();
	currencyRate.setExchangeRate(new BigDecimal(rate));
	currencyRate.setCurrency(currencyDefinition);
	currencyRate.setDate(LocalDate.parse(date));
	return currencyRate;
    }

}