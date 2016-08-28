package forex.rates.api.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ExchangeRates {

    private LocalDate startDate;
    private LocalDate endDate;
    private String base;
    private Map<LocalDate, Rates> ratesByDate;
    private boolean empty;

}
