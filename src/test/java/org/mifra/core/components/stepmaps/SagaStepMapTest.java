package org.mifra.core.components.stepmaps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SagaStepMapTest {

    @Test
    public void configureStepTest() {

        SagaStepMap underTest = new SagaStepMap();

        underTest.configureStep("TestingStep").addStepResult("SUCCESS","NextStepTest");

        String result = underTest.getStep("TestingStep").getNextStep("SUCCESS");

        Assertions.assertEquals("NextStepTest", result);
    }
}
