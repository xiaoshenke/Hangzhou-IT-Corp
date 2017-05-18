package wuxian.me.spidersdk.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import wuxian.me.spidersdk.JobManagerConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static wuxian.me.spidersdk.JobManagerConfig.*;
import static wuxian.me.spidersdk.JobManagerConfig.redisPort;
import static wuxian.me.spidersdk.util.FileUtil.getCurrentPath;

/**
 * Created by wuxian on 3/5/2017.
 */
public class ShellUtil {

    private static final String shellOpenProxyFile = "/util/shell/openproxy";
    private static final String shellCheckprocessFile = "/util/shell/processexist";
    private static final String shellCheckRedisRunning = "/util/shell/checkredisrunning";

    private ShellUtil() {
    }

    private static boolean init = false;
    public static void init(){
        if(!init){
            init = true;

            String path = FileUtil.getCurrentPath() + shellOpenProxyFile;  //写shell文件
            if (!FileUtil.checkFileExist(path)) {
                String shell = "open -t " + FileUtil.getCurrentPath() + ipproxyFile;
                FileUtil.writeToFile(path, shell);
            }

            ShellUtil.chmod(path, 0777);

            path = FileUtil.getCurrentPath() + shellCheckprocessFile;
            if (!FileUtil.checkFileExist(path)) {
                String shell = "ps -ef | grep $1";
                FileUtil.writeToFile(path, shell);
            }
            ShellUtil.chmod(path, 0777);

            path = FileUtil.getCurrentPath() + shellCheckRedisRunning;
            if (!FileUtil.checkFileExist(path)) {
                String shell = "redis-cli -h " + redisIp + " -p " + redisPort + " ping";
                FileUtil.writeToFile(path, shell);
            }
            ShellUtil.chmod(path, 0777);
        }

    }

    public static boolean isRedisServerRunning() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String check = getCheckRedisServerRunningPath();
        String[] args = new String[]{check};
        Process pc = null;
        pc = runtime.exec(args);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(pc.getInputStream()));

        StringBuilder builder = new StringBuilder("");
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString().contains("PONG");
    }

    //0:running 1:not running -1:not known
    public static int textEditState() {
        try {
            return isTextEditRunning() ? 0 : 1;
        } catch (IOException e) {
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private static String getCheckRedisServerRunningPath() {
        return getCurrentPath() + shellCheckRedisRunning;
    }

    private static String getCheckProcessShellPath() {
        return getCurrentPath() + shellCheckprocessFile;
    }

    private static boolean isTextEditRunning() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String check = getCheckProcessShellPath();
        String[] args = new String[]{check, "TextEdit"};
        Process pc = null;
        pc = runtime.exec(args);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(pc.getInputStream()));

        StringBuilder builder = new StringBuilder("");
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString().contains("/TextEdit");  //contains方法非正则 不用特殊处理
    }

    public static boolean openTextEdit() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(FileUtil.readFromFile(
                    getOpenProxyShellPath()));
            int exit = proc.waitFor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int chmod(String path, int mode) {
        return libc.chmod(path, mode);
    }


    public static String getOpenProxyShellPath() {
        return getCurrentPath() + shellOpenProxyFile;
    }

    //http://stackoverflow.com/questions/664432/how-do-i-programmatically-change-file-permissions
    public static CLibrary libc = (CLibrary) Native.loadLibrary("c", CLibrary.class);

    public interface CLibrary extends Library {
        int chmod(String path, int mode);
    }
}
