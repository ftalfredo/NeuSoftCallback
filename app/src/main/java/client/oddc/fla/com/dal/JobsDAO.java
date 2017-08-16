package client.oddc.fla.com.dal;


import java.util.ArrayList;

import client.oddc.fla.com.model.ODDCJob;
import client.oddc.fla.com.restclient.ODDCApplication;

/**
 * Created by yzharchuk on 8/8/2017.
 */

public class JobsDAO
{
    DBHelper dbHelper;

    public JobsDAO()
    {
        dbHelper = new DBHelper(ODDCApplication.getContext());
    }

    public void insertJobs(ArrayList<ODDCJob> jobs)
    {
        dbHelper.insertJobs(jobs);
    }
    public ODDCJob getJob(String jobId)
    {
        ODDCJob job = dbHelper.getJob(jobId);
        return job;
    }
    public ArrayList<ODDCJob> getJobs(ArrayList<String> jobIDs)
    {
        ArrayList<ODDCJob> jobs = dbHelper.getJobs(jobIDs);
        return jobs;
    }




}
