/**
 * Created by yzharchuk on 8/2/2017.
 *
 * This Enum implementation allows getting integer values w/o pain in the neck
 */

package client.oddc.fla.com.model;

import java.util.HashMap;
import java.util.Map;

public enum EventType
{
    FCW (1),
    LDW (2),
    GSHOCK (3),
    MEDIA (4);

    private int value;
    private static Map map = new HashMap<>();

    private EventType(int value) {
        this.value = value;
    }

    static {
        for (EventType eventType : EventType.values()) {
            map.put(eventType.value, eventType);
        }
    }

    public static EventType valueOf(int eventType) {
        return (EventType)map.get(eventType);
    }

    public int getValue() {
        return value;
    }
}