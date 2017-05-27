package wuxian.me.spidermaster.util;

import com.sun.istack.internal.Nullable;
import com.sun.jna.Library;
import com.sun.jna.Native;
import wuxian.me.spidermaster.util.log.LogManager;

/**
 * Created by wuxian on 27/5/2017.
 */
public class ShellUtil {
    private static final String shellKillprocessFile = "/util/shell/killprocess";

    private ShellUtil() {
    }

    private static boolean init = false;

    public static void init() {
        if (!init) {
            init = true;

            String path = null;

            path = FileUtil.getCurrentPath() + shellKillprocessFile;
            if (!FileUtil.checkFileExist(path)) {
                String shell = "kill -9 $1";
                FileUtil.writeToFile(path, shell);
            }
            ShellUtil.chmod(path, 0777);

        }

    }

    public static int chmod(String path, int mode) {
        return libc.chmod(path, mode);
    }

    public static boolean killProcessBy(@Nullable String pid) {
        if (pid == null) {
            return false;
        }
        Runtime runtime = Runtime.getRuntime();
        try {
            String kill = FileUtil.getCurrentPath() + shellKillprocessFile;
            String[] args = new String[]{kill, pid};
            Process proc = runtime.exec(args);
            int exit = proc.waitFor();
            return true;
        } catch (Exception e) {
            LogManager.error("openTextEidt e: " + e.getMessage());
            return false;
        }
    }

    //http://stackoverflow.com/questions/664432/how-do-i-programmatically-change-file-permissions
    public static CLibrary libc = (CLibrary) Native.loadLibrary("c", CLibrary.class);

    public interface CLibrary extends Library {
        int chmod(String path, int mode);
    }
}
