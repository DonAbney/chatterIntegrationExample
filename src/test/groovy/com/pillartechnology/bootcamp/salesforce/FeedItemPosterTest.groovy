package com.pillartechnology.bootcamp.salesforce
import static org.mockito.Mockito.*
import groovy.json.JsonSlurper
import groovy.mock.interceptor.*

import org.apache.http.HttpResponse
import org.apache.http.HttpVersion
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.*
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicHttpResponse
import org.apache.http.message.BasicStatusLine
import org.junit.*
import org.mockito.*

class FeedItemPosterTest extends GroovyTestCase {
	private FeedItemPoster feedItemPoster
	private HttpUriRequest request

	void setUp() {
		feedItemPoster = new FeedItemPosterImpl()
	}

	void testSubmitNullFeedItemWillNotSubmitRequestToChatter() {
		def client = HttpClients.createDefault()
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem(null, "test", "test", "test", client) }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", null, "test", "test", client) }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", "test", null, "test", client) }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", "test", "test", null, client) }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", "test", "test", "test", null) }
	}

	void testSubmitMessageHasURLNonEmptyPath() {

		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", "", "")
		assertFalse("/".equals(request.getURI().toString()))
	}

	void testSubmitMessageHasCorrectFeedbackMessage() {

		String feedback = "Sample feedback."
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", feedback, "")
		def requestBody = new JsonSlurper().parseText(request.entity.content.text).get("body").get("messageSegments")[0].get("text")
		assertTrue(requestBody.contains("Sample feedback."))
	}

	void testSubmitMessageHasValidHeaderValues() {

		HttpUriRequest request = feedItemPoster.createFeedRequest("", "token", "", "")

		Map<String, BasicHeader> headerMap = new HashMap<String, BasicHeader>()
		request.getAllHeaders().each { header ->
			headerMap.put(header.getName(), header)
		}

		assertEquals("Bearer token", headerMap.get("Authorization").value)
		assertEquals("application/json", headerMap.get("Content-Type").value)
	}

	void testSubmitFeedItemFormat() {
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", "Feedback", "")
		def feedback = new JsonSlurper().parseText(request.entity.content.text)
		assertNotNull(feedback)
	}

	void testSubmitFeedItemHasCorrectTopic() {
		String feedback = "Sample feedback."
		String topic = "Sample topic"
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", feedback, topic)
		def requestBody = new JsonSlurper().parseText(request.entity.content.text).get("body").get("messageSegments")[0].get("text")
		assertTrue(requestBody.contains(" #[${topic}]"))
	}

	void testForHttpClientCallInPostFeedItem() {
		HttpClient defaultHttpClient = Mockito.mock(HttpClient)
		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 201, "test"))
		when(defaultHttpClient.execute(any())).thenReturn(response)
		feedItemPoster.postFeedItem("test","test","test","test", defaultHttpClient)
		Mockito.verify(defaultHttpClient, Mockito.times(1))
	}
}
