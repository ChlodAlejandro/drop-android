package net.chlod.android.drop;

import net.chlod.android.drop.enums.OutputFormat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class MockVideoFormat {

    public final UUID id;
    public final String acodec;
    public final String vcodec;

    public MockVideoFormat(String acodec, String vcodec) {
        id = UUID.randomUUID();
        this.acodec = acodec;
        this.vcodec = vcodec;
    }

    public boolean equals(MockVideoFormat b) {
        return b.id.equals(id);
    }

}

public class PathCheck {

    public static <T> String join(String delimeter, List<T> objects) {
        if (objects == null || objects.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < objects.size(); i++) {
            sb.append(objects.get(i).toString());

            // if not the last item
            if (i != objects.size() - 1) {
                sb.append(delimeter);
            }
        }

        return sb.toString();
    }

    public static boolean isAudioOnlyFormat(OutputFormat format) {
        return format == OutputFormat.MP3 || format == OutputFormat.WAV;
    }

    public String generateEncodingPathDescription(String output, int audioSource, int videoSource) {
        return String.format("[%s] A: %s, V: %s", output,
                audioSource == 0 ? "none" : String.valueOf(audioSource),
                videoSource == 0 ? "none" : String.valueOf(videoSource));
    }

    public String generateEncodingPathDescription(String output, String description) {
        return String.format("[%s] %s", output, description);
    }

    public String determineEncodingPath(MockVideoFormat audio, MockVideoFormat video, OutputFormat targetFormat) {
        boolean audioOnly = isAudioOnlyFormat(targetFormat);

        boolean audioHasAudio = !audio.acodec.equals("none");
        boolean audioHasVideo = !audio.vcodec.equals("none");
        boolean videoHasAudio = !video.acodec.equals("none");
        boolean videoHasVideo = !video.vcodec.equals("none");

        if (isAudioOnlyFormat(targetFormat)) { // no audio but audio only
            if (!audioHasAudio && !videoHasAudio) {
                return generateEncodingPathDescription("E", "No audio on both tracks for audio-only format.");
            } else if (audioHasAudio) {
                return generateEncodingPathDescription("A", 1, 0);
            } else { // video has audio
                return generateEncodingPathDescription("A", 2, 0);
            }
        } else {
            if (!audioHasVideo && !videoHasVideo) { // all outputs are audio
                if (audioHasAudio) {
                    return generateEncodingPathDescription("A", 1, 0);
                } else if (videoHasAudio) { // video has audio
                    return generateEncodingPathDescription("A", 2, 0);
                } else {
                    return generateEncodingPathDescription("E", "No streams.");
                }
            } else if (videoHasVideo) {
                if (audioHasAudio) {
                    return generateEncodingPathDescription("AV", 1, 2);
                } else if (videoHasAudio) { // video has audio
                    return generateEncodingPathDescription("AV", 2, 2);
                } else {
                    return generateEncodingPathDescription("V", 0, 2);
                }
            } else {
                if (audioHasAudio) {
                    return generateEncodingPathDescription("AV", 1, 1);
                } else if (videoHasAudio) { // video has audio
                    return generateEncodingPathDescription("AV", 2, 1);
                } else {
                    return generateEncodingPathDescription("V", 0, 1);
                }
            }
        }
    }

    @Test
    public void encodingPathCheck() {
        MockVideoFormat audio = new MockVideoFormat("aac", "none");
        MockVideoFormat video = new MockVideoFormat("none", "H.264");
        MockVideoFormat av = new MockVideoFormat("aac", "H.264");

        MockVideoFormat[] formats = new MockVideoFormat[] {
            audio, video, av
        };

        List<Boolean> results = new ArrayList<>();

        System.out.println("========================================");
        for (OutputFormat outputFormat : OutputFormat.values()) {
            for (int a = 0; a < formats.length; a++) {
                for (int b = a; b < formats.length; b++) {
                    final MockVideoFormat fA = formats[a];
                    final MockVideoFormat fB = formats[b];
                    System.out.printf("Input: [%s], [%s]\nOutput: %s\n",
                            join(", ", new ArrayList<String>() {{
                                if (!fA.acodec.equals("none")) add("Audio");
                                if (!fA.vcodec.equals("none")) add("Video");
                            }}),
                            join(", ", new ArrayList<String>() {{
                                if (!fB.acodec.equals("none")) add("Audio");
                                if (!fB.vcodec.equals("none")) add("Video");
                            }}),
                            outputFormat.toString());
                    System.out.printf("\tPath: %s\n\n", determineEncodingPath(fA, fB, outputFormat));
                }
            }
            System.out.println("========================================");
        }
    }

}
