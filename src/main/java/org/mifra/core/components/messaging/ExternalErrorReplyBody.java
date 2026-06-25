package org.mifra.core.components.messaging;

import org.mifra.core.api.models.external.payloads.ExternalReplyBody;

import java.util.List;

/**
 * An internal standard, platform-level reply payload returned when an incoming, request fails framework-level
 * validation.
 * @param errorCode
 * @param message
 * @param details
 */
public record ExternalErrorReplyBody(
        String errorCode,
        String message,
        List<String> details
) implements ExternalReplyBody {}
