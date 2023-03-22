/**
 * Error.java
 *
 */

package io.gitplelive.chat.sdk.model;


public class ErrorType {

    public static int INVALID_PARAMETERS = 60101;
    public static int INVALID_TOKEN = 60102;
    public static int EXPIRED_TOKEN = 60103;
    public static int INVALID_CHANNEL_ID = 60104;
    public static int SERVER_NOT_RESPONDING = 60901;
    public static int UNABLE_CONNECT_ERROR = 60902;
    public static int UNABLE_SUBSCRIBE_ERROR = 60903;
    public static int NOT_CONNECTED = 60904;
    public static int UNKNOWN_ERROR = 60999;

    public static String message(int errorType) {
        if (errorType == 1001) return "Invalid or missing parameters.";
        if (errorType == 1002) return "The requested resource could not be found.";
        if (errorType == 1003) return "The requested channel could not be found.";
        if (errorType == 1004) return "The requested user could not be found.";
        if (errorType == 1005) return "The requested message could not be found.";
        if (errorType == 1006) return "Member already joined.";
        if (errorType == 1007) return "Duplicate ID.";
        if (errorType == 1008) return "Not Joined.";
        if (errorType == 1009) return "Frozen channel.";
        if (errorType == 1010) return "Number of members exceeded.";
        if (errorType == 1011) return "Number of managers exceeded.";
        if (errorType == 1012) return "File size exceeded.";
        if (errorType == 1013) return "Token usage is off.";
        if (errorType == 1014) return "Key count exceeded.";
        if (errorType == 1015) return "User token usage is off.";
        if (errorType == 1016) return "Not a registered manager.";
        if (errorType == 1017) return "Managers can not ban each other.";
        if (errorType == 1018) return "Can not ban yourself.";
        if (errorType == 1019) return "Join is a banned user.";
        if (errorType == 1020) return "Profanity in content.";
        if (errorType == 3001) return "No permissions.";
        if (errorType == 4001) return "Unauthorized.";
        if (errorType == 4002) return "Unauthorized organization.";
        if (errorType == 4003) return "Unauthorized application.";
        if (errorType == 9001) return "Too Many Requests.";
        if (errorType == 9999) return "Unknown Server Error.";

        if (errorType == INVALID_PARAMETERS) return "Check the sdk initialization init parameters.";
        if (errorType == INVALID_TOKEN) return "Invalid session token.";
        if (errorType == EXPIRED_TOKEN) return "Generate token again.";
        if (errorType == INVALID_CHANNEL_ID) return "Invalid channel ID.";
        if (errorType == SERVER_NOT_RESPONDING) return "The server is not responding.";
        if (errorType == UNABLE_CONNECT_ERROR) return "Unable to connect to the server.";
        if (errorType == UNABLE_SUBSCRIBE_ERROR) return "Unable to subscribe to the event.";
        if (errorType == NOT_CONNECTED) return "The device is not connected to the server.";
        if (errorType == UNKNOWN_ERROR) return "Check the message on the console.";

        return "";
    }

} // Error.java