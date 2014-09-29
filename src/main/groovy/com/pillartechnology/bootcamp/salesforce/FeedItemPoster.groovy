package com.pillartechnology.bootcamp.salesforce
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHeader


class InvalidPostRequestException extends Exception{}


class FeedItemPoster {

    void postFeedItem(String message) {
        throw new InvalidPostRequestException()
    }

    HttpUriRequest createFeedRequest(String url, String feedback) {
        
//	def request = RequestBuilder.post()
//	    .addHeader("Content-Type", "application/json")
//	    .setUri(url + "/service/data/v31.0/chatter/feed-elements")
//	    .setEntity(new StringEntity(feedback))
//	    .build()

        def request = RequestBuilder.post()
	        .addHeader(new BasicHeader("Authorization", "Bearer"))
	        .addHeader("Content-Type", "application/json")
	        .setUri("${url}/services/data/v31.0/chatter/feed-elements")
	        .setEntity(new StringEntity(feedback))
	        .build()
    }
}
