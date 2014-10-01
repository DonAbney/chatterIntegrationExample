package com.pillartechnology.bootcamp.salesforce
import static org.mockito.Mockito.*
import groovy.json.JsonSlurper
import groovy.mock.interceptor.*

import org.apache.http.HttpException
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
	FeedItemPoster feedItemPoster
	HttpUriRequest request
	List<String> topic
	HttpClient defaultHttpClient
	HttpClient failingHttpClient
	
	void setUp() {
		feedItemPoster = new FeedItemPosterImpl()
		topic = new ArrayList<String>()
		topic.add("test topic")
		defaultHttpClient = Mockito.mock(HttpClient.class)
		failingHttpClient = Mockito.mock(HttpClient.class)
	}

	void testSubmitNullFeedItemWillNotSubmitRequestToChatter() {
		def client = HttpClients.createDefault()
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem(null, "test", "test", new ArrayList<String>(), client, "test") }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", null, "test", new ArrayList<String>(), client, "test") }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", "test", null, new ArrayList<String>(), client, "test") }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", "test", "test", null, client, "test") }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", "test", "test", new ArrayList<String>(), null, "test") }
		shouldFail(IllegalArgumentException) { feedItemPoster.postFeedItem("test", "test", "test", new ArrayList<String>(), client, null) }
	}

	void testSubmitMessageHasURLNonEmptyPath() {
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", "", new ArrayList<String>(), "")
		assertFalse("/".equals(request.getURI().toString()))
	}

	void testSubmitMessageHasCorrectFeedbackMessage() {
		String feedback = "Sample feedback."
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", feedback, new ArrayList<String>(), "")
		def requestBody = new JsonSlurper().parseText(request.entity.content.text).get("body").get("messageSegments")[0].get("text")
		assertTrue(requestBody.contains("Sample feedback."))
	}

	void testSubmitMessageHasValidHeaderValues() {
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "token", "", new ArrayList<String>(), "")
		Map<String, BasicHeader> headerMap = new HashMap<String, BasicHeader>()
		request.getAllHeaders().each { header ->
			headerMap.put(header.getName(), header)
		}
		assertEquals("Bearer token", headerMap.get("Authorization").value)
		assertEquals("application/json", headerMap.get("Content-Type").value)
	}

	void testSubmitFeedItemFormat() {
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", "Feedback", new ArrayList<String>(), "")
		def feedback = new JsonSlurper().parseText(request.entity.content.text)
		assertNotNull(feedback)
	}

	void testSubmitFeedItemHasCorrectTopic() {
		String feedback = "Sample feedback."
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", feedback, topic, "")
		def requestBody = new JsonSlurper().parseText(request.entity.content.text).get("body").get("messageSegments")[0].get("text")
		assertTrue(requestBody.contains("#[${topic[0]}]"))
	}

	void testSubmitFeedItemHandlesMultipleTopics() {
		topic.add("sample topic")
		HttpUriRequest request = feedItemPoster.createFeedRequest("", "", "", topic, "")
		def requestBody = new JsonSlurper().parseText(request.entity.content.text).get("body").get("messageSegments")[0].get("text")
		assertTrue(requestBody.contains("#[${topic[0]}] #[${topic[1]}]"))
	}
	
	void testFeedItemHasAttachedLinkToThirdPartyLearningPage() {
		HttpUriRequest request = feedItemPoster.createFeedRequest("test", "test", "feedback", topic, "testurl")
		def linkAttachment = new JsonSlurper().parseText(request.entity.content.text).get("attachment")
		assertNotNull(linkAttachment)
	}
	
	void testAttachmentHasCorrectUrlAndUrlName() {
		HttpUriRequest request = feedItemPoster.createFeedRequest("test", "test", "feedback", topic, "testurl")
		def linkUrl = new JsonSlurper().parseText(request.entity.content.text).get("attachment").get("url")
		def linkName = new JsonSlurper().parseText(request.entity.content.text).get("attachment").get("urlName")
		assert linkUrl == "testurl"
		assert linkName == "testurl"
	}
	

//	void testForHttpClientCallInPostFeedItem() {
//		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 201, "test"))
//		when(defaultHttpClient.execute(any())).thenReturn(response)
//		feedItemPoster.postFeedItem("test","test","test","test", defaultHttpClient)
//		Mockito.verify(defaultHttpClient, Mockito.times(1))
//	}
	
	// will revisit this.  we need to figure out what the hell mockito is doing
//	void testHttpExceptionIsThrownWhenClientDoesNotReturn200() {
//		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 404, "test"))
//		when(failingHttpClient.execute(any())).thenReturn(response)
//		shouldFail(HttpException) {
//			feedItemPoster.postFeedItem("test", "test", "test", "test", failingHttpClient)		
//		}
//	}
}
