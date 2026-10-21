package org.mifra.testresources.orchestrators;

import org.mifra.core.api.models.domain.SagaStepHistory;
import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.domain.payloads.SagaStepPayload;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.orchestrator.BaseOrchestrator;
import org.mifra.core.components.stepmaps.SagaStepMap;
import org.mifra.testresources.messages.SampleExternalRequestBody;
import org.mifra.testresources.messages.TimeoutCheckExternalReplyBody;
import org.mifra.testresources.messages.TimeoutCheckStartingPayload;

public class TimeoutCheckOrchestrator  extends BaseOrchestrator<SampleExternalRequestBody, TimeoutCheckExternalReplyBody> {
    @Override
    public SagaStepMessage<? extends SagaStepPayload> handleOncomingRequest(ExternalRequest<SampleExternalRequestBody> request) {
        TimeoutCheckStartingPayload payload = new TimeoutCheckStartingPayload();
        payload.setWaitingTime(request.payload().getStartingValue());
        return new SagaStepMessage<>(payload);
    }

    @Override
    public ExternalReply<TimeoutCheckExternalReplyBody> prepareExternalReply(SagaStepHistory sagaStepHistory) {
        return new ExternalReply<>(new TimeoutCheckExternalReplyBody("Went through waiting time successfully"));
    }

    @Override
    public void defineStepMap(SagaStepMap stepMap) {
        stepMap.setInitialStep("WAITSTEP");
    }
}
