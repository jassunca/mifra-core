package org.mifra.core.network.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.components.external.assemblers.HttpMessageAssembler;
import org.mifra.core.components.messaging.ExternalErrorReplyBody;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jetty-based handler that listens to client requests and dispatches them to the appropriate orchestrators.
 */
public class MifraHttpHandler extends Handler.Abstract{

    HttpMessageAssembler assembler;
    private ObjectMapper mapper;

    public MifraHttpHandler(HttpMessageAssembler assembler) {
        this.assembler = assembler;
        mapper = new ObjectMapper();
    }

    @Override
    public boolean handle(Request request, Response response, Callback callback) throws Exception {
        String path = Request.getPathInContext(request);
        String rawBodyText = Content.Source.asString(request);
        String requestId = request.getId();

        Map<String, List<String>> rawHeaders = new HashMap<>();
        for (HttpField field : request.getHeaders()) {
            rawHeaders.computeIfAbsent(field.getName(), k -> new ArrayList<>()).add(field.getValue());
        }

        try {
            ExternalReply<?> replyEnvelope = assembler.process(
                    path, rawBodyText, requestId, rawHeaders
            );

            if (replyEnvelope == null) {
                return false;
            }

            response.setStatus(200);

            replyEnvelope.getHeaders().forEach((key, values) -> {
                for (String val : values) {
                    response.getHeaders().add(key, val);
                }
            });

            byte[] jsonBytes = this.mapper.writeValueAsBytes(replyEnvelope);
            response.getHeaders().put("Content-Type", "application/json");
            response.write(true, ByteBuffer.wrap(jsonBytes), callback);
            return true;

        } catch (JsonProcessingException | IllegalArgumentException e) {
            ExternalErrorReplyBody error = new ExternalErrorReplyBody("BAD_REQUEST_FORMAT", e.getMessage(), List.of());
            ExternalReply<ExternalReplyBody> errorEnvelope = new ExternalReply<>(error);
            errorEnvelope.setStatus("BAD_REQUEST");

            response.setStatus(400);
            response.write(true, ByteBuffer.wrap(mapper.writeValueAsBytes(errorEnvelope)), callback);
            return true;

        } catch (Exception e) {
            // General protection against unhandled execution faults
            ExternalErrorReplyBody error = new ExternalErrorReplyBody("INTERNAL_SERVER_ERROR", "An unexpected error occurred.", List.of());
            ExternalReply<ExternalReplyBody> errorEnvelope = new ExternalReply<>(error);
            errorEnvelope.setStatus("SERVER_FAULT");

            response.setStatus(500);
            response.write(true, ByteBuffer.wrap(mapper.writeValueAsBytes(errorEnvelope)), callback);
            return true;
        }

    }
}
