package forex.rates.api.service.impl;

import forex.rates.api.configuration.DataSetContext;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.service.CurrencyDefinitionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AvailableCurrenciesServiceImplTest {

    private final CurrencyDefinition USD_DEFINITION = createCurrencyDefinition("USD", 4);
    private final CurrencyDefinition PLN_DEFINITION = createCurrencyDefinition("PLN", 4);

    private @Mock CurrencyDefinitionService currencyDefinitionService;
    private @Mock DataSetContext dataSetContext;

    private AvailableCurrenciesServiceImpl availableCurrenciesService;

    @Before
    public void before() throws Exception {
	MockitoAnnotations.initMocks(this);
	availableCurrenciesService = new AvailableCurrenciesServiceImpl(currencyDefinitionService, dataSetContext);
    }

    @Test
    public void shouldReturnListOfUsdAndPln() throws Exception {
	// Given
	when(currencyDefinitionService.getAll()).thenReturn(Arrays.asList(USD_DEFINITION, PLN_DEFINITION));
	when(dataSetContext.getBaseCurrency()).thenReturn("EUR");

	// When
	List<String> actualAvailableCurrencies = availableCurrenciesService.getCodeList();

	// Then
	assertThat(actualAvailableCurrencies).containsOnly("USD", "PLN", "EUR");
    }

    private CurrencyDefinition createCurrencyDefinition(String codeName, int precision) {
	CurrencyDefinition currencyDefinition = new CurrencyDefinition();
	currencyDefinition.setCodeName(codeName);
	currencyDefinition.setPrecision(precision);
	return currencyDefinition;
    }

}