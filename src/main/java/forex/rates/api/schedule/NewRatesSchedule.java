package forex.rates.api.schedule;

import forex.rates.api.configuration.DataSetContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NewRatesSchedule {

    private final TaskScheduler scheduler;
    private final DataSetContext dataSetContext;
    private final RunnableTask newDailyRatesTask;

    @Autowired
    public NewRatesSchedule(TaskScheduler scheduler, DataSetContext dataSetContext, RunnableTask newDailyRatesTask) {
	this.scheduler = scheduler;
	this.dataSetContext = dataSetContext;
	this.newDailyRatesTask = newDailyRatesTask;
    }

    @Async
    public void scheduleUpdate() {
	TimeZone timeZone = TimeZone.getTimeZone(dataSetContext.getScheduleNewRatesTimeZone());
	String cronPattern = dataSetContext.getScheduleNewRatesCronPatternTrigger();
	ScheduledFuture<?> schedule = scheduler.schedule(newDailyRatesTask, new CronTrigger(cronPattern, timeZone));
	log.info("Time until next new rates update: {} seconds", schedule.getDelay(TimeUnit.SECONDS));
    }

}
