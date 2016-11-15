package forex.rates.api.model.request;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class ExchangeRatesRequest {

    private final String base;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<String> currencies;

    public ExchangeRatesRequest(String base, String date, String[] currencies) {
        this.base = base;
        this.startDate = this.endDate = LocalDate.parse(date);
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
        return IntStream.rangeClosed(0, Math.toIntExact(daysNumber))
                .mapToObj(startDate::plusDays)
                .filter(skipSaturdaysAndSundays())
                .collect(Collectors.toList());
    }

    private Predicate<LocalDate> skipSaturdaysAndSundays() {
        return date -> isDayOfWeekEquals(date, DayOfWeek.SATURDAY) &&
                isDayOfWeekEquals(date, DayOfWeek.SUNDAY);
    }

    private boolean isDayOfWeekEquals(LocalDate date, DayOfWeek dayOfWeek) {
        return !date.getDayOfWeek().equals(dayOfWeek);
    }

}
