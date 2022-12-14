package hsge.hsgeback.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageUtil {
    private static final String REPLACE_CHAR = "%s";

    public static String parseMessage(String message, String ...args) {
        if (message == null || message.trim().length() <= 0) {
            return message;
        }

        if (args == null || args.length <= 0) {
            return message;
        }

        String[] split = message.split(REPLACE_CHAR);

        if (split.length <= 1) {
            return message;
        }

        for (String arg : args) {
            message = message.replaceFirst(REPLACE_CHAR, arg);
        }

        return message;
    }
}
