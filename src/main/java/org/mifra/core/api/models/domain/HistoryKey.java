package org.mifra.core.api.models.domain;

import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

public record HistoryKey (
        String stepId,
        Class<? extends SagaStepPayload> payloadClass
) {

}
