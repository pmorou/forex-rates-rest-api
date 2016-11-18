package forex.rates.api.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class Transaction {

    @Getter(onMethod = @__(@JsonAnyGetter))
    private final Map<String, BigDecimal> to = new HashMap<>();

    public void addAmount(String currency, BigDecimal amount) {
	to.put(currency, amount);
    }

}
