package forex.rates.api.model.exchange;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class ExchangeTransactions {

    @Getter(AccessLevel.PRIVATE)
    private final ExchangeRates exchangeRates;
    private final BigDecimal amount;

    public String getFrom() {
	return exchangeRates.getBase();
    }

    public LocalDate getStartDate() {
	return exchangeRates.getStartDate();
    }

    public LocalDate getEndDate() {
	return exchangeRates.getEndDate();
    }

    public Map<LocalDate, Transactions> getTo() {
	return exchangeRates.getRatesByDate().entrySet().stream()
		.collect(Collectors.toMap(
			getLocalDate(), getTransactions())
		);
    }

    private Function<Map.Entry<LocalDate, Rates>, LocalDate> getLocalDate() {
	return e -> e.getKey();
    }

    private Function<Map.Entry<LocalDate, Rates>, Transactions> getTransactions() {
	return e -> toTransactions(e.getValue());
    }

    private Transactions toTransactions(Rates dailyRates) {
	return dailyRates.getRates().entrySet().stream()
		.collect(Transactions::new,
			accumulator(),
			combiner());
    }

    private BiConsumer<Transactions, Map.Entry<String, BigDecimal>> accumulator() {
	return (t, e) -> t.add(e.getKey(), multiplyWithAmount(e.getValue()));
    }

    private BigDecimal multiplyWithAmount(BigDecimal rate) {
	return rate.multiply(this.amount, new MathContext(3, RoundingMode.HALF_UP));
    }

    private BiConsumer<Transactions, Transactions> combiner() {
	return (t1, t2) -> t1.add(t2);
    }

}
