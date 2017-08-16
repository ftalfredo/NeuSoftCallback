package client.oddc.fla.com.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yzharchuk on 8/11/2017.
 */

public enum JobStatus
{
    STATUS1 (1),
    STATUS2 (2),
    STATUS3 (3);


    private int value;
    private static Map map = new HashMap<>();

    private JobStatus(int value) {
        this.value = value;
    }

    static {
        for (JobStatus jobStatus : JobStatus.values()) {
            map.put(jobStatus.value, jobStatus);
        }
    }

    public static JobStatus valueOf(int jobStatus) {
        return (JobStatus)map.get(jobStatus);
    }

    public int getValue() {
        return value;
    }
}