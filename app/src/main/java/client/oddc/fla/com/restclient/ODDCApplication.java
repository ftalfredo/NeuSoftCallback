package client.oddc.fla.com.restclient;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by yzharchuk on 8/8/2017.
 *
 * This  class pulls resources from any location of our application,
 * so we could access strings, etc.
 * see also entry in AndroidManifest.xml. : <string>android:name="ODDCApplication"</string>>
 */

public class ODDCApplication extends Application
{
    private static Context context;

    public static Resources getResourcesStatic()
    {
        return context.getResources();
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.context = getApplicationContext();
    }
}
