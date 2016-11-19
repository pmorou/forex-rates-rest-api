package forex.rates.api.model.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
public class ExchangeRates {

    private LocalDate startDate;
    private LocalDate endDate;
    private String base;
    private Map<LocalDate, Rates> ratesByDate;

}
