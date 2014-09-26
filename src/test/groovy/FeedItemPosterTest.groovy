class FeedItemPosterTest extends GroovyTestCase {

    void testSubmitEmptyFeedItemWillNotSubmitRequestToChatter() {

        FeedItemPoster feedItemPoster = new FeedItemPoster()

	shouldFail(InvalidPostRequestException) {
	    feedItemPoster.postFeedItem()
	}
    }

}
