package client.oddc.fla.com.restclient;

/**
 * Created by yzharchuk on 8/8/2017.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import client.oddc.fla.com.model.VideoCollection;



public class PostMediaDataTask extends AsyncTask<VideoCollection, Void, Void>
{
    private String url;

    PostMediaDataTask(String url)
    {
        this.url = url;
    }

    @Override
    protected Void doInBackground(VideoCollection... data)
    {
        RestTemplate restTemplate = new RestTemplate();
        try
        {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //HttpHeaders headers = new HttpHeaders();
            restTemplate.postForObject(url, data[0], VideoCollection.class);
        }
        catch (Exception e)
        {
            Log.e("POST MediaData failed: ", e.getMessage());
        }
        return null;
    }

    protected void onPostExecute()
    {
    }
}