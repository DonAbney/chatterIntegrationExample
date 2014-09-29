package com.pillartechnology.bootcamp.salesforce
import org.apache.http.client.methods.*
import org.apache.http.message.BasicHeader
import org.apache.http.params.HttpParams
import org.junit.*

import com.pillartechnology.bootcamp.salesforce.FeedItemPoster;
import com.pillartechnology.bootcamp.salesforce.InvalidPostRequestException;

class FeedItemPosterTest extends GroovyTestCase {

	private FeedItemPoster feedItemPoster;
	private HttpUriRequest request;

	void setUp() {
		feedItemPoster = new FeedItemPoster()
	}

	void testSubmitEmptyFeedItemWillNotSubmitRequestToChatter() {


		shouldFail(InvalidPostRequestException) { feedItemPoster.postFeedItem("") }
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

		HttpUriRequest request = feedItemPoster.createFeedRequest("", "")

		Map<String, BasicHeader> headerMap = new HashMap<String, BasicHeader>()
		request.getAllHeaders().each { header -> 
			headerMap.put(header.getName(), header)
		}
		
		assertEquals("Bearer", headerMap.get("Authorization").value)
		assertEquals("application/json", headerMap.get("Content-Type").value)
	}

}
