package org.mifra.core.components.domain.messages;

import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

public record HistoryKey (
        String stepId,
        Class<? extends SagaStepPayload> payloadClass
) {

}
