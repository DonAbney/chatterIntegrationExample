package com.pillartechnology.bootcamp.salesforce

import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder


class FeedItemRetrieverImpl implements FeedItemRetriever {

	private static String GROUP_FEED_URI = "/services/data/v31.0/connect/topics?exactMatch=true&q="
	
	List<FeedItem> findFeedItems(url, token, topic) {

		if (url == null || token == null || topic == null) {
			throw new IllegalArgumentException()
		}
		new ArrayList<FeedItem>()
	}

	protected HttpUriRequest createFeedItemHttpRequest(url, token, topic) {
		RequestBuilder.get().setUri(url + GROUP_FEED_URI + topic).build()
	}
}
