package forex.rates.api.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class Rates {

    private final Map<String, BigDecimal> rates = new HashMap<>();

    public void addRate(String currency, BigDecimal rate) {
	rates.put(currency, rate);
    }

}
