package forex.rates.api.service;

import java.time.LocalDate;

public interface DateTimeProviderService {

    /**
     * @return UTC time in milliseconds
     */
    long getCurrentTimestamp();

    /**
     * @return {@link #getTodaysDate()} as String
     */
    String getTodaysDateAsString();

    /**
     * @return UTC time in YYYY-MM-DD format
     */
    LocalDate getTodaysDate();

}
