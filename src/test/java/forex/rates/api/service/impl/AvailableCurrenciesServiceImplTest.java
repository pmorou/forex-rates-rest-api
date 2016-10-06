package forex.rates.api.service.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.repository.CurrencyDefinitionRepository;
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

    private @Mock CurrencyDefinitionRepository currencyDefinitionRepository;

    private AvailableCurrenciesServiceImpl availableCurrenciesService;

    @Before
    public void before() throws Exception {
	MockitoAnnotations.initMocks(this);
	availableCurrenciesService = new AvailableCurrenciesServiceImpl(currencyDefinitionRepository);
    }

    @Test
    public void shouldReturnListOfUsdAndPln() throws Exception {
	// Given
	when(currencyDefinitionRepository.findAll()).thenReturn(Arrays.asList(USD_DEFINITION, PLN_DEFINITION));

	// When
	List<String> actualAvailableCurrencies = availableCurrenciesService.getList();

	// Then
	assertThat(actualAvailableCurrencies).containsOnlyOnce("USD", "PLN");
    }

    private CurrencyDefinition createCurrencyDefinition(String codeName, int precision) {
	CurrencyDefinition currencyDefinition = new CurrencyDefinition();
	currencyDefinition.setCodeName(codeName);
	currencyDefinition.setPrecision(precision);
	return currencyDefinition;
    }

}