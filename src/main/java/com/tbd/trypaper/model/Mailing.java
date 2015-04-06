package com.tbd.trypaper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Models a TryPaper.com "Mailing" resource.  This is the fundamental concept in their API; it's analogous to
 * a "letter to send"<br/>
 * For more detailed information,
 * see <a href="http://docs.trypaper.com/article/12-mailing">http://docs.trypaper.com/article/12-mailing</a></p>
 *
 * <p>Example JSON representation:<br/>
 * <pre>
        {
          "ReturnAddressId": "Primary",
          "Recipient": {
            "Name": "John Smith",
            "Organization": "ACME, Inc.",
            "AddressLineOne": "123 Any Street",
            "AddressLineTwo": "Suite 789",
            "City": "Anytown",
            "Province": "WA",
            "PostalCode": "12345",
            "Country": "US"
          },
          "ReplyEnvelopeAddressId": "Primary",
          "HIPAASensitive": false,
          "Content": "http://i.trypaper.com/pub/MacroniAndCheeseReference.pdf",
          "Tags": [
            "force_bw",
            "duplex"
          ]
        }
 * </pre></p>
 */
public class Mailing {
    private final String returnAddressId;
    private final Address recipient;
    private final String replyEnvelopeAddressId;
    private final boolean hipaaSensitive;
    private final String content;
    private final List<String> tags;

    /**
     * @param returnAddressId
     * @param recipient
     * @param replyEnvelopeAddressId
     * @param hipaaSensitive
     * @param content This can <i>either</i> be a base64 encoded string of a PDF file <i>or</i> a URL that points to a
     * PDF or HTML file.  If it's a URL it must be reachable, naturally, from TryPaper's internal servers.  It is thus
     * not recommended to publicly host PDFs that are hipaaSensitive
     * @param tags
     */
    @JsonCreator
    public Mailing(@JsonProperty("ReturnAddressId") String returnAddressId,
                   @JsonProperty("Recipient") Address recipient,
                   @JsonProperty("ReplyEnvelopeAddressId") String replyEnvelopeAddressId,
                   @JsonProperty("HIPAASensitive") boolean hipaaSensitive,
                   @JsonProperty("Content") String content,
                   @JsonProperty("Tags") List<String> tags) {
        this.returnAddressId = returnAddressId;
        this.recipient = recipient;
        this.replyEnvelopeAddressId = replyEnvelopeAddressId;
        this.hipaaSensitive = hipaaSensitive;
        this.content = content;
        this.tags = tags;
    }

    @JsonProperty("ReturnAddressId")
    public String getReturnAddressId() {
        return returnAddressId;
    }

    @JsonProperty("Recipient")
    public Address getRecipient() {
        return recipient;
    }

    @JsonProperty("ReplyEnvelopeAddressId")
    public String getReplyEnvelopeAddressId() {
        return replyEnvelopeAddressId;
    }

    @JsonProperty("HIPAASensitive")
    public boolean isHipaaSensitive() {
        return hipaaSensitive;
    }

    @JsonProperty("Content")
    public String getContent() {
        return content;
    }

    @JsonProperty("Tags")
    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
