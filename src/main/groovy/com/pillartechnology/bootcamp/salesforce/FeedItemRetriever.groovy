package com.pillartechnology.bootcamp.salesforce
class InvalidInputException extends Exception{}

class FeedItemRetriever {
    
    void findFeedItems() {
       throw new InvalidInputException()
    }

}
