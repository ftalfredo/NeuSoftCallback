package client.oddc.fla.com.restclient;

import java.util.ArrayList;

import client.oddc.fla.com.dal.JobsDAO;
import client.oddc.fla.com.model.ODDCJob;

/**
 * Created by yzharchuk on 8/8/2017.
 */

public class JobManager
{
    private final String baseUrl = ODDCApplication.getResourcesStatic().getString(R.string.base_url);
    private RESTController restController;
    private ArrayList<ODDCJob> jobs;

    public JobManager()
    {
        restController = new RESTController(baseUrl);
        jobs = new ArrayList<ODDCJob>();
    }

    public ArrayList<ODDCJob> getJobsFromServer()
    {
        ArrayList<ODDCJob> jobs = restController.getJobList();
        return jobs;
    }
    public void storeJobsIntoSQLite(ArrayList<ODDCJob> jobs)
    {
        JobsDAO jobsDAO = new JobsDAO();
        jobsDAO.insertJobs(jobs);
    }

    public void processJobs()
    {
        ArrayList<ODDCJob> jobs = getJobsFromServer();
        storeJobsIntoSQLite(jobs);
    }
}
