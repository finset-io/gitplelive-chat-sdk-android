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
        else if (errorType == 1002) return "The requested resource could not be found.";
        else if (errorType == 1003) return "The requested channel could not be found.";
        else if (errorType == 1004) return "The requested user could not be found.";
        else if (errorType == 1005) return "The requested message could not be found.";
        else if (errorType == 1006) return "Member already joined.";
        else if (errorType == 1007) return "Duplicate ID.";
        else if (errorType == 1008) return "Not Joined.";
        else if (errorType == 1009) return "Frozen channel.";
        else if (errorType == 1010) return "Number of members exceeded.";
        else if (errorType == 1011) return "Number of managers exceeded.";
        else if (errorType == 1012) return "File size exceeded.";
        else if (errorType == 1013) return "Token usage is off.";
        else if (errorType == 1014) return "Key count exceeded.";
        else if (errorType == 1015) return "User token usage is off.";
        else if (errorType == 1016) return "Not a registered manager.";
        else if (errorType == 1017) return "Managers can not ban each other.";
        else if (errorType == 1018) return "Can not ban yourself.";
        else if (errorType == 1019) return "Join is a banned user.";
        else if (errorType == 1020) return "Profanity in content.";
        else if (errorType == 3001) return "No permissions.";
        else if (errorType == 4001) return "Unauthorized.";
        else if (errorType == 4002) return "Unauthorized organization.";
        else if (errorType == 4003) return "Unauthorized application.";
        else if (errorType == 9001) return "Too Many Requests.";
        else if (errorType == 9999) return "Unknown Server Error.";

        else if (errorType == INVALID_PARAMETERS) return "Check the sdk initialization init parameters.";
        else if (errorType == INVALID_TOKEN) return "Invalid session token.";
        else if (errorType == EXPIRED_TOKEN) return "Generate token again.";
        else if (errorType == INVALID_CHANNEL_ID) return "Invalid channel ID.";
        else if (errorType == SERVER_NOT_RESPONDING) return "The server is not responding.";
        else if (errorType == UNABLE_CONNECT_ERROR) return "Unable to connect to the server.";
        else if (errorType == UNABLE_SUBSCRIBE_ERROR) return "Unable to subscribe to the event.";
        else if (errorType == NOT_CONNECTED) return "The device is not connected to the server.";
        else if (errorType == UNKNOWN_ERROR) return "Check the message on the console.";

        return "";
    }

} // Error.java