package forex.rates.api.dataset.impl.ecb;

import forex.rates.api.dataset.CompleteDataSet;
import forex.rates.api.dataset.DataSetEntry;
import forex.rates.api.dataset.DataSetSource;
import forex.rates.api.dataset.DataSetSourceLocalCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Profile("european-central-bank")
@Component
public class DataSetSourceEcbImpl implements DataSetSource {

    private final DataSetEntry dataSetEntry;
    private final DataSetSourceLocalCopy dataSetSourceLocalCopy;

    @Autowired
    public DataSetSourceEcbImpl(DataSetEntry dataSetEntry, DataSetSourceLocalCopy dataSetSourceLocalCopy) {
       this.dataSetEntry = dataSetEntry;
       this.dataSetSourceLocalCopy = dataSetSourceLocalCopy;
    }

    @Override
    public CompleteDataSet getCompleteDataSet() {
       try {
           SAXParserFactory factory = SAXParserFactory.newInstance();
           SAXParser saxParser = factory.newSAXParser();

           DefaultHandler handler = new DefaultHandler() {
               private static final String GROUP_TAG = "Group";
               private static final String SERIES_TAG = "Series";
               private static final String OBS_TAG = "Obs";

               public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                   if (GROUP_TAG.equalsIgnoreCase(qName)) {
                       dataSetEntry.newCurrency();
                       Map<String, String> attributesMap = mapAttributes(attributes);
                       attributesMap.entrySet().forEach(addAsDataSetEntryAttribute());
                   }

                   if (OBS_TAG.equalsIgnoreCase(qName)) {
                       Map<String, String> attributesMap = mapAttributes(attributes);
                       dataSetEntry.addRate(attributesMap.get("TIME_PERIOD"), attributesMap.get("OBS_VALUE"));
                   }
               }

               public void endElement(String uri, String localName, String qName) throws SAXException {
                   if (SERIES_TAG.equalsIgnoreCase(qName)) {
                       dataSetEntry.saveCurrency();
                   }
               }
           };

           saxParser.parse(dataSetSourceLocalCopy.getFile(), handler);

       } catch (Exception e) {
           e.printStackTrace();
       }

       return (CompleteDataSet) dataSetEntry;
    }

    private Consumer<Map.Entry<String, String>> addAsDataSetEntryAttribute() {
        return entry -> dataSetEntry.addAttribute(entry.getKey(), entry.getValue());
    }

    private Map<String, String> mapAttributes(Attributes attributes) {
        return IntStream.range(0, attributes.getLength())
                .boxed()
                .collect(toMap(attributes::getLocalName, attributes::getValue));
    }

}
