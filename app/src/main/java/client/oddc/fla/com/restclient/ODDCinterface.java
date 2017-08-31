package client.oddc.fla.com.restclient;

import java.util.ArrayList;

import client.oddc.fla.com.model.ContinuousData;


public interface ODDCinterface {
    public boolean onContinuousData(ContinuousData data);
    public ArrayList<PlaybackList> getPlaybackList();
    public boolean ok2Startup();
    public boolean reqShutdown();
}