package client.oddc.fla.com.restclient;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by yzharchuk on 8/17/2017.
 */


public class PostNotificationTask  extends AsyncTask<ArrayList<String>, Void, Void> {
    private String url;

    PostNotificationTask(String url) {
        this.url = url;
    }

    @Override
    protected Void doInBackground(ArrayList<String>... notification) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //HttpHeaders headers = new HttpHeaders();
            restTemplate.postForObject(url, notification[0], ArrayList.class);
        } catch (Exception e) {
            String message = e.getMessage();
            return null;
        }
        return null;
    }

    protected void onPostExecute() {
    }
}