package com.tbd.trypaper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbd.trypaper.client.responsehandlers.MailingResponseHandler;
import com.tbd.trypaper.client.responsehandlers.ReturnAddressResponseHandler;
import com.tbd.trypaper.model.Mailing;
import com.tbd.trypaper.model.MailingResponse;
import com.tbd.trypaper.model.ReturnAddress;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>TODO - perhaps much simplified with https://github.com/google/google-http-java-client</p>
 * <p>This is the default implementation of the TryPaperClient interface that provides a Java OO abstraction around the
 * REST endpoints hosted by trypaper.com.</p>
 * <p>Construction of this immutable client is done with the DefaultTryPaperClientBuilder class in this package. See that
 * class's class-level javadocs for additional examples and documentation.</p>
 */
public class DefaultTryPaperClient implements TryPaperClient {

    private final Logger logger = LoggerFactory.getLogger(DefaultTryPaperClient.class);
    private final URL baseURL;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final MailingResponseHandler mailingResponseHandler;
    private final ReturnAddressResponseHandler returnAddressResponseHandler;

    /**
     * <p>Package private on purpose -- use the {@code TryPaperClientBuilder} to construct a {@code TryPaperClient}
     * @param baseURL The "root" URL of any request made to TryPaper (e.g. by default this is "https://api.trypaper.com")
     * @param httpClient The Apache {@code HttpClient} to use when making HTTP requests of the TryPaper REST resources
     * @param objectMapper The Jackson ObjectMapper to use when (de)serializing JSON to/from TryPaper's REST resources
     */
    DefaultTryPaperClient(URL baseURL, HttpClient httpClient, ObjectMapper objectMapper) {
        this.baseURL = baseURL;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.mailingResponseHandler = new MailingResponseHandler(objectMapper);
        this.returnAddressResponseHandler = new ReturnAddressResponseHandler(objectMapper);
    }

    /**
     * @param mailing  POSTs a new {@code Mailing} to TryPaper's "/Mailing" endpoint.
     * @return The response from TryPaper, in convenient Java object form if the request was successful (a 201). Otherwise
     * this returns a null object.
     */
    @Override
    public MailingResponse postMailing(Mailing mailing) {
        checkNotNull(mailing);
        final String endpoint = "/Mailing";

        MailingResponse response = null;
        try {
            String mailingJson = this.objectMapper.writeValueAsString(mailing);

            HttpUriRequest requestBuilder = RequestBuilder
                .post()
                .setUri(new URL(this.baseURL, endpoint).toURI())
                .setEntity(new StringEntity(mailingJson))
                .build();

            logger.debug("Executing post against {} with content {}", requestBuilder.getURI(), mailingJson);
            response = this.httpClient.execute(requestBuilder, this.mailingResponseHandler);
        }
        catch (URISyntaxException | IOException ex) {
            logger.error("Error posting to " + endpoint + ". Returning null.", ex);
        }
        return response;
    }

    /**
     * @return The list of {@code ReturnAddress}es that have been configured on the TryPaper.com site for the API Key ID
     * used in the construction of this TryPaperClient.  Any errors are swallowed, logged, and an empty list is returned.
     */
    @Override
    public List<ReturnAddress> getReturnAddresses() {
        List<ReturnAddress> addresses = Collections.<ReturnAddress>emptyList();
        final String endpoint = "/ReturnAddress";

        try {
            HttpUriRequest requestBuilder = RequestBuilder
                .get()
                .setUri(new URL(this.baseURL, endpoint).toURI())
                .build();

            logger.debug("Executing get against {}", requestBuilder.getURI());
            addresses = this.httpClient.execute(requestBuilder, returnAddressResponseHandler);
        }
        catch (URISyntaxException | IOException ex) {
            logger.error("Error getting return addresses from " + endpoint + ".  Returning empty list.", ex);
        }
        return addresses;
    }
}
