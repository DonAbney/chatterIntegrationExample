import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity

class FeedItemPosterTest extends GroovyTestCase {

    private FeedItemPoster feedItemPoster;
    private HttpUriRequest request;

    void setUp() {
        feedItemPoster = new FeedItemPoster()

    }

    void testSubmitEmptyFeedItemWillNotSubmitRequestToChatter() {


	shouldFail(InvalidPostRequestException) {
	    feedItemPoster.postFeedItem("")
	}
    }

    void testSubmitMessageHasURLNonEmptyPath() {
    
	HttpUriRequest request = feedItemPoster.createFeedRequest("", "")
        assertFalse("/".equals(request.getURI().toString()))
    }

   void testSubmitMessageHasNonEmptyMessage() {

        String feedback = "Sample feedback."
	HttpUriRequest request = feedItemPoster.createFeedRequest("", feedback)
        assertFalse("".equals(request.entity.content.text))
        assertEquals(feedback, request.entity.content.text)
   }


}
