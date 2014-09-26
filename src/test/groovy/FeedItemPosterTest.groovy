import org.apache.http.client.methods.*

class FeedItemPosterTest extends GroovyTestCase {

    void testSubmitEmptyFeedItemWillNotSubmitRequestToChatter() {

        FeedItemPoster feedItemPoster = new FeedItemPoster()

	shouldFail(InvalidPostRequestException) {
	    feedItemPoster.postFeedItem("")
	}
    }

    void testSubmitMessageHasURLNonEmptyPath() {
    
        FeedItemPoster feedItemPoster = new FeedItemPoster()
	HttpUriRequest request = feedItemPoster.createFeedRequest("")
        assertFalse("/".equals(request.getURI().toString()))
    }


}
