package forex.rates.api.dataset.impl.ecb;

import forex.rates.api.dataset.CompleteDataSet;
import forex.rates.api.dataset.DataSetEntry;
import forex.rates.api.dataset.DataSetSourceLocalCopy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.mockito.Mockito.when;

public class DataSetSourceEcbImplTest {

    @Mock(extraInterfaces = CompleteDataSet.class)
    private DataSetEntry dataSetEntry;

    @Mock
    private DataSetSourceLocalCopy dataSetSourceLocaLCopy;

    @Before
    public void before() {
       MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCallMethodsWithUsdAttributesAndRates() throws Exception {
       // Given
       File testDataSetFile = new File("src/test/resources/dataset/testDataSetSource_ecb.xml");
       when(dataSetSourceLocaLCopy.getFile()).thenReturn(testDataSetFile);
       DataSetSourceEcbImpl dataSetSourceEcb = new DataSetSourceEcbImpl(dataSetEntry, dataSetSourceLocaLCopy);

       // When
       dataSetSourceEcb.getCompleteDataSet();

       // Then
       InOrder inOrder = Mockito.inOrder(dataSetEntry);
       inOrder.verify(dataSetEntry).newCurrency();
       inOrder.verify(dataSetEntry).addAttribute("UNIT", "USD");
       inOrder.verify(dataSetEntry).addAttribute("DECIMALS", "4");
       inOrder.verify(dataSetEntry).addRate("2016-05-02", "1.1493");
       inOrder.verify(dataSetEntry).addRate("2016-05-03", "1.1569");
       inOrder.verify(dataSetEntry).saveCurrency();
    }

}