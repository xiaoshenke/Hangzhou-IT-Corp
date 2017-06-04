package wuxian.me.spidermaster.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * http://outofmemory.cn/java/guava/IO/Files-operation
 */
public class FileUtil {

    static {
        try {
            File file = new File(FileUtil.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath());
            if (FileUtil.checkFileExist(file.getParentFile().getAbsolutePath() + SpiderConfig.DEFAULT_PATH)) {
                FileUtil.setCurrentFile(file.getAbsolutePath());
                FileUtil.setCurrentPath(file.getParentFile().getAbsolutePath());

            } else {
                file = new File("");
                FileUtil.setCurrentPath(file.getAbsolutePath());
            }
        } catch (Exception e) {
            ;
        }
    }

    private FileUtil() {
    }

    private static String currentPath;

    public static String currentFile;

    public static void setCurrentFile(String path) {
        currentFile = path;
    }


    //ide运行和java -jar运行的时候是不同的值
    public static void setCurrentPath(String path) {
        currentPath = path;
    }

    public static String getCurrentPath() {
        if (currentPath == null) {
            File file = null;
            file = new File("");
            currentPath = file.getAbsolutePath();

        }
        return currentPath;
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
            file.delete();
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