package com.android.xjq.utils.live;

/**
 * Created by lingjiu on 2017/3/13.
 */

public class MessageEvent {

    public static final String EXIT_FLOATING_WINDOW = "exit_floating_window";

    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public static String getExitFloatingWindow() {
        return EXIT_FLOATING_WINDOW;
    }

    public String getMessage() {
        return message;
    }
}
