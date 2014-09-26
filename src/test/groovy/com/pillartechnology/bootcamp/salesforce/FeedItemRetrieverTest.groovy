package com.pillartechnology.bootcamp.salesforce
import com.pillartechnology.bootcamp.salesforce.FeedItemRetriever;
import com.pillartechnology.bootcamp.salesforce.InvalidInputException;

class FeedItemRetrieverTest extends GroovyTestCase {

    void testRetrieveFeedItemsReturnsExceptionWithNullInput() {

        FeedItemRetriever feedItemRetriever = new FeedItemRetriever()

        shouldFail(InvalidInputException) {
            feedItemRetriever.findFeedItems()
        }
    }

}
