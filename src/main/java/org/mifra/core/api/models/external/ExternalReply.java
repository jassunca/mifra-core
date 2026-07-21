package org.mifra.core.api.models.external;

import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.components.messaging.EndpointReply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An aggregator object to be created at the end of a request handling with all the values to pass to the server's
 * reply object.
 * @param <T> The type of the object that will be serialized as the reply's body, which must implement
 *           ExternalReplyBody.
 */
public class ExternalReply <T extends ExternalReplyBody> implements EndpointReply {
    private String status;
    private final Map<String, List<String>> headers;
    private T payload;

    public ExternalReply () {
        headers = new HashMap<>();
    }

    public ExternalReply (T payload) {
        headers = new HashMap<>();
        this.payload = payload;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void putHeader(String key, List<String> value) {
        headers.put(key,value);
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
