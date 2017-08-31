package client.oddc.fla.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by yzharchuk on 8/4/2017.
 */

public class Video
{
    @JsonProperty("cameraId")
    private String cameraId;
    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("size")
    private int size;
    @JsonProperty("videoResolution")
    private int videoResolution;
    @JsonProperty("frameRate")
    private int frameRate;
    @JsonProperty("codec")
    private String codec;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("FOV")
    private String FOV;
    @JsonProperty("videoTimeStamp")
    private Timestamp videoTimeStamp;

    @JsonProperty("videoBytes")
    private byte[] videoBytes;

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(int videoResolution) {
        this.videoResolution = videoResolution;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFOV() {
        return FOV;
    }

    public void setFOV(String FOV) {
        this.FOV = FOV;
    }

    public Timestamp getVideoTimeStamp() {
        return videoTimeStamp;
    }

    public void setVideoTimestamp(Timestamp videoTimeStamp) {
        this.videoTimeStamp = videoTimeStamp;
    }

    public byte[] getVideoBytes() {
        return videoBytes;
    }

    public void setVideoBytes(byte[] videoBytes) {
        this.videoBytes = videoBytes;
    }

    public static ArrayList<Video> createDummyVideoRecords(int numRecords)
    {
        ArrayList<Video> videos = new ArrayList<Video>();

        for (int i = 1; i <= numRecords; i++ )
        {
            Video video = new Video();
            video.cameraId = "camera" + i;
            video.fileName = "VIDEO" + (new Random().nextInt(100) + i) + ".mp4";
            video.size = i * 1000;
            video.videoResolution = 1080;
            video.frameRate = 24;
            video.codec = "K-LITE";
            video.direction = "F";
            video.FOV = "FOV Scene " + (new Random().nextInt(20) + i);
            video.videoTimeStamp = new Timestamp(new Date().getTime());;
            videos.add(video);
        }

        return videos;
    }

    public static Video createDummyVideo(byte[] videoBytes)
    {
        Video video = new Video();
        video.videoBytes = videoBytes;
        video.videoTimeStamp = new Timestamp(new Date().getTime());
        video.cameraId = "camera" + (new Random().nextInt(20));
        video.fileName = "VIDEO" + (new Random().nextInt(100)) + "_" + video.videoTimeStamp + ".mp4";
        video.size = videoBytes.length;
        video.videoResolution = 1080;
        video.frameRate = 24;
        video.codec = "K-LITE";
        video.direction = "F";
        video.FOV = "FOV Scene " + (new Random().nextInt(20));

        return video;
    }
}
