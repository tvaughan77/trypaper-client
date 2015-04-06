package com.tbd.trypaper.client.responsehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.tbd.trypaper.client.DefaultTryPaperClientBuilder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import static org.easymock.EasyMock.*;

/**
 * <p>Common/reusable pieces across the tests for the different ResponseHandlers in this package</p>
 */
abstract class AbstractResponseHandlerTest {

    protected final ObjectMapper objectMapper = DefaultTryPaperClientBuilder.createDefaultObjectMapper();

    /**
     * @param contentFixture A classpath resource containing whatever content the mock HttpEntity should stream back
     * from a call to its {@code getContent()} method.
     * @return A mock HttpEntity that will return an input stream with the contents of {@code contentFixture}
     * @throws Exception
     */
    protected HttpEntity mockEntity(String contentFixture) throws Exception {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(Resources.getResource(contentFixture).getFile()));
        HttpEntity entity = createStrictMock(HttpEntity.class);
        expect(entity.getContent()).andReturn(inputStream);
        replay(entity);
        return entity;
    }

    /**
     * @param statusLine
     * @param entity
     * @param headers An array of headers that is returned on <i>any</i> call to {@code HttpResponse.getHeaders( )}
     * @return A mock HttpResponse that will return the {@code statusLine}, {@code entity} and {@code headers}
     */
    protected HttpResponse mockResponse(StatusLine statusLine, HttpEntity entity, Header[] headers) {
        HttpResponse response = createNiceMock(HttpResponse.class);
        expect(response.getStatusLine()).andReturn(statusLine).anyTimes();
        expect(response.getEntity()).andReturn(entity).anyTimes();
        expect(response.getHeaders(anyString())).andReturn(headers).anyTimes();
        replay(response);
        return response;
    }

    /**
     * @param statusLine
     * @param entity
     * @return An HttpResponse returning status line {@code statusLine} and whatever content that's been set up in the
     * {@code entity}
     */
    protected HttpResponse mockResponse(StatusLine statusLine, HttpEntity entity) {
        return mockResponse(statusLine, entity, null);
    }

    /**
     * @param statusLine
     * @return An HttpResponse returning just a status line but null entity content, null headers, null everything-else
     */
    protected HttpResponse mockResponse(StatusLine statusLine) {
        return mockResponse(statusLine, null);
    }

    /**
     * @param responseCode
     * @return A mock StatusLine that returns {@code responseCode} when its {@code getStatusCode()} method is called
     */
    protected StatusLine mockStatusLine(int responseCode) {
        StatusLine statusLine = createStrictMock(StatusLine.class);
        expect(statusLine.getStatusCode()).andReturn(responseCode);
        replay(statusLine);
        return statusLine;
    }
}
