package net.chlod.android.drop.utility;

import net.chlod.android.drop.enums.RequestOptionType;
import net.chlod.android.drop.objects.RequestOption;
import net.chlod.ytdl_android.YoutubeDLRequest;

public class RequestOptionUtilities {

    public static RequestOption getRequestOptionFromType(RequestOptionType type) {
        return RequestOption.options.get(type);
    }

    public static void setRequestOption(YoutubeDLRequest request, RequestOptionType type, String value) {
        RequestOption requestOption = getRequestOptionFromType(type);
        if (requestOption.valueType == null)
            throw new IllegalArgumentException(type.toString() + " does not take any values.");
        else
            request.setOption(requestOption.argument, value);
    }

    public static void setRequestOption(YoutubeDLRequest request, RequestOptionType type, Number value) {
        RequestOption requestOption = getRequestOptionFromType(type);
        if (requestOption.valueType == null)
            throw new IllegalArgumentException(type.toString() + " does not take any values.");
        else
            request.setOption(requestOption.argument, value);
    }

    public static void setRequestOption(YoutubeDLRequest request, RequestOptionType type) {
        RequestOption requestOption = getRequestOptionFromType(type);
        if (requestOption.valueType != null)
            throw new IllegalArgumentException(type.toString() + " needs a value.");
        else
            request.setOption(requestOption.argument);
    }

}
