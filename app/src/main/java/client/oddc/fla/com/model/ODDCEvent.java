/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

public class ODDCEvent
{
    EventType eventType;
    SendType sendType;

    public EventType getEventType()
    {
        return eventType;
    }
    public void setEventType(EventType eventType)
    {
        this.eventType = eventType;
    }
    public SendType getSendType()
    {
        return sendType;
    }
    public void setSendType(SendType sendType)
    {
        this.sendType = sendType;
    }
}
