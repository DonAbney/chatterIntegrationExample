import org.apache.http.client.methods.*

class InvalidPostRequestException extends Exception{}


class FeedItemPoster {

    void postFeedItem(String message) {
        throw new InvalidPostRequestException()
    }

    HttpUriRequest createFeedRequest(String url) {
        
	def request = RequestBuilder.post()
	    .setUri(url + "/service/data/v31.0/chatter/feed-elements")
	    .build()
    }
}
