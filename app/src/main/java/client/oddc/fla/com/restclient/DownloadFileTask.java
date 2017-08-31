package client.oddc.fla.com.restclient;

import android.os.AsyncTask;

import client.oddc.fla.com.utilities.Utilities;

/**
 * Created by yzharchuk on 8/29/2017.
 */

public class DownloadFileTask extends AsyncTask<String, Void, byte[]>
{
    protected byte[] doInBackground(String... urls)
    {
        return Utilities.downloadFile(urls[0]);
    }

    protected void onPostExecute(byte[] result)
    {

    }
}
