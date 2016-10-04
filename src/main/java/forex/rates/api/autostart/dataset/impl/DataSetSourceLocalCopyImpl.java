package forex.rates.api.autostart.dataset.impl;

import forex.rates.api.autostart.DataSetContext;
import forex.rates.api.autostart.dataset.DataSetSourceLocalCopy;
import forex.rates.api.http.client.HttpClient;
import forex.rates.api.service.DateTimeProviderService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DataSetSourceLocalCopyImpl implements DataSetSourceLocalCopy {

    private final DataSetContext dataSetContext;
    private final HttpClient httpClient;
    private final DateTimeProviderService dateTimeProviderService;

    private File dataSetSourceCopy;

    public DataSetSourceLocalCopyImpl(DataSetContext dataSetContext, HttpClient httpClient, DateTimeProviderService dateTimeProviderService) {
	this.dataSetContext = dataSetContext;
	this.httpClient = httpClient;
	this.dateTimeProviderService = dateTimeProviderService;
    }

    private File copyFromRemoteSource() throws IOException {
	InputStream is = httpClient.getInputStream(dataSetContext.getSourceUrl());
	File file = new File(getPathWithFileName());
	IOUtils.copy(is, FileUtils.openOutputStream(file));
	return this.dataSetSourceCopy = file;
    }

    private String getPathWithFileName() {
	return dataSetContext.getSourceLocalCopyPath() + "/" + dataSetContext.getSourceLocalCopyPrefix() + "_"
		+ dateTimeProviderService.getCurrentTimestamp() + "." + dataSetContext.getSourceLocalCopyExtension();
    }

    @Override
    public File getFile() throws IOException {
	return dataSetSourceCopy == null ? copyFromRemoteSource() : dataSetSourceCopy;
    }

}
