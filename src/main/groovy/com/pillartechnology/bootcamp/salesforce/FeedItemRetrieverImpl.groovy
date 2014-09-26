package com.pillartechnology.bootcamp.salesforce

import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder


class FeedItemRetrieverImpl implements FeedItemRetriever {

	private static String GROUP_FEED_PLACEHOLDER = "#{groupId}"
	private static String GROUP_FEED_URI = "/services/data/v32.0/chatter/feeds/record/" + GROUP_FEED_PLACEHOLDER +"/feed-elements"
	
	List<FeedItem> findFeedItems(url, token, group, topic) {

		if (url == null || token == null || group == null || topic == null) {
			throw new IllegalArgumentException()
		}
		new ArrayList<FeedItem>()
	}

	protected HttpUriRequest createFeedItemHttpRequest(url, token, group, topic) {
		String uri = url + GROUP_FEED_URI.replace(GROUP_FEED_PLACEHOLDER, group);
		RequestBuilder.get().setUri(uri).build()
	}
}
