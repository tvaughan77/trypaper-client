package com.tbd.trypaper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Models TryPaper's "ReturnAddress" resource<br/>
 * For more detail, see: 
 * <a href="http://docs.trypaper.com/article/13-return-address">http://docs.trypaper.com/article/13-return-address</a></p>
 */
public class ReturnAddress {

    private final String id;
    private final Address address;

    @JsonCreator
    public ReturnAddress(@JsonProperty("Id") String id,
                         @JsonProperty("Address") Address address) {
        this.id = id;
        this.address = address;
    }

    /**
     * @return The server-side ID of the return address (e.g. whatever unique ID TryPaper associates with the contents of the
     * {@code address})
     */
    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    /**
     * @return The return address content (e.g. John Doe / 123 Main St / Wherever / VA)
     */
    @JsonProperty("Address")
    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
