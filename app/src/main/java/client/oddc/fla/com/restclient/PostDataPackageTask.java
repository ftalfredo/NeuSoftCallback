package client.oddc.fla.com.restclient;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import client.oddc.fla.com.model.DataPackage;

/**
 * Created by yzharchuk on 8/29/2017.
 */

class PostDataPackageTask extends AsyncTask<DataPackage, Void, HttpStatus>
{
    private String url;
    private HttpStatus returnStatus;

    public PostDataPackageTask(String url)
    {
        this.url = url;
    }

    @Override
    protected HttpStatus doInBackground(DataPackage... data)
    {
        RestTemplate restTemplate = new RestTemplate();
        try
        {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //HttpHeaders headers = new HttpHeaders();
            HttpEntity<DataPackage> request = new HttpEntity<>(data[0]);
            ResponseEntity<DataPackage> result = restTemplate.exchange(url, HttpMethod.POST, request, DataPackage.class);
            returnStatus = result.getStatusCode();
        }
        catch (Exception e)
        {
            String message = e.getMessage();
            return returnStatus;
        }
        return returnStatus;
    }

    @Override
    protected void onPostExecute(HttpStatus status)
    {
    }
}