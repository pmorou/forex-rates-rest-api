package forex.rates.api.autostart.dataset;

import java.io.File;
import java.io.IOException;

public interface DataSetSourceLocalCopy {

    File getFile() throws IOException;

}
