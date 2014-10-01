package com.pillartechnology.bootcamp.salesforce

import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder


class FeedItemRetrieverImpl implements FeedItemRetriever {

	private static String TOPIC_ID_URI = "/services/data/v31.0/connect/topics?exactMatch=true&q="
	private static String TOPIC_MESSAGES_URI = "/services/data/v31.0/chatter/feeds/topics/0TOo00000008QB7GAM/feed-items"
	private static String TOPIC_PAGE_URL = "_ui/core/chatter/topics/TopicPage?name="
	
	List<FeedItem> findFeedItems(url, token, topic) {

		if (url == null || token == null || topic == null) {
			throw new IllegalArgumentException()
		}
		new ArrayList<FeedItem>()
	}

	protected HttpUriRequest createFeedItemHttpRequest(url, token, topic) {
		RequestBuilder.get().setUri(url + GROUP_FEED_URI + topic).build()
	}
	
	//get topic id from TOPIC_ID_URI
	//get messages from topic id
	//create feed items from messages
	//add web url for topic page
}
