package net.chlod.android.drop.objects;

import androidx.annotation.Nullable;

import net.chlod.android.drop.enums.RequestOptionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
public class RequestOption {

    public static final List<RequestOptionType> multipleUsageOptions = new ArrayList<RequestOptionType>(){{
        add(RequestOptionType.WORKAROUND_ADD_HEADER);
    }};

    public static final Map<RequestOptionType, RequestOption> options =
        new HashMap<RequestOptionType, RequestOption>(){{
            // GENERAL OPTIONS
            put(RequestOptionType.OUTPUT, new RequestOption(
                RequestOptionType.OUTPUT,
                "--output",
                "Output filename template, see the \"OUTPUT TEMPLATE\" for all the info",
                "TEMPLATE"));
            // VIDEO OPTIONS
            put(RequestOptionType.VIDEO_PLAYLIST_START, new RequestOption(
                    RequestOptionType.VIDEO_PLAYLIST_START,
                    "--playlist-start",
                    "Playlist video to start at (default is 1)",
                    "NUMBER"));
            put(RequestOptionType.VIDEO_PLAYLIST_END, new RequestOption(
                    RequestOptionType.VIDEO_PLAYLIST_END,
                    "--playlist-end",
                    "Playlist video to end at (default is last)",
                    "NUMBER"));
            put(RequestOptionType.VIDEO_PLAYLIST_ITEMS, new RequestOption(
                    RequestOptionType.VIDEO_PLAYLIST_START,
                    "--playlist-items",
                    "Playlist video items to download. Specify indices of the videos in the playlist separated by commas like: \"--playlist-items 1,2,5,8\" if you want to download videos indexed 1, 2, 5, 8 in the playlist. You can specify range: \"--playlist-items 1-3,7,10-13\", it will download the videos at index 1, 2, 3, 7, 10, 11, 12 and 13.",
                    "ITEM_SPEC"));
            put(RequestOptionType.VIDEO_MATCH_TITLE, new RequestOption(
                    RequestOptionType.VIDEO_MATCH_TITLE,
                    "--match-title",
                    "Download only matching titles (regex or caseless sub-string)",
                    "REGEX"));
            put(RequestOptionType.VIDEO_REJECT_TITLE, new RequestOption(
                    RequestOptionType.VIDEO_REJECT_TITLE,
                    "--reject-title",
                    "Skip download for matching titles (regex or caseless sub-string)",
                    "REGEX"));
            put(RequestOptionType.VIDEO_MAX_DOWNLOADS, new RequestOption(
                    RequestOptionType.VIDEO_MAX_DOWNLOADS,
                    "--max-downloads",
                    "Abort after downloading NUMBER files",
                    "NUMBER"));
            put(RequestOptionType.VIDEO_MIN_FILESIZE, new RequestOption(
                    RequestOptionType.VIDEO_MIN_FILESIZE,
                    "--min-filesize",
                    "Do not download any videos smaller than SIZE (e.g. 50k or 44.6m)",
                    "SIZE"));
            put(RequestOptionType.VIDEO_MAX_FILESIZE, new RequestOption(
                    RequestOptionType.VIDEO_MAX_FILESIZE,
                    "--max-filesize",
                    "Do not download any videos larger than SIZE (e.g. 50k or 44.6m)",
                    "SIZE"));
            put(RequestOptionType.VIDEO_DATE, new RequestOption(
                    RequestOptionType.VIDEO_DATE,
                    "--date",
                    "Download only videos uploaded in this date",
                    "DATE"));
            put(RequestOptionType.VIDEO_DATE_BEFORE, new RequestOption(
                    RequestOptionType.VIDEO_DATE_BEFORE,
                    "--datebefore",
                    "Download only videos uploaded on or before this date (i.e. inclusive)",
                    "DATE"));
            put(RequestOptionType.VIDEO_DATE_AFTER, new RequestOption(
                    RequestOptionType.VIDEO_DATE_AFTER,
                    "--dateafter",
                    "Download only videos uploaded on or after this date (i.e. inclusive)",
                    "DATE"));
            put(RequestOptionType.VIDEO_MIN_VIEWS, new RequestOption(
                    RequestOptionType.VIDEO_MIN_VIEWS,
                    "--min-views",
                    "Do not download any videos with less than COUNT views",
                    "COUNT"));
            put(RequestOptionType.VIDEO_MAX_VIEWS, new RequestOption(
                    RequestOptionType.VIDEO_MAX_VIEWS,
                    "--max-views",
                    "Do not download any videos with more than COUNT views",
                    "COUNT"));
            put(RequestOptionType.VIDEO_MATCH_FILTER, new RequestOption(
                    RequestOptionType.VIDEO_MATCH_FILTER,
                    "--match-filter",
                    "FILTER",
                    "Generic video filter. Specify any key (see the \"OUTPUT TEMPLATE\" for a list of available keys) to match if the key is present, !key to check if the key is not present, key > NUMBER (like \"comment_count > 12\", also works with >=, <, <=, !=, =) to compare against a number, key = 'LITERAL' (like \"uploader = 'Mike Smith'\", also works with !=) to match against a string literal and & to require multiple matches. Values which are not known are excluded unless you put a question mark (?) after the operator. For example, to only match videos that have been liked more than 100 times and disliked less than 50 times (or the dislike functionality is not available at the given service), but who also have a description, use --match-filter \"like_count > 100 & dislike_count <? 50 & description\"."));
            put(RequestOptionType.VIDEO_NO_PLAYLIST, new RequestOption(
                    RequestOptionType.VIDEO_NO_PLAYLIST,
                    "--no-playlist",
                    "Download only the video, if the URL refers to a video and a playlist.",
                    null));
            put(RequestOptionType.VIDEO_YES_PLAYLIST, new RequestOption(
                    RequestOptionType.VIDEO_YES_PLAYLIST,
                    "--yes-playlist",
                    "Download the playlist, if the URL refers to a video and a playlist.",
                    null));
            put(RequestOptionType.VIDEO_AGE_LIMIT, new RequestOption(
                    RequestOptionType.VIDEO_AGE_LIMIT,
                    "--age-limit",
                    "Download only videos suitable for the given age",
                    "YEARS"));
            put(RequestOptionType.VIDEO_DOWNLOAD_ARCHIVE, new RequestOption(
                    RequestOptionType.VIDEO_DOWNLOAD_ARCHIVE,
                    "--download-archive",
                    "Download only videos not listed in the archive file. Record the IDs of all downloaded videos in it.",
                    "FILE"));
            // DOWNLOAD OPTIONS
            put(RequestOptionType.DOWNLOAD_RATE, new RequestOption(
                    RequestOptionType.DOWNLOAD_RATE,
                    "--limit-rate",
                    "Maximum download rate in bytes per second (e.g. 50K or 4.2M)",
                    "RATE"));
            put(RequestOptionType.DOWNLOAD_RETRIES, new RequestOption(
                    RequestOptionType.DOWNLOAD_RETRIES,
                    "--retries",
                    "Number of retries (default is 10), or \"infinite\".",
                    "RETRIES"));
            put(RequestOptionType.DOWNLOAD_FRAGMENT_RETRIES, new RequestOption(
                    RequestOptionType.DOWNLOAD_FRAGMENT_RETRIES,
                    "--fragment-retries",
                    "Number of retries for a fragment (default is 10), or \"infinite\" (DASH, hlsnative and ISM)",
                    "RETRIES"));
            put(RequestOptionType.DOWNLOAD_SKIP_UNAVAILABLE_FRAGMENTS, new RequestOption(
                    RequestOptionType.DOWNLOAD_SKIP_UNAVAILABLE_FRAGMENTS,
                    "--skip-unavailable-fragments",
                    "Skip unavailable fragments (DASH, hlsnative and ISM)",
                    null));
            put(RequestOptionType.DOWNLOAD_ABORT_ON_UNAVAILABLE_FRAGMENT, new RequestOption(
                    RequestOptionType.DOWNLOAD_ABORT_ON_UNAVAILABLE_FRAGMENT,
                    "--abort-on-unavailable-fragment",
                    "Abort downloading when some fragment is not available",
                    null));
            put(RequestOptionType.DOWNLOAD_KEEP_FRAGMENTS, new RequestOption(
                    RequestOptionType.DOWNLOAD_KEEP_FRAGMENTS,
                    "--keep-fragments",
                    "Keep downloaded fragments on disk after downloading is finished; fragments are erased by default",
                    null));
            put(RequestOptionType.DOWNLOAD_BUFFER_SIZE, new RequestOption(
                    RequestOptionType.DOWNLOAD_BUFFER_SIZE,
                    "--buffer-size",
                    "Size of download buffer (e.g. 1024 or 16K) (default is 1024)",
                    "SIZE"));
            put(RequestOptionType.DOWNLOAD_NO_BUFFER_RESIZE, new RequestOption(
                    RequestOptionType.DOWNLOAD_NO_BUFFER_RESIZE,
                    "--no-resize-buffer",
                    "Do not automatically adjust the buffer size. By default, the buffer size is automatically resized from an initial value of SIZE.",
                    null));
            put(RequestOptionType.DOWNLOAD_HTTP_CHUNK_SIZE, new RequestOption(
                    RequestOptionType.DOWNLOAD_HTTP_CHUNK_SIZE,
                    "--http-chunk-size",
                    "Size of a chunk for chunk-based HTTP downloading (e.g. 10485760 or 10M) (default is disabled). May be useful for bypassing bandwidth throttling imposed by a webserver (experimental)",
                    "SIZE"));
            put(RequestOptionType.DOWNLOAD_REVERSE_PLAYLIST, new RequestOption(
                    RequestOptionType.DOWNLOAD_REVERSE_PLAYLIST,
                    "--playlist-reverse",
                    "Download playlist videos in reverse order",
                    null));
            put(RequestOptionType.DOWNLOAD_RANDOMIZE_PLAYLIST, new RequestOption(
                    RequestOptionType.DOWNLOAD_RANDOMIZE_PLAYLIST,
                    "--playlist-random",
                    "Download playlist videos in random order",
                    null));
            put(RequestOptionType.DOWNLOAD_SET_XATTR_FILESIZE, new RequestOption(
                    RequestOptionType.DOWNLOAD_SET_XATTR_FILESIZE,
                    "--xattr-set-filesize",
                    "Set file xattribute ytdl.filesize with expected file size",
                    null));
            put(RequestOptionType.DOWNLOAD_PREFER_NATIVE_HLS, new RequestOption(
                    RequestOptionType.DOWNLOAD_PREFER_NATIVE_HLS,
                    "--hls-prefer-native",
                    "Use the native HLS downloader instead of ffmpeg",
                    null));
            put(RequestOptionType.DOWNLOAD_PREFER_FFMPEG, new RequestOption(
                    RequestOptionType.DOWNLOAD_PREFER_FFMPEG,
                    "--hls-prefer-ffmpeg",
                    "Use ffmpeg instead of the native HLS downloader",
                    null));
            put(RequestOptionType.DOWNLOAD_PREFER_MPEGTS, new RequestOption(
                    RequestOptionType.DOWNLOAD_PREFER_MPEGTS,
                    "--hls-use-mpegts",
                    "Use the mpegts container for HLS videos, allowing to play the video while downloading (some players may not be able to play it)",
                    null));
            put(RequestOptionType.DOWNLOAD_EXTERNAL, new RequestOption(
                    RequestOptionType.DOWNLOAD_EXTERNAL,
                    "--external-downloader",
                    "Use the specified external downloader. Currently supports aria2c,avconv,axel,curl,ffmpeg,httpie,wget",
                    "COMMAND"));
            put(RequestOptionType.DOWNLOAD_EXTERNAL_ARGS, new RequestOption(
                    RequestOptionType.DOWNLOAD_EXTERNAL_ARGS,
                    "--external-downloader-args",
                    "Give these arguments to the external downloader",
                    "ARGS"));
            // FILESYSTEM OPTIONS
            put(RequestOptionType.FILESYSTEM_BATCH, new RequestOption(
                    RequestOptionType.FILESYSTEM_BATCH,
                    "--batch-file",
                    "File containing URLs to download ('-' for stdin), one URL per line. Lines starting with '#', ';' or ']' are considered as comments and ignored.",
                    "FILE"));
            put(RequestOptionType.FILESYSTEM_ID_ONLY, new RequestOption(
                    RequestOptionType.FILESYSTEM_ID_ONLY,
                    "--id",
                    "Use only video ID in file name",
                    null));
            put(RequestOptionType.FILESYSTEM_OUTPUT_TEMPLATE, new RequestOption(
                    RequestOptionType.FILESYSTEM_OUTPUT_TEMPLATE,
                    "--output",
                    "Output filename template, see the \"OUTPUT TEMPLATE\" for all the info",
                    "TEMPLATE"));
            put(RequestOptionType.FILESYSTEM_AUTONUMBER_START, new RequestOption(
                    RequestOptionType.FILESYSTEM_AUTONUMBER_START,
                    "--autonumber-start",
                    "Specify the start value for %(autonumber)s (default is 1)",
                    "NUMBER"));
            put(RequestOptionType.FILESYSTEM_RESTRICT_FILENAMES, new RequestOption(
                    RequestOptionType.FILESYSTEM_RESTRICT_FILENAMES,
                    "--restrict-filenames",
                    "Restrict filenames to only ASCII characters, and avoid \"&\" and spaces in filenames",
                    null));
            put(RequestOptionType.FILESYSTEM_NO_OVERWRITES, new RequestOption(
                    RequestOptionType.FILESYSTEM_NO_OVERWRITES,
                    "--no-overwrites",
                    "Do not overwrite files",
                    null));
            put(RequestOptionType.FILESYSTEM_FORCE_CONTINUE, new RequestOption(
                    RequestOptionType.FILESYSTEM_FORCE_CONTINUE,
                    "--continue",
                    "Force resume of partially downloaded files. By default, youtube-dl will resume downloads if possible.",
                    null));
            put(RequestOptionType.FILESYSTEM_NO_CONTINUE, new RequestOption(
                    RequestOptionType.FILESYSTEM_NO_CONTINUE,
                    "--no-continue",
                    "Do not resume partially downloaded files (restart from beginning)",
                    null));
            put(RequestOptionType.FILESYSTEM_NO_PARTS, new RequestOption(
                    RequestOptionType.FILESYSTEM_NO_PARTS,
                    "--no-part",
                    "Do not use .part files - write directly into output file",
                    null));
            put(RequestOptionType.FILESYSTEM_NO_MTIME, new RequestOption(
                    RequestOptionType.FILESYSTEM_NO_MTIME,
                    "--no-mtime",
                    "Do not use the Last-modified header to set the file modification time",
                    null));
            put(RequestOptionType.FILESYSTEM_WRITE_DESCRIPTION, new RequestOption(
                    RequestOptionType.FILESYSTEM_WRITE_DESCRIPTION,
                    "--write-description",
                    "Write video description to a .description file",
                    null));
            put(RequestOptionType.FILESYSTEM_WRITE_INFO, new RequestOption(
                    RequestOptionType.FILESYSTEM_WRITE_INFO,
                    "--write-info-json",
                    "Write video metadata to a .info.json file",
                    null));
            put(RequestOptionType.FILESYSTEM_WRITE_ANNOTATIONS, new RequestOption(
                    RequestOptionType.FILESYSTEM_WRITE_ANNOTATIONS,
                    "--write-annotations",
                    "Write video annotations to a .annotations.xml file",
                    null));
            put(RequestOptionType.FILESYSTEM_LOAD_INFO, new RequestOption(
                    RequestOptionType.FILESYSTEM_LOAD_INFO,
                    "--load-info-json",
                    "JSON file containing the video information (created with the \"--write-info-json\" option)",
                    "FILE"));
            put(RequestOptionType.FILESYSTEM_COOKIES, new RequestOption(
                    RequestOptionType.FILESYSTEM_COOKIES,
                    "--cookies",
                    "File to read cookies from and dump cookie jar in",
                    "FILE"));
            put(RequestOptionType.FILESYSTEM_CACHE_DIR, new RequestOption(
                    RequestOptionType.FILESYSTEM_CACHE_DIR,
                    "--cache-dir",
                    "Location in the filesystem where youtube-dl can store some downloaded information permanently. By default $XDG_CACHE_HOME/youtube-dl or ~/.cache/youtube-dl). At the moment, only YouTube player files (for videos with obfuscated signatures",
                    "DIR"));
            put(RequestOptionType.FILESYSTEM_NO_CACHE_DIR, new RequestOption(
                    RequestOptionType.FILESYSTEM_NO_CACHE_DIR,
                    "--no-cache-dir",
                    "Disable filesystem caching",
                    null));
            put(RequestOptionType.FILESYSTEM_DELETE_CACHE_DIR, new RequestOption(
                    RequestOptionType.FILESYSTEM_DELETE_CACHE_DIR,
                    "--rm-cache-dir",
                    "Delete all filesystem cache files",
                    null));
            // THUMBNAIL
            put(RequestOptionType.FILESYSTEM_WRITE_THUMBNAIL, new RequestOption(
                    RequestOptionType.FILESYSTEM_WRITE_THUMBNAIL,
                    "--write-thumbnail",
                    "Write thumbnail image to disk",
                    null));
            put(RequestOptionType.FILESYSTEM_WRITE_ALL_THUMBNAILS, new RequestOption(
                    RequestOptionType.FILESYSTEM_WRITE_ALL_THUMBNAILS,
                    "--write-all-thumbnails",
                    "Write all thumbnail image formats to disk",
                    null));
            put(RequestOptionType.FILESYSTEM_LIST_THUMBNAILS, new RequestOption(
                    RequestOptionType.FILESYSTEM_LIST_THUMBNAILS,
                    "--list-thumbnails",
                    "Simulate and list all available thumbnail formats",
                    null));
            // WORKAROUNDS
            put(RequestOptionType.WORKAROUND_FORCE_ENCODING, new RequestOption(
                    RequestOptionType.WORKAROUND_FORCE_ENCODING,
                    "--encoding",
                    "Force the specified encoding (experimental)",
                    "ENCODING"));
            put(RequestOptionType.WORKAROUND_NO_CERTIFICATE_CHECK, new RequestOption(
                    RequestOptionType.WORKAROUND_NO_CERTIFICATE_CHECK,
                    "--no-check-certificate",
                    "Suppress HTTPS certificate validation",
                    null));
            put(RequestOptionType.WORKAROUND_PREFER_INSECURE, new RequestOption(
                    RequestOptionType.WORKAROUND_PREFER_INSECURE,
                    "--prefer-insecure",
                    "Use an unencrypted connection to retrieve information about the video. (Currently supported only for YouTube)",
                    null));
            put(RequestOptionType.WORKAROUND_USER_AGENT, new RequestOption(
                    RequestOptionType.WORKAROUND_USER_AGENT,
                    "--user-agent",
                    "Specify a custom user agent",
                    "UA"));
            put(RequestOptionType.WORKAROUND_REFERER, new RequestOption(
                    RequestOptionType.WORKAROUND_REFERER,
                    "--referer",
                    "Specify a custom referer, use if the video access is restricted to one domain",
                    "URL"));
            put(RequestOptionType.WORKAROUND_ADD_HEADER, new RequestOption(
                    RequestOptionType.WORKAROUND_ADD_HEADER,
                    "--add-header",
                    "FIELD:VALUE         Specify a custom HTTP header and its value, separated by a colon ':'. You can use this option multiple times",
                    null));
            put(RequestOptionType.WORKAROUND_BIDIRECTIONAL, new RequestOption(
                    RequestOptionType.WORKAROUND_BIDIRECTIONAL,
                    "--bidi-workaround",
                    "Work around terminals that lack bidirectional text support. Requires bidiv or fribidi executable in PATH",
                    null));
            put(RequestOptionType.WORKAROUND_SLEEP, new RequestOption(
                    RequestOptionType.WORKAROUND_SLEEP,
                    "--sleep-interval",
                    "Number of seconds to sleep before each download when used alone or a lower bound of a range for randomized sleep before each download (minimum possible number of seconds to sleep) when used along with --max-sleep-interval.",
                    "SECONDS"));
            put(RequestOptionType.WORKAROUND_MAX_SLEEP, new RequestOption(
                    RequestOptionType.WORKAROUND_MAX_SLEEP,
                    "--max-sleep-interval",
                    "Upper bound of a range for randomized sleep before each download (maximum possible number of seconds to sleep). Must only be used along with --min-sleep-interval.",
                    "SECONDS"));
            // FORMAT
            put(RequestOptionType.FORMAT_CODE, new RequestOption(
                    RequestOptionType.FORMAT_CODE,
                    "--format",
                    "Video format code, see the \"FORMAT SELECTION\" for all the info",
                    "FORMAT"));
            put(RequestOptionType.FORMAT_ALL, new RequestOption(
                    RequestOptionType.FORMAT_ALL,
                    "--all-formats",
                    "Download all available video formats",
                    null));
            put(RequestOptionType.FORMAT_PREFER_FREE, new RequestOption(
                    RequestOptionType.FORMAT_PREFER_FREE,
                    "--prefer-free-formats",
                    "Prefer free video formats unless a specific one is requested",
                    null));
            put(RequestOptionType.FORMAT_LIST, new RequestOption(
                    RequestOptionType.FORMAT_LIST,
                    "--list-formats",
                    "List all available formats of requested videos",
                    null));
            put(RequestOptionType.FORMAT_SKIP_YT_DASH, new RequestOption(
                    RequestOptionType.FORMAT_SKIP_YT_DASH,
                    "--youtube-skip-dash-manifest",
                    "Do not download the DASH manifests and related data on YouTube videos",
                    null));
            put(RequestOptionType.FORMAT_MERGED_FORMAT, new RequestOption(
                    RequestOptionType.FORMAT_MERGED_FORMAT,
                    "--merge-output-format",
                    "If a merge is required (e.g. bestvideo+bestaudio), output to given container format. One of mkv, mp4, ogg, webm, flv. Ignored if no merge is required",
                    "FORMAT"));
            // SUBTITLE
            put(RequestOptionType.SUBTITLES_WRITE, new RequestOption(
                    RequestOptionType.SUBTITLES_WRITE,
                    "--write-sub",
                    "Write subtitle file",
                    null));
            put(RequestOptionType.SUBTITLES_WRITE_AUTO, new RequestOption(
                    RequestOptionType.SUBTITLES_WRITE_AUTO,
                    "--write-auto-sub",
                    "Write automatically generated subtitle file (YouTube only)",
                    null));
            put(RequestOptionType.SUBTITLES_ALL, new RequestOption(
                    RequestOptionType.SUBTITLES_ALL,
                    "--all-subs",
                    "Download all the available subtitles of the video",
                    null));
            put(RequestOptionType.SUBTITLES_LIST, new RequestOption(
                    RequestOptionType.SUBTITLES_LIST,
                    "--list-subs",
                    "List all available subtitles for the video",
                    null));
            put(RequestOptionType.SUBTITLES_FORMAT, new RequestOption(
                    RequestOptionType.SUBTITLES_FORMAT,
                    "--sub-format",
                    "Subtitle format, accepts formats preference, for example: \"srt\" or \"ass/srt/best\"",
                    "FORMAT"));
            put(RequestOptionType.SUBTITLES_LANGAUGE, new RequestOption(
                    RequestOptionType.SUBTITLES_LANGAUGE,
                    "--sub-lang",
                    "Languages of the subtitles to download (optional) separated by commas, use --list-subs for available language tags",
                    "LANGS"));
            // AUTHENTICATION
            put(RequestOptionType.AUTHENTICATION_USERNAME, new RequestOption(
                    RequestOptionType.AUTHENTICATION_USERNAME,
                    "--username",
                    "Login with this account ID",
                    "USERNAME"));
            put(RequestOptionType.AUTHENTICATION_PASSWORD, new RequestOption(
                    RequestOptionType.AUTHENTICATION_PASSWORD,
                    "--password",
                    "Account password. If this option is left out, youtube-dl will ask interactively.",
                    "PASSWORD"));
            put(RequestOptionType.AUTHENTICATION_TWO_FACTOR, new RequestOption(
                    RequestOptionType.AUTHENTICATION_TWO_FACTOR,
                    "--twofactor",
                    "Two-factor authentication code",
                    "TWOFACTOR"));
            put(RequestOptionType.AUTHENTICATION_NETRC, new RequestOption(
                    RequestOptionType.AUTHENTICATION_NETRC,
                    "--netrc",
                    "Use .netrc authentication data",
                    null));
            put(RequestOptionType.AUTHENTICATION_VIDEO_PASSWORD, new RequestOption(
                    RequestOptionType.AUTHENTICATION_VIDEO_PASSWORD,
                    "--video-password",
                    "Video password (vimeo, smotri, youku)",
                    "PASSWORD"));
            // POST-PROCESSING
            put(RequestOptionType.POST_EXTRACT_AUDIO, new RequestOption(
                    RequestOptionType.POST_EXTRACT_AUDIO,
                    "--extract-audio",
                    "Convert video files to audio-only files (requires ffmpeg or avconv and ffprobe or avprobe)",
                    null));
            put(RequestOptionType.POST_AUDIO_FORMAT, new RequestOption(
                    RequestOptionType.POST_AUDIO_FORMAT,
                    "--audio-format",
                    "Specify audio format: \"best\", \"aac\", \"flac\", \"mp3\", \"m4a\", \"opus\", \"vorbis\", or \"wav\"; \"best\" by default; No effect without -x",
                    "FORMAT"));
            put(RequestOptionType.POST_AUDIO_QUALITY, new RequestOption(
                    RequestOptionType.POST_AUDIO_QUALITY,
                    "--audio-quality",
                    "Specify ffmpeg/avconv audio quality, insert a value between 0 (better) and 9 (worse) for VBR or a specific bitrate like 128K (default 5)",
                    "QUALITY"));
            put(RequestOptionType.POST_RECODE_VIDEO, new RequestOption(
                    RequestOptionType.POST_RECODE_VIDEO,
                    "--recode-video",
                    "Encode the video to another format if necessary (currently supported: mp4|flv|ogg|webm|mkv|avi)",
                    "FORMAT"));
            put(RequestOptionType.POST_POSTPROCESSOR_ARGS, new RequestOption(
                    RequestOptionType.POST_POSTPROCESSOR_ARGS,
                    "--postprocessor-args",
                    "Give these arguments to the postprocessor",
                    "ARGS"));
            put(RequestOptionType.POST_KEEP_VIDEO, new RequestOption(
                    RequestOptionType.POST_KEEP_VIDEO,
                    "--keep-video",
                    "Keep the video file on disk after the post-processing; the video is erased by default",
                    null));
            put(RequestOptionType.POST_NO_OVERRIDES, new RequestOption(
                    RequestOptionType.POST_NO_OVERRIDES,
                    "--no-post-overwrites",
                    "Do not overwrite post-processed files; the post-processed files are overwritten by default",
                    null));
            put(RequestOptionType.POST_EMBED_SUBTITLES, new RequestOption(
                    RequestOptionType.POST_EMBED_SUBTITLES,
                    "--embed-subs",
                    "Embed subtitles in the video (only for mp4, webm and mkv videos)",
                    null));
            put(RequestOptionType.POST_EMBED_THUMBNAIL, new RequestOption(
                    RequestOptionType.POST_EMBED_THUMBNAIL,
                    "--embed-thumbnail",
                    "Embed thumbnail in the audio as cover art",
                    null));
            put(RequestOptionType.POST_ADD_METADATA, new RequestOption(
                    RequestOptionType.POST_ADD_METADATA,
                    "--add-metadata",
                    "Write metadata to the video file",
                    null));
            put(RequestOptionType.POST_METADATA_FROM_TITLE, new RequestOption(
                    RequestOptionType.POST_METADATA_FROM_TITLE,
                    "--metadata-from-title",
                    "Parse additional metadata like song title / artist from the video title. The format syntax is the same as --output. Regular expression with named capture groups may also be used. The parsed parameters replace existing values. Example: --metadata-from-title \"%(artist)s - %(title)s\" matches a title like \"Coldplay - Paradise\". Example (regex): --metadata-from-title \"(?P<artist>.+?) - (?P<title>.+)\"",
                    "FORMAT"));
            put(RequestOptionType.POST_XATTRIBUTES, new RequestOption(
                    RequestOptionType.POST_XATTRIBUTES,
                    "--xattrs",
                    "Write metadata to the video file's xattrs (using dublin core and xdg standards)",
                    null));
            put(RequestOptionType.POST_FIXUP, new RequestOption(
                    RequestOptionType.POST_FIXUP,
                    "--fixup",
                    "Automatically correct known faults of the file. One of never (do nothing), warn (only emit a warning), detect_or_warn (the default; fix file if we can, warn otherwise)",
                    "POLICY"));
            put(RequestOptionType.POST_PREFER_AVCONV, new RequestOption(
                    RequestOptionType.POST_PREFER_AVCONV,
                    "--prefer-avconv",
                    "Prefer avconv over ffmpeg for running the postprocessors",
                    null));
            put(RequestOptionType.POST_PREFER_FFMPEG, new RequestOption(
                    RequestOptionType.POST_PREFER_FFMPEG,
                    "--prefer-ffmpeg",
                    "Prefer ffmpeg over avconv for running the postprocessors (default)",
                    null));
            put(RequestOptionType.POST_FFMPEG_LOCATION, new RequestOption(
                    RequestOptionType.POST_FFMPEG_LOCATION,
                    "--ffmpeg-location",
                    "Location of the ffmpeg/avconv binary; either the path to the binary or its containing directory.",
                    "PATH"));
            put(RequestOptionType.POST_EXEC, new RequestOption(
                    RequestOptionType.POST_EXEC,
                    "--exec",
                    "Execute a command on the file after downloading, similar to find's -exec syntax. Example: --exec 'adb push {} /sdcard/Music/ && rm {}'",
                    "CMD"));
            put(RequestOptionType.POST_CONVERT_SUBS, new RequestOption(
                    RequestOptionType.POST_CONVERT_SUBS,
                    "--convert-subs",
                    "Convert the subtitles to other format (currently supported: srt|ass|vtt|lrc)",
                    "FORMAT"));
        }};

    public RequestOptionType type;
    public String argument;
    public String description;
    public String valueType;

    private RequestOption(RequestOptionType type, String argument, String description, @Nullable String valueType) {
        this.type = type;
        this.argument = argument;
        this.description = description;
        this.valueType = valueType;
    }

}
