package com.tbd.trypaper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.tbd.trypaper.model.Mailing;
import com.tbd.trypaper.model.MailingResponse;
import com.tbd.trypaper.model.ReturnAddress;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * <p>This Integration Test actually goes out hits TryPaper with a Test API Key and tries to GET the list of
 * ReturnAddress(es) on file with TryPaper and attempts to POST a couple of different {@code Mailing} objects</p>
 * <p>For this to work, there must exist:
 * <ol>
 *   <li>a file named src/test/resources/test_api_key.properties which has in it:</li>
 *   <li>trypaper.api.key=YOUR_KEY_HERE   (should probably start with "TPTEST"...)</li>
 * </ol>
 * </p>
 */
public class IntTestDefaultTryPaperClient {

    private final Logger logger = LoggerFactory.getLogger(IntTestDefaultTryPaperClient.class);
    private static String testApiKey;
    private DefaultTryPaperClient client;
    private ObjectMapper objectMapper;

    @BeforeClass
    public static void loadTestApiKey() throws IOException {
        Properties properties = new Properties();
        properties.load(Resources.getResource("test_api_key.properties").openStream());
        testApiKey = properties.getProperty("trypaper.api.key");
    }

    @Before
    public void setUp() {
        client = DefaultTryPaperClientBuilder.create().withApiKey(testApiKey).build();
        objectMapper = new ObjectMapper();
    }

    /**
     * <p>This test uses a URL in the content field to inform TryPaper of where to get the mailing content.  This
     * would be inappropriate for a HIPAA-sensitive document because it'd need to be publicly available for
     * TryPaper to fetch it and process it.</p>
     * @throws IOException
     */
    @Test
    public void testPostMailingWithUrlContent() throws IOException {
        Mailing mailing = objectMapper.readValue(Resources.getResource("com/tbd/trypaper/model/mailing.json"), Mailing.class);
        MailingResponse mailingResponse = client.postMailing(mailing);

        assertNotNull(mailingResponse);
        assertNotNull(mailingResponse.getRequestId());
        logger.debug("Posted a Mailing request and got requestId {} back", mailingResponse.getRequestId());
    }

    /**
     * <p>This test loads a PDF off disk, base64 encodes it and uses that encoded String as the content of the mailing</p>
     * @throws IOException
     */
    @Test
    public void testPostMailingWithBase64Content() throws IOException {
        String encodedPdfContent = encodePdf("com/tbd/trypaper/client/one_page_sample.pdf");

        // Use our existing fixture as a base to create a new Mailing object (basically, just a copy but with the
        // encoded PDF as the content instead of the URL that you'd find in the fixture
        Mailing mailingWithUrlContent =
            objectMapper.readValue(Resources.getResource("com/tbd/trypaper/model/mailing.json"), Mailing.class);
        Mailing mailingWithBase64Content =
            new Mailing(mailingWithUrlContent.getReturnAddressId(),
                        mailingWithUrlContent.getRecipient(),
                        mailingWithUrlContent.getReplyEnvelopeAddressId(),
                        mailingWithUrlContent.isHipaaSensitive(),
                        encodedPdfContent,
                        mailingWithUrlContent.getTags());

        MailingResponse mailingResponse = client.postMailing(mailingWithBase64Content);

        assertNotNull(mailingResponse);
        assertNotNull(mailingResponse.getRequestId());
        logger.debug("Posted a Mailing request and got requestId {} back", mailingResponse.getRequestId());
    }

    @Test
    public void testGetReturnAddresses() throws IOException {
        List<ReturnAddress> returnAddresses = client.getReturnAddresses();

        assertFalse(returnAddresses.isEmpty());
        logger.debug("All our returnAddresses:");
        for (ReturnAddress returnAddress : returnAddresses) {
            logger.debug("\t{}", returnAddress);
        }
    }

    private String encodePdf(String location) throws IOException {
        URL onePagePdfUrl = Resources.getResource(location);
        return Base64.encodeBase64String(Resources.toByteArray(onePagePdfUrl));
    }
}
