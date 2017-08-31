package client.oddc.fla.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by yzharchuk on 8/29/2017.
 */

public class DataPackage
{
    @JsonProperty("packageType")
    private DataPackageType packageType;
    @JsonProperty("continuousData")
    private ArrayList<ContinuousData> continuousData;
    @JsonProperty("videos")
    private ArrayList<Video> videos;
    @JsonProperty("envelope")
    private Envelope envelope;

    public DataPackage()
    {
    }

    public DataPackage(DataPackageType packageType, ArrayList<ContinuousData> continuousData, ArrayList<Video> videos, Envelope envelope) {
        this.packageType = packageType;
        this.continuousData = continuousData;
        this.videos = videos;
        this.envelope = envelope;
    }

    public static DataPackage createDummyDataPackage(Envelope envelope, byte[] videoBytes, DataPackageType dataPackageType)
    {
        DataPackage dataPackage = new DataPackage();

        if(dataPackageType == DataPackageType.EVENT || dataPackageType == DataPackageType.SELECTIVE)
        {
            int videoRecords = (int) (Math.random() * (5)) + 1;
            ArrayList<Video> videos = new ArrayList<Video>(videoRecords);

            for (int i = 0; i < videoRecords; i++)
            {
                Video video = Video.createDummyVideo(videoBytes);
                videos.add(video);
            }

            dataPackage.setVideos(videos);
        }

        int dataRecords = (int) (Math.random() * (150)) + 50;
        ArrayList<ContinuousData> continuousData = new ArrayList<ContinuousData>(dataRecords);

        //for (int i = 0; i < dataRecords; i++) continuousData.add(ContinuousData.createDummyContinuousData());

        dataPackage.setContinuousData(continuousData);
        dataPackage.setPackageType(dataPackageType);
        dataPackage.setEnvelope(envelope);

        return dataPackage;
    }

    public DataPackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(DataPackageType packageType) {
        this.packageType = packageType;
    }

    public ArrayList<ContinuousData> getContinuousData() {
        return continuousData;
    }

    public void setContinuousData(ArrayList<ContinuousData> continuousData) {
        this.continuousData = continuousData;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }
}
