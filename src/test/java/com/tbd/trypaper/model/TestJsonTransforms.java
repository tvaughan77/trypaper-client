package com.tbd.trypaper.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Tests that the Json transformation of our model objects is what's expected by TryPaper's API
 */
public class TestJsonTransforms {

    private ObjectMapper objectMapper;
    private static final Address ADDRESS_FULL = new Address("John Smith",
                                                            "ACME, Inc.",
                                                            "123 Any Street",
                                                            "Suite 789",
                                                            "Anytown",
                                                            "WA",
                                                            "12345",
                                                            "US");
    private static final Address ADDRESS_PARTIAL = new Address("Jane Doe",
                                                               null,
                                                               "123 Main Avenue",
                                                               null,
                                                               "Washington",
                                                               "DC",
                                                               "12345",
                                                               null);

    private static final ReturnAddress RETURN_ADDRESS = new ReturnAddress("Primary", ADDRESS_FULL);

    private static final Mailing MAILING = new Mailing("Test_Return_Address",
                                                       ADDRESS_FULL,
                                                       "Test_Return_Address",
                                                       false,
                                                       "http://i.trypaper.com/pub/MacroniAndCheeseReference.pdf",
                                                       Lists.newArrayList("force_bw", "duplex"));

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper = objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void testAddressFullDeserializesCorrectly() throws IOException, JSONException {
        testDeserialization(ADDRESS_FULL, "com/tbd/trypaper/model/address-full.json");
    }

    @Test
    public void testAddressPartialDeserializesCorrectly() throws IOException, JSONException {
        testDeserialization(ADDRESS_PARTIAL, "com/tbd/trypaper/model/address-partial.json");
    }

    @Test
    public void testReturnAddressDeserializesCorrectly() throws IOException, JSONException {
        testDeserialization(RETURN_ADDRESS, "com/tbd/trypaper/model/return-address.json");
    }

    @Test
    public void testMailingDeserializesCorrectly() throws IOException, JSONException {
        testDeserialization(MAILING, "com/tbd/trypaper/model/mailing.json");
    }

    private void testDeserialization(Object object, String fixturePath) throws IOException, JSONException {
        String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

        URL fixture = Resources.getResource(fixturePath);
        String expected = Resources.toString(fixture, Charset.defaultCharset());

        JSONAssert.assertEquals(expected, actual, true);
    }
}
