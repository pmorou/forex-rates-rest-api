package forex.rates.api.model;

import org.junit.Test;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeRatesRequestTest {

    @Test
    public void shouldCreateRequestWithSameStartAndEndDate() throws Exception {
	// When
	ExchangeRatesRequest request = new ExchangeRatesRequest("EUR", "2001-01-01", new String[]{"USD", "PLN"});

	// Then
	assertThat(request.getBase()).isEqualTo("EUR");
	assertThat(request.getCurrencies()).containsExactly("USD", "PLN");
	assertThat(request.getStartDate()).isEqualTo(parse("2001-01-01"));
	assertThat(request.getEndDate()).isEqualTo(parse("2001-01-01"));
	assertThat(request.getDateRange()).containsExactly(parse("2001-01-01"));
    }

    @Test
    public void shouldCreateRequestWithDifferentStartAndEndDate() throws Exception {
	// When
	ExchangeRatesRequest request = new ExchangeRatesRequest("EUR", "2001-01-01", "2001-01-02", new String[]{"USD", "PLN"});

	// Then
	assertThat(request.getBase()).isEqualTo("EUR");
	assertThat(request.getCurrencies()).containsExactly("USD", "PLN");
	assertThat(request.getStartDate()).isEqualTo(parse("2001-01-01"));
	assertThat(request.getEndDate()).isEqualTo(parse("2001-01-02"));
	assertThat(request.getDateRange()).containsExactly(parse("2001-01-01"), parse("2001-01-02"));
    }

}