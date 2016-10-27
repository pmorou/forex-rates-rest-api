package forex.rates.api.autostart;

import forex.rates.api.schedule.NewRatesSchedule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ScheduleUpdateAutostartImpl implements Autostart {

    private final NewRatesSchedule newRatesSchedule;

    public ScheduleUpdateAutostartImpl(NewRatesSchedule newRatesSchedule) {
        this.newRatesSchedule = newRatesSchedule;
    }

    @Override
    @PostConstruct
    public void start() {
	newRatesSchedule.scheduleUpdate();
    }

}
