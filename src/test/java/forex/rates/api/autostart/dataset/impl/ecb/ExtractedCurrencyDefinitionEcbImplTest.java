package forex.rates.api.autostart.dataset.impl.ecb;

import forex.rates.api.autostart.dataset.impl.ecb.ExtractedCurrencyDefinitionEcbImpl;
import forex.rates.api.model.entity.CurrencyDefinition;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractedCurrencyDefinitionEcbImplTest {

    @Test
    public void shouldReturnUsdWithPrecisionUpTo4Digits() throws Exception {
	// Given
	Map<String, String> attributes = new HashMap<>();
	attributes.put("UNIT","USD");
	attributes.put("DECIMALS","4");

	// When
	CurrencyDefinition currencyDefinition =
		new ExtractedCurrencyDefinitionEcbImpl().getCurrencyDefinition(attributes);

	// Then
	assertThat(currencyDefinition.getCodeName()).isEqualTo("USD");
	assertThat(currencyDefinition.getPrecision()).isEqualTo(4);
    }

    @Test
    public void shouldReturnJpyWithPrecisionUpTo2Digits() throws Exception {
	// Given
	Map<String, String> attributes = new HashMap<>();
	attributes.put("UNIT","JPY");
	attributes.put("DECIMALS","2");

	// When
	CurrencyDefinition currencyDefinition =
		new ExtractedCurrencyDefinitionEcbImpl().getCurrencyDefinition(attributes);

	// Then
	assertThat(currencyDefinition.getCodeName()).isEqualTo("JPY");
	assertThat(currencyDefinition.getPrecision()).isEqualTo(2);
    }

}