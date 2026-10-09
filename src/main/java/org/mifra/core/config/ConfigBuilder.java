package org.mifra.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

/**
 * 
 */
public class ConfigBuilder {

    String configFilePath;

    public ConfigBuilder setConfigFilePath(String filePath) {
        this.configFilePath = filePath;
        return this;
    }

    public MifraConfig build() throws IOException {
        if (configFilePath == null || configFilePath.isBlank()) {
            return buildConfig();
        } else {
            return buildConfig(configFilePath);
        }
    }

    private MifraConfig buildConfig(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        MifraConfig mifraConfig = mapper.readValue(new File(path), MifraConfig.class);

        return mifraConfig;
    }

    private MifraConfig buildConfig() {
        return new MifraConfig();
    }
}
