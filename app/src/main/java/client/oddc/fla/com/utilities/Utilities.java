package client.oddc.fla.com.utilities;

/**
 * Created by yzharchuk on 8/15/2017.
 */

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import client.oddc.fla.com.model.Video;

public class Utilities
{
    public static String generateUUIDString()
    {
        return UUID.randomUUID().toString();
    }

    public static boolean saveVideoFile(Video video, String path)
    {
        boolean success = false;
        String fileName = path + File.separator + video.getFileName();

        try
        {
            FileOutputStream out = new FileOutputStream(fileName);
            byte[] bytes = video.getVideoBytes();
            out.write(bytes);
            out.close();
            success = true;
        }
        catch (Exception e)
        {
            Log.d("saveVideoFile()", "File '" + fileName + "' saved.");
        }

        return success;
    }

    public static byte [] downloadFile(String fileURL)
    {
        byte[] bytes = null;

        try
        {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();

                if (disposition != null)
                {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");

                    if (index > 0)
                        fileName = disposition.substring(index + 10, disposition.length() - 1);
                }
                else
                {
                    // extracts file name from URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
                }

                Log.d("downloadFile", "Content-Type = " + contentType);
                Log.d("downloadFile","Content-Disposition = " + disposition);
                Log.d("downloadFile","Content-Length = " + contentLength);
                Log.d("downloadFile","fileName = " + fileName);

                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
              //  bytes = IOUtils.toByteArray(inputStream);    //yz missing IOUtils library
                Log.d("downloadFile","File downloaded");
            }
            else
            {
                Log.d("downloadFile","No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        }
        catch (Exception e)
        {
            Log.d("downloadFile",e.getMessage());
        }

        return bytes;
    }
}