package client.oddc.fla.com.restclient;

import android.os.AsyncTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yzharchuk on 8/29/2017.
 */

class PostSelectiveCheckTask extends AsyncTask<Void, Void, Boolean>
{
    private String url;
    private HttpStatus returnStatus;

    public PostSelectiveCheckTask(String url)
    {
        this.url = url;
    }

    @Override
    protected Boolean doInBackground(Void... data)
    {
        boolean ret = false;
        RestTemplate restTemplate = new RestTemplate();
        try
        {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //HttpHeaders headers = new HttpHeaders();
            //HttpEntity<Void> request = new HttpEntity<>(data);
            ResponseEntity<Boolean> result = restTemplate.exchange(url, HttpMethod.POST, null, Boolean.class);
            ret = result.getBody();
        }
        catch (Exception e)
        {
            String message = e.getMessage();
            return null;
        }
        return ret;
    }

    @Override
    protected void onPostExecute(Boolean ret)
    {
    }
}
