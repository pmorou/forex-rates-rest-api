package forex.rates.api.autostart;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class DataSetContextImpl implements DataSetContext {

    @Value("${dataSet.baseCurrency}")
    private String baseCurrency;

    @Value("${dataSet.source.url}")
    private String sourceUrl;

    @Value("${dataSet.update.url}")
    private String updateUrl;

    @Value("${dataSet.source.localCopy.path}")
    private String sourceLocalCopyPath;

    @Value("${dataSet.source.localCopy.prefix}")
    private String sourceLocalCopyPrefix;

    @Value("${dataSet.source.localCopy.extension}")
    private String sourceLocalCopyExtension;

}
