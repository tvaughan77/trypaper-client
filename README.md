# trypaper-client
Java REST client for the trypaper.com API

# Setup steps
1. Log on to [trypaper.com's "PrintRoom"](https://www.trypaper.com/Authentication)
2. Get a Test API Key 
3. Setup a Return Address with the id "Test_Return_Address"
4. Copy src/test/resources/CHANGE_ME_test_api_key.properties to src/test/resources/test_api_key.properties
5. Copy your TEST API Key in the test_api_key.properties
6. Run "mvn clean install" to get the project dependencies downloaded and the basic build working
7. Run "mvn clean install -DskipITs" to actually hit the trypaper.com endpoints with test data.

# Usage
Construct a client with your API Key and any other specific behavior rides you want or need:
```Java
// Simplest client:
DefaultTryPaperClient client = DefaultTryPaperClientBuilder
    .create( )
    .withApiKey( "my key" )
    .build( );

// Builder provides additional configuration options:
DefaultTryPaperClient client = DefaultTryPaperClientBuilder
    .create( )
    .withApiKey( "my key" )
    .withBaseUri( "http://trypaper.com/some/new/url/" )
    .withHttpClient( mySpecialApacheHttpClient )
    .withObjectMapper( mySpecialJacksonMapper )
    .build();
```

Then use the client to send Mailings to TryPaper:
```Java
Mailing myMailing = new Mailing( x, y, z );
MailingResponse response = client.postMailing( myMailing );
```

# Contributing
Feel free to issue pull requests against this and/or fork it for your own purposes.  If you want to contirbute, run "mvn clean site" before submitting a pull request and make sure checkstyle, pmd, findbugs are all reporting a good build.

Note to contributors: code coverage reports are generated as part of the build and available under ./target/site/jacoco-combined/index.html.  These reports are not currently linked to off of the normal maven-site-plugin generated ./target/site, but it'd be nice if they were.

# TODO:
2. timing / metrics
3. configuration
6. Can I get rid of JsonProperty annotations above the getters now that they're on the constructor?  And/or move all annotations
to just the variable declaration?
8. do I need to manually close the httpClient after using it in the TryPaperClient's methods?
12. I have different - and confusing - error handling from the different methods I'm implementing in the default client.
    I should either re-throw, or capture the error, and be consistent about returning null/empty/exceptions/etc
13. Optional/consider - making the MappingResponse final by moving the XRequestId field up into a "MappingResponseId" class
    that tuples the final MappingResponse JSON with the final XRequestId.  Overkill IMHO right now.
19. change the TestDefaultTryPaperClient to stop trying after some amount of time
21. Implement the POST method for the /ReturnAddress/ endpoint
22. Implement the GET method for the /ReturnAddress/{Id}
23. Implement the POST method for the /Batch/ endpoint http://docs.trypaper.com/article/9-batch 
24. Figure out how to get the Jacoco test coverage as part of the maven site report
