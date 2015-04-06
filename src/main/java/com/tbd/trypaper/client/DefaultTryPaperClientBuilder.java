package com.tbd.trypaper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.common.base.Throwables;
import org.apache.http.entity.ContentType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>Builder for a TryPaper.com HTTP Client.</p>
 * <p>Usage example:<br/><pre>
 DefaultTryPaperClient client = DefaultTryPaperClientBuilder
              .create()
              .withApiKey("blah")
              .build();
 </pre>
 * </p>
 * <p>Optionally, if the default {@code CloseableHttpClient} that this builder provides to the DefaultTryPaperClient isn't
 * sufficient, you can supply a custom {@code HttpClient} to this builder, <strong>as long as the TryPaper authorization key
 * is set as default HTTP header</strong>, e.g.:<br/><pre>
 MyAwesomeClient myClient = new MyAwesomeClient();       // implements org.apache.http.client.HttpClient
 myClient.setDefaultHeader("Authorization", "blah");

 DefaultTryPaperClient client = DefaultTryPaperClientBuilder
              .create()
              .withHttpClient(myClient)
              .build();
 </pre>
 * </p>
 */
public class DefaultTryPaperClientBuilder {

    /**
     * The default place to request TryPaper resources from
     */
    protected static final String DEFAULT_BASE_URL = "https://api.trypaper.com";

    /**
     * The header key TryPaper.com wants in every request. Set the value to the test API Key or the Production (paid for) key,
     * as appropriate.
     */
    protected static final String HEADER_KEY_AUTHZ = "Authorization";

    /**
     * The header key for the content-type of the data sent to TryPaper.  By default, it's JSON.
     * TODO - this has to be a defined constant somewhere in our dependencies; where is it?
     */
    protected static final String HEADER_CONTENT_TYPE = "Content-Type";

    protected static final String HEADER_ACCEPT = "Accept";

    private URL baseUri;
    private String apiKey;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    protected DefaultTryPaperClientBuilder() {
        super();
    }

    public static DefaultTryPaperClientBuilder create() {
        return new DefaultTryPaperClientBuilder();
    }

    /**
     * @param uri The root URL on which app TryPaper web service endpoints are appended.  E.g. "https://api.trypaper.com"
     * @return {@code this}, with the baseUri the built client should use set to {@code uri}
     */
    public final DefaultTryPaperClientBuilder withBaseUri(String uri) {
        this.baseUri = createUrlFromString(uri);
        return this;
    }

    /**
     * @param apiKey The value to set for the Authorization header.  Get your own using trypaper.com's UI.
     * @return {@code this}, with the {@code apiKey} to be used as a default http header by the DefaultTryPaperClient
     * provided to the caller on return from the {@code build()} method.
     */
    public final DefaultTryPaperClientBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * @param httpClient A specially-configured httpClient to use, instead of the default one used in this Builder's
     * construction of our DefaultTryPaperClient. <br>
     * <strong>NOTE:</strong> if you provide an httpClient, it will be used as-is.  Unless this {@code httpClient} has a
     * TryPaper-compatible authorization header added to its list of default HTTP headers, no requests to TryPaper are going
     * to work.
     * @return {@code this}, with the {@code httpClient} set for use in the construction of the DefaultTryPaperClient provided
     * to the caller on return from the {@code build()} method.
     */
    public final DefaultTryPaperClientBuilder withHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * @param objectMapper The objectMapper to be used by our DefaultTryPaperClient to serialize/deserialize JSON to/from
     * the TryPaper endpoint
     * @return {@code this}, with the {@code objectMapper} set for use in the construction of the DefaultTryPaperClient provided
     * to the caller on return from the {@code build()} method.
     */
    public final DefaultTryPaperClientBuilder withObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    /**
     * @return A {@code DefaultTryPaperClient}, configured with an {@code HttpClient}, per the way the caller invoked all this
     * builder's {@code with*()} methods.
     */
    public final DefaultTryPaperClient build() {
        checkNotNull(this.apiKey);

        if (this.baseUri == null) {
            this.baseUri = createUrlFromString(DEFAULT_BASE_URL);
        }

        if (this.httpClient == null) {
            this.httpClient = createDefaultHttpClient();
        }

        if (this.objectMapper == null) {
            this.objectMapper = createDefaultObjectMapper();
        }

        return new DefaultTryPaperClient(baseUri, httpClient, objectMapper);
    }


    /**
     * @return An Apache {@code HttpClient} with the TryPaper header authorization (key, value) pair added to the list of
     * default headers for any request made by the {@code HttpClient}
     */
    protected HttpClient createDefaultHttpClient() {
        Collection<? extends Header> authenticationHeader = Lists.newArrayList(
            new BasicHeader(HEADER_KEY_AUTHZ, apiKey),
            new BasicHeader(HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()),
            new BasicHeader(HEADER_ACCEPT, ContentType.APPLICATION_JSON.getMimeType()));

        return HttpClientBuilder
            .create()
            .setDefaultHeaders(authenticationHeader)
            .build();
    }

    /**
     * @return The default Jackson ObjectMapper this builder provides to the client for its use in (de)serialization of
     * JSON to/from the TryPaper REST endpoints.  This objectMapper is configured with a JodaModule to enable the processing
     * of "createTime" responses like '2014-08-22T02:24:39.162799Z' into JodaTime DateTime objects.
     * See <a href="https://github.com/FasterXML/jackson-datatype-joda">https://github.com/FasterXML/jackson-datatype-joda</a>
     */
    public static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.registerModule(new JodaModule());
        return defaultObjectMapper;
    }

    /**
     * @param urlString A non-null, well-formatted string to turn in to a URL
     * @return The URL object created from {@code urlString}, or a RuntimeException if it's a bad input argument
     */
    private URL createUrlFromString(String urlString) {
        checkNotNull(urlString);
        try {
            return new URL(urlString);
        }
        catch (MalformedURLException ex) {
            throw Throwables.propagate(ex);
        }
    }
}
