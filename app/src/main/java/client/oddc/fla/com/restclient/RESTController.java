package client.oddc.fla.com.restclient;

import android.util.Log;

import java.util.ArrayList;

import client.oddc.fla.com.model.ContinuousDataCollection;
import client.oddc.fla.com.model.ODDCJob;
import client.oddc.fla.com.model.VideoCollection;
/**
 * Created by yzharchuk on 8/1/2017.
 */

public class RESTController
{
    private String base_url;

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

    public void postContinuousData(ContinuousDataCollection dataCollection)
    {
        new PostContinuousDataTask(base_url + "data/continuous").execute(dataCollection);
    }

    public void postMediaData(VideoCollection videos) {
        new PostMediaDataTask(base_url + "data/video").execute(videos);
    }
}