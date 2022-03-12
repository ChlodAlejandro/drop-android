package net.chlod.android.drop.utility;

import java.util.regex.Pattern;

public class FilenameUtilities {

    public static boolean isValidFileName(String filename, boolean linuxOnly) {
        if (filename.length() == 0)
            return false;
        else if (filename.startsWith("/"))
            return false;
        else return !Pattern.matches("(?:^/+.*)|(?:^\\\\+.*)|(?=[\\x00-\\x1F])(?=[^\\r\\n])", filename);
    }

    public static boolean isValidFileName(String filename) {
        return isValidFileName(filename, false);
    }

}
