package com.pillartechnology.bootcamp.salesforce
interface FeedItemRetriever {
	List<FeedItem> findFeedItems(url, token, topic, httpClient)
}
