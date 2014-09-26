package com.pillartechnology.bootcamp.salesforce

import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder


class FeedItemRetrieverImpl implements FeedItemRetriever {

	List<FeedItem> findFeedItems(url, token, group, topic) {

		if (url == null || token == null || group == null || topic == null) {
			throw new IllegalArgumentException()
		}
		new ArrayList<FeedItem>()
	}

	protected HttpUriRequest createFeedItemHttpRequest(url, token, group, topic) {
		 RequestBuilder.get().setUri(url).build()
	}
}
