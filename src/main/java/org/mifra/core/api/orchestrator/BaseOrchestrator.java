package org.mifra.core.api.orchestrator;

import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.api.models.external.payloads.ExternalTimeoutReplyBody;

public abstract class BaseOrchestrator<I extends ExternalRequestBody, O extends ExternalReplyBody> implements Orchestrator<I,O> {

    public ExternalReply<ExternalTimeoutReplyBody> prepareTimeoutReply(ExternalRequest<I> request, String sagaId) {

        ExternalTimeoutReplyBody payload = new ExternalTimeoutReplyBody("The operation timed out waiting for a response.");
        ExternalReply<ExternalTimeoutReplyBody> reply = new ExternalReply<>();
        reply.setStatus("500");
        reply.setPayload(payload);

        return reply;
    }

}
