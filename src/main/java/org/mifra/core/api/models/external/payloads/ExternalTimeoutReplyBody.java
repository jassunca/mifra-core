package org.mifra.core.api.models.external.payloads;

public class ExternalTimeoutReplyBody implements ExternalReplyBody{

    private final String message;

    public ExternalTimeoutReplyBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
