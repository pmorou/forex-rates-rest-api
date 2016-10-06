package forex.rates.api.service.impl;

import forex.rates.api.service.DateTimeProviderService;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class DateTimeProviderServiceImpl implements DateTimeProviderService {

    private static final Clock CLOCK = Clock.systemUTC();

    @Override
    public long getCurrentTimestamp() {
	return CLOCK.millis();
    }

    @Override
    public String getTodaysDateAsString() {
	return getTodaysDate().toString();
    }

    @Override
    public LocalDate getTodaysDate() {
	return LocalDate.now(CLOCK);
    }

}
