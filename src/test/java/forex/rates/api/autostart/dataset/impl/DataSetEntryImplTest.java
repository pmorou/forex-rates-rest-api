package forex.rates.api.autostart.dataset.impl;

import forex.rates.api.autostart.dataset.DataSetEntry;
import forex.rates.api.autostart.dataset.ExtractedCurrencyDefinition;
import forex.rates.api.autostart.dataset.ExtractedCurrencyRate;
import forex.rates.api.model.entity.CurrencyDefinition;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class DataSetEntryImplTest {

    private @Mock ExtractedCurrencyRate extractedCurrencyRate;
    private @Mock ExtractedCurrencyDefinition extractedCurrencyDefinition;

    private DataSetEntry dataSetEntry;

    @Before
    public void before() throws Exception {
	MockitoAnnotations.initMocks(this);
	dataSetEntry = new DataSetEntryImpl(extractedCurrencyRate, extractedCurrencyDefinition);
    }

    @Test
    @Parameters
    public void shouldThrowNullPointerException(Throwable actualResult) throws Exception {
	// Then
	assertThat(actualResult).isInstanceOf(NullPointerException.class);
    }

    public Object[] parametersForShouldThrowNullPointerException() {
	return new Object[]{
		new Object[]{catchThrowable(() -> dataSetEntry.addAttribute("UNIT", "JPY"))},
		new Object[]{catchThrowable(() -> dataSetEntry.addRate("2001-01-01", "120.32"))},
		new Object[]{catchThrowable(() -> dataSetEntry.saveCurrency())}
	};
    }

    @Test
    public void shouldCallExtractedObjectsWithGivenParameters() throws Exception {
	// Given
	dataSetEntry.newCurrency();
	dataSetEntry.addAttribute("UNIT", "JPY");
	dataSetEntry.addRate("2001-01-01", "120.32");

	// When
	dataSetEntry.saveCurrency();

	// Then
	verify(extractedCurrencyDefinition).getCurrencyDefinition(Collections.singletonMap("UNIT", "JPY"));
	verify(extractedCurrencyRate).getCurrencyRate(null, createEntry("2001-01-01", "120.32"));
    }

    @Test
    public void shouldCatchIllegalArgumentException() throws Exception {
	// Given
	dataSetEntry.newCurrency();
	dataSetEntry.addRate("throws", "IAE");
	when(extractedCurrencyDefinition.getCurrencyDefinition(any())).thenReturn(createCurrencyDefinition("JPY"));
	when(extractedCurrencyRate.getCurrencyRate(any(), eq(createEntry("throws", "IAE")))).thenThrow(IllegalArgumentException.class);

	// When
	dataSetEntry.saveCurrency();
    }

    private CurrencyDefinition createCurrencyDefinition(String codeName) {
	CurrencyDefinition currencyDefinition = new CurrencyDefinition();
	currencyDefinition.setCodeName(codeName);
	return currencyDefinition;
    }

    private Map.Entry<String, String> createEntry(String key, String value) {
	return Collections.singletonMap(key, value).entrySet().iterator().next();
    }

}