import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity

class InvalidPostRequestException extends Exception{}


class FeedItemPoster {

    void postFeedItem(String message) {
        throw new InvalidPostRequestException()
    }

    HttpUriRequest createFeedRequest(String url, String feedback) {
        
	def request = RequestBuilder.post()
	    .setUri(url + "/service/data/v31.0/chatter/feed-elements")
	    .setEntity(new StringEntity(feedback))
	    .build()
    }
}
