package net.chlod.android.drop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import com.sapher.youtubedl.mapper.VideoInfo;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class must be identical to the actual VideoInfoCollection, but built
 * to use sapher's youtubedl-java library instead, in order to test download
 * capabilities (since ytdl-android is a fork of youtubedl-java).
 */
class VideoInfoCollection {

    public static VideoInfoCollection getInfo(String url) throws YoutubeDLException {
        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.setOption("dump-json");
        request.setOption("no-playlist");
        YoutubeDLResponse response = YoutubeDL.execute(request);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ArrayList<VideoInfo> infoList = new ArrayList<>();
            for (String s : response.getOut().split("\n")) {
                infoList.add(objectMapper.readValue(s, VideoInfo.class));
            }
            return new VideoInfoCollection(infoList);
        } catch (IOException var6) {
            throw new YoutubeDLException("Unable to parse video information: " + var6.getMessage());
        }
    }

    public enum VICType {
        SINGLE,
        MULTIPLE
    }

    public VICType type;
    public List<VideoInfo> infoList;

    private VideoInfoCollection(List<VideoInfo> infoList) {
        this.infoList = infoList;
        type = infoList.size() > 1 ? VICType.MULTIPLE : VICType.SINGLE;
    }

}

public class YouTubeTests {

    private String testVideoURL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private String testPlaylistURL = "https://www.youtube.com/playlist?list=PLwjeCZFBHNJp_UvUl6N_XiLiuB95kjXr4";

    @Test
    public void videoInfoOutput() throws YoutubeDLException {
        System.out.println("Grabbing video info (this may take a while)...");
        System.out.println(new Gson().toJson(VideoInfoCollection.getInfo(testVideoURL)));
        System.out.println("Success.");
    }

    @Test
    public void simulateVideoDownload() throws YoutubeDLException {
        System.out.println("Running download simulations (this may take a while)...");
        YoutubeDLRequest request = new YoutubeDLRequest(testVideoURL);
        request.setOption("simulate");
        request.setOption("verbose");
        request.setOption("all-formats");
        request.setOption("all-subs");
        YoutubeDLResponse exec = YoutubeDL.execute(request);
        System.out.println("Command:\t\t" + exec.getCommand());
        System.out.println("Directory:\t\t" + exec.getDirectory());
        System.out.println("Elapsed:\t\t" + exec.getElapsedTime());
        System.out.println("\n" + exec.getOut());
        System.out.println("Success.");
    }

    @Test
    public void assertVideoInfo() throws YoutubeDLException {
        System.out.println("Asserting video info (this may take a while)...");
        VideoInfoCollection infoCollection = VideoInfoCollection.getInfo(testVideoURL);

        assertEquals(infoCollection.type, VideoInfoCollection.VICType.SINGLE);
        assertEquals(infoCollection.infoList.size(), 1);

        VideoInfo info = infoCollection.infoList.get(0);

        assertEquals("dQw4w9WgXcQ", info.id);
        assertEquals("Rick Astley - Never Gonna Give You Up (Video)", info.fulltitle);
        assertEquals("20091024", info.uploadDate);
        assertEquals(212, info.duration);
        assertEquals("RickAstleyVEVO", info.uploader);
        System.out.println("Success.");
    }

    @Test
    public void playlistInfoOutput() throws YoutubeDLException {
        System.out.println("Grabbing playlist info (this may take a while)...");

        VideoInfoCollection infoCollection = VideoInfoCollection.getInfo(testPlaylistURL);

        assertEquals(VideoInfoCollection.VICType.MULTIPLE, infoCollection.type);
        assertEquals(5, infoCollection.infoList.size());

        for (VideoInfo info : infoCollection.infoList)
            System.out.println(info.toString());
        System.out.println("Success.");
    }

    @Test
    public void simulatePlaylistDownload() throws YoutubeDLException {
        System.out.println("Running download simulations (this may take a while)...");
        YoutubeDLRequest request = new YoutubeDLRequest(testPlaylistURL);
        request.setOption("simulate");
        request.setOption("verbose");
        request.setOption("all-formats");
        request.setOption("all-subs");
        YoutubeDLResponse exec = YoutubeDL.execute(request);
        System.out.println("Command:\t\t" + exec.getCommand());
        System.out.println("Directory:\t\t" + exec.getDirectory());
        System.out.println("Elapsed:\t\t" + exec.getElapsedTime());
        System.out.println("\n" + exec.getOut());
        System.out.println("Success.");
    }


//    public void assertPlaylistInfo() throws YoutubeDLException {
//        System.out.println("Asserting video info (this may take a while)...");
//        VideoInfo info = YoutubeDL.getVideoInfo(testVideoURL);
//        assertEquals("dQw4w9WgXcQ", info.id);
//        assertEquals("Rick Astley - Never Gonna Give You Up (Video)", info.fulltitle);
//        assertEquals("20091024", info.uploadDate);
//        assertEquals(212, info.duration);
//        assertEquals("RickAstleyVEVO", info.uploader);
//        System.out.println("Success.");
//    }

}
