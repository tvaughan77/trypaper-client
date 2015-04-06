package com.tbd.trypaper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * Models the TryPaper "Address", which, represented in JSON looks like:
 * <pre>
        "Address": {
           "Name": "John Smith",
           "Organization": "ACME, Inc.",
           "AddressLineOne": "123 Any Street",
           "AddressLineTwo": "Suite 789",
           "City": "Anytown",
           "Province": "WA",
           "PostalCode": "12345",
           "Country": "US"
         }
 * </pre>
 */
public class Address {

    private final String name;
    private final String organization;
    private final String addressLineOne;
    private final String addressLineTwo;
    private final String city;
    private final String province;
    private final String postalCode;
    private final String country;

    @JsonCreator
    public Address(@JsonProperty("Name") String name,
                   @JsonProperty("Organization") String organization,
                   @JsonProperty("AddressLineOne") String addressLineOne,
                   @JsonProperty("AddressLineTwo") String addressLineTwo,
                   @JsonProperty("City") String city,
                   @JsonProperty("Province") String province,
                   @JsonProperty("PostalCode") String postalCode,
                   @JsonProperty("Country") String country) {
        this.name = name;
        this.organization = organization;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
    }

    /**
     * @return The recipient name; e.g. "John Doe"
     */
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    /**
     * @return The destination organization
     */
    @JsonProperty("Organization")
    public String getOrganization() {
        return organization;
    }

    /**
     * @return The first line of the recipient address
     */
    @JsonProperty("AddressLineOne")
    public String getAddressLineOne() {
        return addressLineOne;
    }

    /**
     * @return The second line of the recipient address
     */
    @JsonProperty("AddressLineTwo")
    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    /**
     * @return The city of the recipient address
     */
    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    /**
     * @return The province of the recipient address (in the US, the "state").  E.g. "VA"
     */
    @JsonProperty("Province")
    public String getProvince() {
        return province;
    }

    /**
     * @return The postal code of the recipient address (in the US, the "zip code").  E.g. "22201"
     */
    @JsonProperty("PostalCode")
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return The country of the recipient address.
     */
    @JsonProperty("Country")
    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
