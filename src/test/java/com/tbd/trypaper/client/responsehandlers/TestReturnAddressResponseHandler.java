package com.tbd.trypaper.client.responsehandlers;

import com.tbd.trypaper.model.ReturnAddress;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <p>Puts the JSON-response handling of the ReturnAddressResponseHandler through its paces</p>
 * <p>Intentionally not calling "verify" on any of the HTTP objects because I want to test the output of the handlers,
 * not the internals.</p>
 */
public class TestReturnAddressResponseHandler extends AbstractResponseHandlerTest {

    private ReturnAddressResponseHandler handler = new ReturnAddressResponseHandler(super.objectMapper);

    @Test(expected=RuntimeException.class)
    public void testNon200ResponseCausesRuntimeException() throws Exception {
        HttpResponse response = mockResponse(mockStatusLine(HttpStatus.SC_REQUEST_TIMEOUT));
        handler.handleResponse(response);    // should throw exception
    }

    @Test
    public void test200ResponseWithNullEntityResultsInEmptyList() throws IOException {
        HttpResponse response = mockResponse(mockStatusLine(HttpStatus.SC_OK), null);

        List<ReturnAddress> addresses = handler.handleResponse(response);

        assertTrue(addresses.isEmpty());
    }

    @Test
    public void testHappyPath() throws Exception {
        HttpEntity entity = mockEntity("com/tbd/trypaper/client/responsehandlers/return-address-response.json");
        HttpResponse response = mockResponse(mockStatusLine(HttpStatus.SC_OK), entity);

        List<ReturnAddress> addresses = handler.handleResponse(response);
        assertEquals(2, addresses.size());
        assertEquals("Primary", addresses.get(0).getId());  // just spot check a couple fields
        assertEquals("123 Any Street", addresses.get(0).getAddress().getAddressLineOne());
        assertEquals("22209", addresses.get(1).getAddress().getPostalCode());
    }
}
