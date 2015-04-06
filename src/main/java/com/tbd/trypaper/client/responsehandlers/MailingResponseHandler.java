package com.tbd.trypaper.client.responsehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbd.trypaper.model.MailingResponse;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>Encapsulates how to convert TryPaper's JSON response to a POST against their /Mailing endpoint into something
 * programmatically useful</p>
 */
public class MailingResponseHandler implements ResponseHandler<MailingResponse> {

    private final ObjectMapper objectMapper;
    // The header we expect to come back from TryPaper that contains the server-side request ID for the /Mailing
    // Post request we just made (that this class is handling the response for).  This is a UUID that should be useful
    // when using TryPaper.com's UI to see whether/how the request was processed on their servers.
    static final String HEADER_KEY_REQUEST_ID = "X-RequestId";

    public MailingResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public MailingResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        checkNotNull(response);
        MailingResponse mailingResponse = null;

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_CREATED) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                try (InputStream content = entity.getContent()) {
                    mailingResponse = this.objectMapper.readValue(content, MailingResponse.class);

                    Header[] headers = response.getHeaders(HEADER_KEY_REQUEST_ID);
                    checkState(headers.length == 1, "Error when processing response for a new /Mailing POST. "
                        + "Expected exactly 1 header of name %s but found %s", HEADER_KEY_REQUEST_ID, headers.length);
                    mailingResponse.setRequestId(headers[0].getValue());
                }
            }
        }
        else {
            // FIXME - improve this entire handling situation
            // I'm dumping the content that TryPaper is sending back on the ground.  You can see their response in 
            // their UI, but it'd be nice to figure out how to communicate this back to the caller programmatically
            throw new RuntimeException("Got status code " + statusCode + " from response [" + response + "]");
        }

        return mailingResponse;
    }

}
