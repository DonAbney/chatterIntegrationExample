class FeedItemRetrieverTest extends GroovyTestCase {

    void testRetrieveFeedItemsReturnsExceptionWithNullInput() {

        FeedItemRetriever feedItemRetriever = new FeedItemRetriever()

        shouldFail(InvalidInputException) {
            feedItemRetriever.findFeedItems()
        }
    }

}
