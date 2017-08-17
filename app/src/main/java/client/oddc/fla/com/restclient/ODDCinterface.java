package client.oddc.fla.com.restclient;

import java.util.List;

import client.oddc.fla.com.model.ContinuousData;


public interface ODDCinterface {
    public boolean onContinuousData(ContinuousData data);
    public List<String> getContinuousLog();
    public List<String> getEventLog();
    public List<String> getOnDemandLog();
    public boolean ok2Startup();
    public boolean reqShutdown();
}