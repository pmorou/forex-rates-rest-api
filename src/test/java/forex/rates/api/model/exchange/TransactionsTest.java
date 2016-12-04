package forex.rates.api.model.exchange;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionsTest {

    @Test
    public void shouldAddSingleTransactionsRecord() throws Exception {
	// Given
	Transactions transactions = new Transactions();
	int sizeBefore = transactions.getTo().size();

	// When
	transactions.add("USD", BigDecimal.ONE);

	// Then
	Map<String, BigDecimal> result = transactions.getTo();
	assertThat(result).hasSize(sizeBefore + 1);
	assertThat(result.get("USD")).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void shouldAddAnotherTransactionsObject() throws Exception {
	// Given
	Transactions transactions = new Transactions();
	int sizeBefore = transactions.getTo().size();
	Transactions transactionsToAdd = new Transactions();
	transactionsToAdd.add("USD", BigDecimal.ONE);
	transactionsToAdd.add("EUR", BigDecimal.TEN);

	// When
	transactions.add(transactionsToAdd);

	// Then
	Map<String, BigDecimal> result = transactions.getTo();
	assertThat(result).hasSize(sizeBefore + 2);
	assertThat(result.get("USD")).isEqualTo(BigDecimal.ONE);
	assertThat(result.get("EUR")).isEqualTo(BigDecimal.TEN);
    }

}
