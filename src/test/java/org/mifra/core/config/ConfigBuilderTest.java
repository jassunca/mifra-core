package org.mifra.core.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ConfigBuilderTest {

    @Test
    public void testCompleteYAML() throws IOException {

        MifraConfig result = new ConfigBuilder()
                .setConfigFilePath("src/test/resources/testProperties.yaml")
                .build();


        Assertions.assertEquals(10, result.getCore().getSagaTimeout());
        Assertions.assertEquals(8070,result.getServer().getHttp().getEndpointPort());
    }

    @Test
    public void testPartialYAML() throws IOException {

        MifraConfig result = new ConfigBuilder()
                .setConfigFilePath("src/test/resources/testPropertiesPartial.yaml")
                .build();

        Assertions.assertEquals(10, result.getCore().getSagaTimeout());
        Assertions.assertEquals(8080,result.getServer().getHttp().getEndpointPort());
    }

    @Test
    public void testDefaultConfigs() throws IOException {

        MifraConfig result = new ConfigBuilder()
                .build();

        Assertions.assertEquals(5, result.getCore().getSagaTimeout());
        Assertions.assertEquals(8080,result.getServer().getHttp().getEndpointPort());
    }
}
