package client.oddc.fla.com.restclient;

/*
  Created by yzharchuk on 8/2/2017.
 */
import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import client.oddc.fla.com.model.ContinuousDataCollection;


public class PostContinuousDataTask  extends AsyncTask<ContinuousDataCollection, Void, Void> {
    private String url;

    PostContinuousDataTask(String url) {
        this.url = url;
    }

    @Override
    protected Void doInBackground(ContinuousDataCollection... dataCollection) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //HttpHeaders headers = new HttpHeaders();
            restTemplate.postForObject(url, dataCollection[0], ContinuousDataCollection.class);
        } catch (Exception e) {
            String message = e.getMessage();
            return null;
        }
        return null;
    }

    protected void onPostExecute() {
    }
}