package forex.rates.api.dataset.impl.ecb;

import forex.rates.api.configuration.DataSetContext;
import forex.rates.api.dataset.DataSetUpdate;
import forex.rates.api.dataset.ExtractedCurrencyRate;
import forex.rates.api.http.client.HttpClient;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import forex.rates.api.service.DateTimeProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class DataSetUpdateEcbImpl implements DataSetUpdate {

    private final HttpClient httpClient;
    private final DataSetContext dataSetContext;
    private final ExtractedCurrencyRate extractedCurrencyRate;
    private final CurrencyDefinitionRepository currencyDefinitionRepository;
    private final DateTimeProviderService dateTimeProviderService;

    public DataSetUpdateEcbImpl(HttpClient httpClient, DataSetContext dataSetContext, ExtractedCurrencyRate extractedCurrencyRate,
				CurrencyDefinitionRepository currencyDefinitionRepository, DateTimeProviderService dateTimeProviderService) {
	this.httpClient = httpClient;
	this.dataSetContext = dataSetContext;
	this.extractedCurrencyRate = extractedCurrencyRate;
	this.currencyDefinitionRepository = currencyDefinitionRepository;
	this.dateTimeProviderService = dateTimeProviderService;
    }

    @Override
    public List<CurrencyRate> getNewCurrencyRates() {
	List<CurrencyRate> currencyRates = new ArrayList<>();
	LocalDate ratesDate = dateTimeProviderService.getTodaysDate();

	try {
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    SAXParser saxParser = factory.newSAXParser();

	    DefaultHandler handler = new DefaultHandler() {
		private static final String CUBE_TAG = "Cube";

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		    if (isInsideCubeTag(qName)) {
			Map<String, String> attributesMap = mapAttributes(attributes);
			String currency = attributesMap.get("currency");
			if (currency != null) {
			    CurrencyDefinition currencyDefinition = currencyDefinitionRepository.findOneByCodeName(currency);
			    Map.Entry<String, String> entry = createEntry(attributesMap, ratesDate);
			    CurrencyRate currencyRate = extractedCurrencyRate.getCurrencyRate(currencyDefinition, entry);
			    currencyRates.add(currencyRate);
			}
		    }
		}

		private boolean isInsideCubeTag(String qName) {
		    return CUBE_TAG.equalsIgnoreCase(qName);
		}

	    };

	    try (InputStream inputStream = getInputStream()) {
		saxParser.parse(inputStream, handler);
	    } catch (IOException e) {
		log.warn("Failed to get input stream", e);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return currencyRates;
    }

    private Map.Entry<String, String> createEntry(Map<String, String> attributesMap, LocalDate ratesDate) {
	Map<String, String> rate = Collections.singletonMap(ratesDate.toString(), attributesMap.get("rate"));
	return rate.entrySet().iterator().next();
    }

    private Map<String, String> mapAttributes(Attributes attributes) {
	int numberOfAttributes = attributes.getLength();
	Map<String, String> attributesMap = new HashMap<>(numberOfAttributes);
	for (int i = 0; i < numberOfAttributes; i++) {
	    attributesMap.put(attributes.getLocalName(i), attributes.getValue(i));
	}
	return attributesMap;
    }

    private InputStream getInputStream() throws IOException {
	return httpClient.getInputStream(dataSetContext.getUpdateUrl());
    }

}
