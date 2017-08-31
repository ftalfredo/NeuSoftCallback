package client.oddc.fla.com.restclient;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import client.oddc.fla.com.model.ODDCJob;
import client.oddc.fla.com.model.ODDCJobCollection;

/**
 * Created by yzharchuk on 8/1/2017.
 */
public class GetJobsRequestTask extends AsyncTask<String, Void, ArrayList<ODDCJob>>
{
    private String url;

    GetJobsRequestTask(String url)
    {
        this.url = url;
    }

    @Override
    protected ArrayList<ODDCJob> doInBackground(String... uri)
    {
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<ODDCJob> jobs = null;

        try
        {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();

            //TODO: This is potentially place for our Envelope data

            //headers.set("APIKey", "5567GGH67225HYVGG");
            //String auth = "Jack" + ":" + "Janes";
            //String encodeAuth = Base64.encodeToString(auth.getBytes(), Base64.DEFAULT);
            //String authHeader = "Basic" + new String(encodeAuth);
            //headers.set("Authorization", authHeader);

            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<ODDCJobCollection> response = restTemplate.exchange(url, HttpMethod.GET, entity, ODDCJobCollection.class);
            jobs = response.getBody().getJobs();
        }
        catch (Exception e)
        {
            Log.e("GET Jobs failed: ", e.getMessage());
        }
        return jobs;
    }

    protected void onPostExecute(ArrayList<ODDCJob> jobs)
    {
    }
}

/*********************************** Alternative class implementation ************************************************/

    /*
    public class GetJobsRequestTask extends AsyncTask<String, Void, ODDCJobCollection>
    {
        @Override
        protected ODDCJobCollection doInBackground(String... uri)
        {
            try
            {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ODDCJobCollection jobs = restTemplate.getForObject(uri[0], ODDCJobCollection.class);
                return jobs;
            }
            catch (Exception e)
            {
                String message = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ODDCJobCollection jobs)
        {
            String id = jobs.getJobs().get(0).getId();
        }
    }
    */






