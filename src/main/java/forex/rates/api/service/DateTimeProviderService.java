package forex.rates.api.service;

import java.time.LocalDate;

public interface DateTimeProviderService {

    long getCurrentTimestamp();

    String getTodaysDateAsString();

    LocalDate getTodaysDate();

}
