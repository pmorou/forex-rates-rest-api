package forex.rates.api.model.exchange.collector;

import forex.rates.api.model.exchange.AddableContainer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class AddableContainerCollector<T extends AddableContainer>
	implements Collector<Map.Entry<String, BigDecimal>, T, T> {

    private static final Characteristics COLLECTOR_CHARACTERISTIC = Characteristics.IDENTITY_FINISH;

    private final Supplier<T> supplier;

    public AddableContainerCollector(Supplier<T> supplier) {
	this.supplier = supplier;
    }

    @Override
    public Supplier<T> supplier() {
	return supplier;
    }

    @Override
    public BiConsumer<T, Map.Entry<String, BigDecimal>> accumulator() {
	return (rates, entry) -> rates.add(entry.getKey(), entry.getValue());
    }

    @Override
    public BinaryOperator<T> combiner() {
	return (rates1, rates2) -> { rates1.add(rates2); return rates1; };
    }

    @Override
    public Function<T, T> finisher() {
	throw new UnsupportedOperationException("Collector characteristic: " + COLLECTOR_CHARACTERISTIC);
    }

    @Override
    public Set<Characteristics> characteristics() {
	return Collections.singleton(COLLECTOR_CHARACTERISTIC);
    }

}
