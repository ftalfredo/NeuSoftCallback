package client.oddc.fla.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yzharchuk on 8/29/2017.
 */

public class Notification
{
    @JsonProperty("envelope")
    private Envelope envelope;
    @JsonProperty("message")
    private String message;

    public Notification() {}

    public Notification(Envelope envelope, String message) {
        this.envelope = envelope;
        this.message = message;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public String getMessage() {
        return "***CLIENT*** " + message;
    }

    public void setMessage(String message) {
        this.message =  message;
    }
}
