package com.tbd.trypaper.client.responsehandlers;

import com.tbd.trypaper.model.MailingResponse;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <p>Puts the JSON-response handling of the MailingResponseHandler through its paces</p>
 * <p>Intentionally not calling "verify" on any of the HTTP objects because I want to test the output of the handlers,
 * not the internals.</p>
 */
public class TestMailingResponseHandler extends AbstractResponseHandlerTest {

    private MailingResponseHandler handler = new MailingResponseHandler(super.objectMapper);

    @Test(expected=RuntimeException.class)
    public void testNon200ResponseCausesRuntimeException() throws Exception {
        HttpResponse response = mockResponse(mockStatusLine(HttpStatus.SC_FORBIDDEN));
        handler.handleResponse(response);    // should throw exception
    }

    @Test
    public void test200ResponseWithNullEntityResultsInNullReturnValue() throws Exception {
        HttpResponse response = mockResponse(mockStatusLine(HttpStatus.SC_CREATED), null);

        MailingResponse mailingResponse = handler.handleResponse(response);

        assertNull(mailingResponse);
    }

    @Test
    public void testHappyPath() throws Exception {
        UUID requestId = UUID.randomUUID();
        BasicHeader[] headers = new BasicHeader[1];
        headers[0] = new BasicHeader(MailingResponseHandler.HEADER_KEY_REQUEST_ID, requestId.toString());

        HttpEntity entity = mockEntity("com/tbd/trypaper/client/responsehandlers/mailing-response.json");
        HttpResponse response = mockResponse(mockStatusLine(HttpStatus.SC_CREATED), entity, headers);

        MailingResponse mailingResponse = handler.handleResponse(response);

        assertNotNull(mailingResponse);
        assertEquals(requestId.toString(), mailingResponse.getRequestId());
        assertEquals("John Smith 12345 WA - 9326A1B047D6", mailingResponse.getId());
        assertEquals("8/3/2014 11:36 PM - Auto", mailingResponse.getBatchId());
        assertEquals(new DateTime("2014-08-22T02:24:39.162Z", DateTimeZone.UTC), mailingResponse.getCreateDateUtc());
    }
}
