package forex.rates.api.configuration;

public interface DataSetContext {

    String getBaseCurrency();

    String getUpdateUrl();

    String getSourceUrl();

    String getSourceLocalCopyPath();

    String getSourceLocalCopyPrefix();

    String getSourceLocalCopyExtension();

    String getScheduleNewRatesTimeZone();

    String getScheduleNewRatesCronPatternTrigger();

}
