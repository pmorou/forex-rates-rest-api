package forex.rates.api.service.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import forex.rates.api.service.CurrencyDefinitionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

public class CurrencyDefinitionServiceImplTest {

    private CurrencyDefinitionService currencyDefinitionService;

    @Mock
    private CurrencyDefinitionRepository currencyDefinitionRepository;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	currencyDefinitionService = new CurrencyDefinitionServiceImpl(currencyDefinitionRepository);
    }

    @Test
    public void shouldVerifyFindAllCurrencyDefinitionsCall() throws Exception {
	// When
	currencyDefinitionService.getAll();

	// Then
	verify(currencyDefinitionRepository).findAll();
    }

    @Test
    public void shouldVerifyFindAllCurrencyDefinitionsByCodeNameInCall() throws Exception {
	// When
	currencyDefinitionService.getAllByCodeNameIn(anyListOf(String.class));

	// Then
	verify(currencyDefinitionRepository).findAllByCodeNameIn(anyListOf(String.class));
    }

    @Test
    public void shouldVerifyFindOneCurrencyDefinitionByCodeNameCall() throws Exception {
	// When
	currencyDefinitionService.getOneByCodeName(anyString());

	// Then
	verify(currencyDefinitionRepository).findOneByCodeName(anyString());
    }

    @Test
    public void shouldVerifyCurrencyDefinitionSaveCall() throws Exception {
	// When
	currencyDefinitionService.save(any(CurrencyDefinition.class));

	// Then
	verify(currencyDefinitionRepository).save(any(CurrencyDefinition.class));
    }

    @Test
    public void shouldVerifyCurrencyDefinitionsSaveCall() throws Exception {
	// When
	currencyDefinitionService.save(anyListOf(CurrencyDefinition.class));

	// Then
	verify(currencyDefinitionRepository).save(anyListOf(CurrencyDefinition.class));
    }

}
