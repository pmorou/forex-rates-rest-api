package forex.rates.api.autostart.dataset;

public interface DataSetEntry {

    void newCurrency();

    void addAttribute(String key, String value);

    void addRate(String date, String value);

    void saveCurrency();

}
