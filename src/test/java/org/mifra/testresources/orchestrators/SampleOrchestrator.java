package org.mifra.testresources.orchestrators;

import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.orchestrator.Orchestrator;
import org.mifra.core.components.domain.messages.SagaStepHistoryMap;
import org.mifra.core.components.stepmaps.SagaStepMap;
import org.mifra.testresources.messages.SampleExternalReplyBody;
import org.mifra.testresources.messages.SampleExternalRequestBody;
import org.mifra.testresources.messages.SampleParticipantBPayload;
import org.mifra.testresources.messages.SampleStartingPayload;

public class SampleOrchestrator implements Orchestrator<SampleExternalRequestBody, SampleExternalReplyBody> {
    @Override
    public SagaStepMessage<?> handleOncomingRequest(ExternalRequest<SampleExternalRequestBody> request) {

        SampleStartingPayload result = new SampleStartingPayload();
        result.setStartingValue(request.payload().getStartingValue());
        return new SagaStepMessage<>(result);

    }

    @Override
    public ExternalReply<SampleExternalReplyBody> prepareExternalReply(SagaStepHistoryMap historyMap) {

        SampleParticipantBPayload resultingStep = historyMap.getStep(SampleParticipantBPayload.class).getPayload();

        SampleExternalReplyBody result = new SampleExternalReplyBody();
        result.setResultingCalculation(resultingStep.getResultingCalculation());

        return new ExternalReply<>(result);
    }

    @Override
    public void defineStepMap(SagaStepMap stepMap) {

        stepMap.setInitialStep("FIRSTSTEP");
        stepMap.configureStep("FIRSTSTEP")
                .addStepResult("SUM","SECONDSTEP");

    }
}
