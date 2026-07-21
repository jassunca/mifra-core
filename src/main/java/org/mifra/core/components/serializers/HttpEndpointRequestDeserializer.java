package org.mifra.core.components.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;

/**
 * Deserialize an HTTP external request body using a template class that must implement ExternalRequestBody.
 */
public class HttpEndpointRequestDeserializer {

    private ObjectMapper mapper;

    public HttpEndpointRequestDeserializer() {
        mapper = new ObjectMapper();
    }

    public  <T extends ExternalRequestBody> T deserialize(String requestBody, Class<T> requestType) throws JsonProcessingException {
        return mapper.readValue(requestBody, requestType);
    }
}
