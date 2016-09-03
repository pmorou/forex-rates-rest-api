package forex.rates.api.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ExchangeRatesRequest {

    private final String base;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<String> currencies;

    public ExchangeRatesRequest(String base, String date, String[] currencies) {
        this.base = base;
        LocalDate startDate = LocalDate.parse(date);
        this.startDate = startDate;
        this.endDate = startDate;
        this.currencies = Arrays.asList(currencies);
    }

    public ExchangeRatesRequest(String base, String startDate, String endDate, String[] currencies) {
        this.base = base;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
        this.currencies = Arrays.asList(currencies);
    }

    public List<LocalDate> getDateRange() {
        long daysNumber = startDate.until(endDate, ChronoUnit.DAYS);
        List<LocalDate> range = new ArrayList<>((int) daysNumber);
        for (int i = 0; i <= daysNumber; i++) {
            range.add(startDate.plusDays(i));
        }
        return range;
    }

}
