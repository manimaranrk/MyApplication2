package com.purpleknot1.Util;


public class Util {

//
//    //simple messageBar for FPS
//    public static void messageBar(Activity activity, String message) {
//        UndoBar undoBar = new UndoBar.Builder(activity)
//                .setMessage(message)
//                .setStyle(UndoBar.Style.KITKAT)
//                .setAlignParentBottom(true)
//                .create();
//        undoBar.show();
//
//    }
//
//    /**
//     * Add log to queue
//     *
//     * @param context,errorType and error string
//     */
//    public static void LoggingQueue(Context context, String errorType, String error) {
//
//        GlobalAppState appState = (GlobalAppState) context.getApplicationContext();
//        if (appState.isLoggingEnabled)
//            appState.queue.enqueue(logging(context, errorType, error));
//
//    }
//
//    /**
//     * get log data and set device id in log
//     *
//     * @param context,errorType and error string
//     */
//    private static LoggingDto logging(Context context, String errorType, String error) {
//        LoggingDto log = new LoggingDto();
//        log.setErrorType(errorType);
//        log.setLogMessage(error);
//        log.setDeviceId(Settings.Secure.getString(
//                context.getContentResolver(), Settings.Secure.ANDROID_ID));
//        return log;
//    }

}
