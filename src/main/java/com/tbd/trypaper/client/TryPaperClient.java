package com.tbd.trypaper.client;

import com.tbd.trypaper.model.Mailing;
import com.tbd.trypaper.model.MailingResponse;
import com.tbd.trypaper.model.ReturnAddress;
import java.util.List;

/**
 * <p>The interface for a Java client to TryPaper's API.</p>
 */
public interface TryPaperClient {

    /**
     * <p>Once a {@code Mailing} is POSTed to TryPaper it will be available to be sent through the mail as part of a
     * Batch</p>
     * <p>For more information, see
     * <a href="http://docs.trypaper.com/article/12-mailing">http://docs.trypaper.com/article/12-mailing</a></p>
     *
     * @param mailing An object representing content to send to a person, and a return address ID.
     * @return The details of the response from TryPaper.  See implementation class for details about how errors are
     * handled (this interface makes no guarantees about error handling)
     */
    MailingResponse postMailing(Mailing mailing);

    /**
     * <p>Before using TryPaper to mail anything, an account owner must configure one or more {@code ReturnAddress}es</p>
     * <p>For more information, see
     * <a href="http://docs.trypaper.com/article/13-return-address">http://docs.trypaper.com/article/13-return-address</a></p>
     *
     * @return A List of all the {@code ReturnAddress}es known by TryPaper for the account whose API Key is being used
     * to make the API request
     */
    List<ReturnAddress> getReturnAddresses();
}
