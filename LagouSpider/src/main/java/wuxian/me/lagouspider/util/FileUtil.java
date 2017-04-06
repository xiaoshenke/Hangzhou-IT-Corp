package wuxian.me.lagouspider.util;

import static com.google.common.base.Preconditions.*;
import static wuxian.me.lagouspider.Config.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * http://outofmemory.cn/java/guava/IO/Files-operation
 */
public class FileUtil {

    private FileUtil() {
    }

    public static String getAreaFilePath() {
        return getCurrentPath() + CONF_AREA;
    }

    public static String getDistinctsFilePath() {
        return getCurrentPath() + CONF_DISTINTC;
    }

    public static String getGrabFilePath() {
        return getCurrentPath() + CONF_LASTGRAB;
    }

    public static String getCookieFilePath() {
        return getCurrentPath() + CONF_COOKIE;
    }

    public static String getCurrentPath() {
        File file = new File("");

        return file.getAbsolutePath();
    }

    public static boolean checkFileExist(String path) {
        checkNotNull(path);
        File file = new File(path);
        return file.exists();
    }

    public static boolean writeToFile(String path, String content) {
        checkNotNull(path);
        checkNotNull(content);
        File file = new File(path);
        try {
            if (!file.exists()) {
                Files.createParentDirs(file);
            }
            Files.write(content.getBytes(), file);
        } catch (IOException e) {
            file.delete();  //暂时简单的删除
            return false;
        }
        return true;
    }

    @Nullable
    public static String readFromFile(String path) {
        checkNotNull(path);
        File file = new File(path);
        try {
            List<String> lines = Files.readLines(file, Charsets.UTF_8);
            String content = "";
            for (String line : lines) {
                content += line;
            }
            return content;
        } catch (IOException e) {
            return null;
        }
    }
}
