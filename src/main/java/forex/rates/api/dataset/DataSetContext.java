package forex.rates.api.dataset;

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
