class InvalidPostRequestException extends Exception{}

class FeedItemPoster {

    void postFeedItem() {
        throw new InvalidPostRequestException()
    }
}
