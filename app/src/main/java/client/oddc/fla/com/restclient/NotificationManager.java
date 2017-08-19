package client.oddc.fla.com.restclient;

import java.util.ArrayList;

/**
 * Created by yzharchuk on 8/17/2017.
 */

public class NotificationManager
{
    private final String baseUrl = ODDCApplication.getResourcesStatic().getString(R.string.base_url);
    private RESTController restController;
    private ArrayList<String> notification;

    public NotificationManager()
    {
        restController = new RESTController(baseUrl);
        notification = new ArrayList<String>();
    }

    private void postStartEngineNotification(ArrayList<String> notification)
    {
        restController.postStartEngineNotification(notification);
    }
    private void postStopEngineNotification(ArrayList<String> notification)
    {
        restController.postStopEngineNotification(notification);
    }
}
