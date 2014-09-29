package com.pillartechnology.bootcamp.salesforce
import groovy.json.JsonSlurper

import org.apache.http.client.methods.*
import org.apache.http.message.BasicHeader
import org.junit.*

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

		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", "")
		assertFalse("/".equals(request.getURI().toString()))
	}

	void testSubmitMessageHasCorrectFeedbackMessage() {

		String feedback = "Sample feedback."
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", feedback)
		def requestBody = new JsonSlurper().parseText(request.entity.content.text).get("body").get("messageSegments")[0].get("text")
		assertTrue(requestBody.contains("Sample feedback."))
	}

	void testSubmitMessageHasValidHeaderValues() {

		HttpUriRequest request = feedItemPoster.createFeedRequest("", "token", "")

		Map<String, BasicHeader> headerMap = new HashMap<String, BasicHeader>()
		request.getAllHeaders().each { header ->
			headerMap.put(header.getName(), header)
		}

		assertEquals("Bearer token", headerMap.get("Authorization").value)
		assertEquals("application/json", headerMap.get("Content-Type").value)
	}

	void testSubmitFeedItemFormat() {
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", "Feedback")
		def feedback = new JsonSlurper().parseText(request.entity.content.text)
		assertNotNull(feedback)
	}
}
