package com.tbd.trypaper.client.responsehandlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbd.trypaper.model.ReturnAddress;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>An Apache-HttpClient ResponseHandler that encapsulates the knowledge needed to convert a response from TryPaper's
 * "/ReturnAddress" resource to a Collection of model objects that represent {@code ReturnAddress}es</p>
 */
public class ReturnAddressResponseHandler implements ResponseHandler<List<ReturnAddress>> {

    private final ObjectMapper objectMapper;

    public ReturnAddressResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ReturnAddress> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        checkNotNull(response);
        List<ReturnAddress> addresses = Collections.<ReturnAddress>emptyList();

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream content = entity.getContent()) {
                    addresses = this.objectMapper.readValue(content, new TypeReference<List<ReturnAddress>>(){});
                }
            }
        }
        else {
            // FIXME - improve this entire handling situation
            throw new RuntimeException("Got status code " + statusCode + " from request");
        }
        return addresses;
    }

}
