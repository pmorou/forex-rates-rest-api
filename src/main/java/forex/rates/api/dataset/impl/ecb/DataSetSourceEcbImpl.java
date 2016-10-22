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
               private static final String GENERIC_SERIES_TAG = "generic:Series";
               private static final String GENERIC_ATTRIBUTES_TAG = "generic:Attributes";
               private static final String GENERIC_VALUE_TAG = "generic:Value";
               private static final String GENERIC_OBS_TAG = "generic:Obs";
               private static final String GENERIC_OBS_DIMENSION_TAG = "generic:ObsDimension";
               private static final String GENERIC_OBS_VALUE_TAG = "generic:ObsValue";

               private Attributes attributesHolder;
               private boolean insideSeries;
               private boolean insideAttributes;
               private boolean insideValue;
               private boolean insideObs;
               private boolean insideObsDimension;
               private boolean insideObsValue;
               private String dateHolder;

               public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                   attributesHolder = attributes;

                   if (GENERIC_SERIES_TAG.equalsIgnoreCase(qName)) {
                       dataSetEntry.newCurrency();
                       insideSeries = true;
                   }

                   if (GENERIC_ATTRIBUTES_TAG.equalsIgnoreCase(qName)) {
                       insideAttributes = true;
                   }

                   if (GENERIC_VALUE_TAG.equalsIgnoreCase(qName)) {
                       insideValue = true;
                   }

                   if (GENERIC_OBS_TAG.equalsIgnoreCase(qName)) {
                       insideObs = true;
                   }

                   if (GENERIC_OBS_DIMENSION_TAG.equalsIgnoreCase(qName)) {
                       insideObsDimension = true;
                   }

                   if (GENERIC_OBS_VALUE_TAG.equalsIgnoreCase(qName)) {
                       insideObsValue = true;
                   }
               }

               public void endElement(String uri, String localName, String qName) throws SAXException {
                   if (GENERIC_ATTRIBUTES_TAG.equalsIgnoreCase(qName) & !insideObs) {
                       insideAttributes = false;
                   }

                   if (GENERIC_OBS_TAG.equalsIgnoreCase(qName)) {
                       insideObs = false;
                   }

                   if (GENERIC_SERIES_TAG.equalsIgnoreCase(qName)) {
                       dataSetEntry.saveCurrency();
                       insideSeries = false;
                   }
               }

               public void characters(char ch[], int start, int length) throws SAXException {
                   if (isInsideSeriesAttributesValuesTree()) {
                       dataSetEntry.addAttribute(
                               attributesHolder.getValue(0), attributesHolder.getValue(1));
                   }

                   if (insideObs && insideObsDimension) {
                       dateHolder = attributesHolder.getValue(0);
                       insideObsDimension = false;
                   }

                   if (insideObs && insideObsValue) {
                       dataSetEntry.addRate(dateHolder, attributesHolder.getValue(0));
                       insideObsValue = false;
                   }

                   if (insideValue) {
                       insideValue = false;
                   }
               }

               private boolean isInsideSeriesAttributesValuesTree() {
                   return insideSeries && insideAttributes && insideValue && !insideObs;
               }
           };

           saxParser.parse(dataSetSourceLocalCopy.getFile(), handler);

       } catch (Exception e) {
           e.printStackTrace();
       }

       return (CompleteDataSet) dataSetEntry;
    }

}
