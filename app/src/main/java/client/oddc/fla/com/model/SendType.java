/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.util.HashMap;
import java.util.Map;

public enum SendType
{
    NOT_SENDING (1),
    SEND_EVENT(2),
    SEND_EVENT_AND_MEDIA(3);

    private int value;
    private static Map map = new HashMap<>();

    private SendType(int value) {
        this.value = value;
    }

    static {
        for (SendType type : SendType.values()) {
            map.put(type.value, type);
        }
    }

    public static SendType valueOf(int type) {
        return (SendType)map.get(type);
    }

    public int getValue() {
        return value;
    }
}