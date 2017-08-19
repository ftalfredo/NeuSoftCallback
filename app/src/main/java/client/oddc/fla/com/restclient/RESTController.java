package client.oddc.fla.com.restclient;

import android.util.Log;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import client.oddc.fla.com.model.ContinuousDataCollection;
import client.oddc.fla.com.model.ODDCJob;
import client.oddc.fla.com.model.VideoCollection;
/**
 * Created by yzharchuk on 8/1/2017.
 */

public class RESTController
{
    private String base_url;

    public void setReturnStatus(HttpStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    public HttpStatus getReturnStatus() {
        return returnStatus;
    }

    private HttpStatus returnStatus;

    public RESTController( String baseUrl)
    {
        base_url = baseUrl;
    }

    public ArrayList<ODDCJob> getJobList()
    {
        ArrayList<ODDCJob> jobs = null;
        try
        {
            jobs = new GetJobsRequestTask(base_url + "jobs/all").execute().get();
        }
        catch(Exception e)
        {
            Log.e("GET Jobs failed: ", e.getMessage());
        }
        return jobs;
    }

    public HttpStatus postContinuousData(ContinuousDataCollection dataCollection)
    {
        HttpStatus status = null;
        try
        {
            status = new PostContinuousDataTask(base_url + "data/continuous").execute(dataCollection).get();
            int i = 0;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            Log.e("Err postContinuousData",e.getMessage());
        }
        finally
        {
            return status;
        }
    }

    public void postMediaData(VideoCollection videos)
    {
        new PostMediaDataTask(base_url + "data/video").execute(videos);
    }

    public void postStartEngineNotification(ArrayList<String> notification)
    {
        new PostNotificationTask(base_url + "notification/enginestart").execute(notification);
    }

    public void postStopEngineNotification(ArrayList<String> notification)
    {
        new PostNotificationTask(base_url + "notification/enginestop").execute(notification);
    }
}