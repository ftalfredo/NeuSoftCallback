package client.oddc.fla.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yzharchuk on 8/4/2017.
 */

//wrapper class for REST
public class VideoCollection implements Serializable {
    public ArrayList<Video> videos;

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public VideoCollection(ArrayList<Video> videos)
    {
        this.videos = videos;
    }
}
