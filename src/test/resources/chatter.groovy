import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader

//real values
def clientId = "3MVG9xOCXq4ID1uGkfqs8olvegeBTR34Tlez7JOI2BgmGRUQ_ajQlWhLAF3I0HRzRNzJrnR6SjAiFkZBSXhZY"
def clientSecret = "7554976980304999635"
//test values
def grantType = "password"
def username = "ksmith2@pillartechnology.com"
def password = "agile123"

def auth = { ->
    def entity = MultipartEntityBuilder.create()
            .addTextBody("client_id", clientId)
            .addTextBody("client_secret", clientSecret)
            .addTextBody("grant_type", grantType)
            .addTextBody("username", username)
            .addTextBody("password", password)
            .build()

    def request = RequestBuilder.post()
            .setEntity(entity)
            .setUri("https://login.salesforce.com/services/oauth2/token")
            .build()

    def response = HttpClients.createDefault().execute(request)

    def data =  new JsonSlurper().parseText(response.entity.content.text)

    [data.instance_url, data.access_token]
}

def loggedInUser = { url, token ->
    def request = RequestBuilder.get()
            .addHeader(new BasicHeader("Authorization", "Bearer ${token}"))
            .setUri("${url}/services/data/v31.0/chatter/users/me")
            .build()

    def response = HttpClients.createDefault().execute(request)

    new JsonSlurper().parseText(response.entity.content.text)
}

def postMessage = { url, token, id ->

    def output = new JsonBuilder()
    output {
        body {
            messageSegments([[type: "Text", text: "Something cool! #holler"]])
        }
        feedElementType("FeedItem")
        subjectId("me")
    }

    def request = RequestBuilder.post()
        .addHeader(new BasicHeader("Authorization", "Bearer ${token}"))
        .addHeader("Content-Type", "application/json")
        .setUri("${url}/services/data/v31.0/chatter/feed-elements")
        .setEntity(new StringEntity(output.toPrettyString()))
        .build()

    def response = HttpClients.createDefault().execute(request)

    new JsonSlurper().parseText(response.entity.content.text)
}


def (url, token) = auth()
println "${url} --> ${token}"
def user = loggedInUser(url, token)

postMessage(url, token, user.id)

