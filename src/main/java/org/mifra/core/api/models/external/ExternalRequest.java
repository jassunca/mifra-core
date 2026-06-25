package org.mifra.core.api.models.external;

import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.components.messaging.EndpointRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * An immutable aggregator object created at the start of a request handling with all the relevant data both from the
 * request itself and added by the framework.
 * @param requestId The ID attributed to the request by the framework for internal identification.
 * @param timestamp The timestamp associated with the request.
 * @param headers List of headers both received in the request and added by the framework.
 * @param payload The deserialized request body.
 * @param <T> The type of the object deserialized as the request payload, which must implement ExternalRequestBody.
 */
public record ExternalRequest<T extends ExternalRequestBody>(
        String requestId,
        ZonedDateTime timestamp,
        Map<String, List<String>> headers,
        T payload
) implements EndpointRequest {}
