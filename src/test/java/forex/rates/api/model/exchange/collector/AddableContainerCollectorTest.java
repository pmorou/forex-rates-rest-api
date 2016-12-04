package forex.rates.api.model.exchange.collector;

import forex.rates.api.model.exchange.Rates;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;

public class AddableContainerCollectorTest {

    @Test
    public void shouldCollectOneUsdRateWithValueOfOne() throws Exception {
	// Given
	Stream<Map.Entry<String, BigDecimal>> stream =
		Collections.singletonMap("USD", BigDecimal.ONE).entrySet().stream();

	// When
	Rates result = stream.collect(new AddableContainerCollector<>(Rates::new));

	// Then
	Map<String, BigDecimal> rates = result.getRates();
	Assertions.assertThat(rates).hasSize(1);
	Assertions.assertThat(rates.get("USD")).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void shouldThrowUnsupportedOperationExceptionWhenFinisherMethodIsCalled() throws Exception {
	// Given
	AddableContainerCollector<Rates> collector = new AddableContainerCollector<>(Rates::new);

	// When
	ThrowingCallable finisher = () -> collector.finisher();

	// Then
	assertThatThrownBy(finisher)
		.isExactlyInstanceOf(UnsupportedOperationException.class)
		.hasMessage("Collector characteristic: IDENTITY_FINISH");
    }

}
