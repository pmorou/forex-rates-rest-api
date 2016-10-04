package forex.rates.api.autostart;

public interface DataSetContext {

    String getBaseCurrency();

    String getUpdateUrl();

    String getSourceUrl();

    String getSourceLocalCopyPath();

    String getSourceLocalCopyPrefix();

    String getSourceLocalCopyExtension();

}
