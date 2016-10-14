package forex.rates.api.dataset;

public interface DataSetEntry {

    void newCurrency();

    void addAttribute(String key, String value);

    void addRate(String date, String value);

    void saveCurrency();

}
