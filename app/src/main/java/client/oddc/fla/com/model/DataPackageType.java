package client.oddc.fla.com.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yzharchuk on 8/29/2017.
 */

public enum DataPackageType
{
    CONTINUOUS(0),
    EVENT(1),
    SELECTIVE(2);

    private int value;
    private static Map<Object, Object> map = new HashMap<>();

    private DataPackageType(int value) {
        this.value = value;
    }

    static {
        for (DataPackageType type : DataPackageType.values()) {
            map.put(type.value, type);
        }
    }

    public static DataPackageType valueOf(int type) {
        return (DataPackageType)map.get(type);
    }

    public int getValue() {
        return value;
    }
}