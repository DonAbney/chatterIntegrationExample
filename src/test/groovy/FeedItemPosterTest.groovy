import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHeader
import org.apache.http.params.HttpParams

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

   void testSubmitMessageHasCorrectFeedbackMessage() {

        String feedback = "Sample feedback."
	HttpUriRequest request = feedItemPoster.createFeedRequest("", feedback)
        assertFalse("".equals(request.entity.content.text))
        assertEquals(feedback, request.entity.content.text)
   }

   void testSubmitMessageHasValidContentType() {
       
        String header = "Content-Type" 
	HttpUriRequest request = feedItemPoster.createFeedRequest("", "")
        //assertTrue(request.getHeaders().contains(header))

	HttpParams params = request.getParams();
	params.each {println it.toString()}

	assert request.getHeaders().contains(header)
	assert "" == request.toString()


   }

}
