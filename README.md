# trypaper-client
Java REST client for the trypaper.com API


TODO:
2) timing / metrics
3) configuration
4) documentation / examples in this README
6) Can I get rid of JsonProperty annotations above the getters now that they're on the constructor?  And/or move all annotations
to just the variable declaration?
8) do I need to manually close the httpClient after using it in the TryPaperClient's methods?
12) I have different - and confusing - error handling from the different methods I'm implementing in the default client.
    I should either re-throw, or capture the error, and be consistent about returning null/empty/exceptions/etc
13) Optional/consider - making the MappingResponse final by moving the XRequestId field up into a "MappingResponseId" class
    that tuples the final MappingResponse JSON with the final XRequestId.  Overkill IMHO right now.
19) change the TestDefaultTryPaperClient to stop trying after some amount of time
21) Implement the POST method for the /ReturnAddress/ endpoint
22) Implement the GET method for the /ReturnAddress/{Id}
23) Implement the POST method for the /Batch/ endpoint http://docs.trypaper.com/article/9-batch 
24) Figure out how to get the Jacoco test coverage as part of the maven site report

Setup instructions:
* Get your temporary key
* Create a return address in TryPaper's UI named "Test_Return_Address"
* run with the -DskipITs=false
* mvn site