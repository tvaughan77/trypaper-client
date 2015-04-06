package com.tbd.trypaper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * <p>Models the TryPaper 201 response from the /mailing resource.<br/>
 * For more detailed information, see
 * <a href="http://docs.trypaper.com/article/5-quick-start-guide">http://docs.trypaper.com/article/5-quick-start-guide</a></p>
 *
 * <p>
 * Example JSON response:<br/>
 * <pre>
        HTTP/1.1 201
        Location: https://api.trypaper.com/Mailing
        X-RequestId: 985ec821-f336-45da-834f-c07f1d5f580f
        X-ApiVersion: 2.0.0
        Content-Type: application/json; charset=utf-8
        {
            "Id":"John Smith 12345 WA - 9326A1B047D6",
            "BatchId":"8/3/2014 11:36 PM - Auto",
            "CreateDateUtc":"2014-08-22T02:24:39.162799Z"
        }
 * </pre></p>
 */
public class MailingResponse {

    private final String id;
    private final String batchId;
    private final DateTime createDateUtc;
    private String requestId;   // Not final because it's in a header field that JSON parsing can't get to easily

    @JsonCreator
    public MailingResponse(@JsonProperty("Id") String id,
                           @JsonProperty("BatchId") String batchId,
                           @JsonProperty("CreateDateUtc") DateTime createDateUtc) {
        this.id = id;
        this.batchId = batchId;
        this.createDateUtc = createDateUtc;
    }

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("BatchId")
    public String getBatchId() {
        return batchId;
    }

    @JsonProperty("CreateDateUtc")
    public DateTime getCreateDateUtc() {
        return createDateUtc;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
