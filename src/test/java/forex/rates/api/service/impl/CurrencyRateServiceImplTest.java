package forex.rates.api.service.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRateRepository;
import forex.rates.api.service.CurrencyRateService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;

public class CurrencyRateServiceImplTest {

    private CurrencyRateService currencyRateService;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Before
    public void before() {
	MockitoAnnotations.initMocks(this);
	currencyRateService = new CurrencyRateServiceImpl(currencyRateRepository);
    }

    @Test
    public void shouldVerifyFindAllCurrencyRatesByDateAndCurrencyInCall() throws Exception {
	// When
	currencyRateService.getAllByDateAndCurrencyIn(any(LocalDate.class), anyListOf(CurrencyDefinition.class));

	// Then
	verify(currencyRateRepository).findAllByDateAndCurrencyIn(any(LocalDate.class), anyListOf(CurrencyDefinition.class));
    }

    @Test
    public void shouldVerifyFindOneCurrencyDefinitionByDateAndCurrencyCall() throws Exception {
	// When
	currencyRateService.getOneByDateAndCurrency(any(LocalDate.class), any(CurrencyDefinition.class));

	// Then
	verify(currencyRateRepository).findOneByDateAndCurrency(any(LocalDate.class), any(CurrencyDefinition.class));
    }

    @Test
    public void shouldVerifyCurrencyDefinitionSaveCall() throws Exception {
	// When
	currencyRateService.save(any(CurrencyRate.class));

	// Then
	verify(currencyRateRepository).save(any(CurrencyRate.class));
    }

    @Test
    public void shouldVerifyCurrencyDefinitionsSaveCall() throws Exception {
	// When
	currencyRateService.save(anyListOf(CurrencyRate.class));

	// Then
	verify(currencyRateRepository).save(anyListOf(CurrencyRate.class));
    }

}
