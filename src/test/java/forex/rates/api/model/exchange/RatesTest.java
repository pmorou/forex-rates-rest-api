package forex.rates.api.model.exchange;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RatesTest {

    @Test
    public void shouldAddSingleRate() throws Exception {
	// Given
	Rates rates = new Rates();
	int sizeBefore = rates.getRates().size();

	// When
	rates.add("USD", BigDecimal.ONE);

	// Then
	Map<String, BigDecimal> result = rates.getRates();
	assertThat(result).hasSize(sizeBefore + 1);
	assertThat(result.get("USD")).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void shouldAddAnotherRatesObject() throws Exception {
	// Given
	Rates rates = new Rates();
	int sizeBefore = rates.getRates().size();
	Rates ratesToAdd = new Rates();
	ratesToAdd.add("USD", BigDecimal.ONE);
	ratesToAdd.add("EUR", BigDecimal.TEN);

	// When
	rates.add(ratesToAdd);

	// Then
	Map<String, BigDecimal> result = rates.getRates();
	assertThat(result).hasSize(sizeBefore + 2);
	assertThat(result.get("USD")).isEqualTo(BigDecimal.ONE);
	assertThat(result.get("EUR")).isEqualTo(BigDecimal.TEN);
    }

}
