package wuxian.me.spidersdk;

import wuxian.me.spidersdk.distribute.ClassFileUtil;
import wuxian.me.spidersdk.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by wuxian on 12/5/2017.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("main");
        String jarPath = "";
        try {
            jarPath = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (Exception e) {
        }

        try {
            JarFile jar = new JarFile(jarPath);
            Set<Class<?>> classSet = ClassFileUtil.getJarFileClasses(jar);

            for (Class<?> clazz : classSet) {
                System.out.println(clazz.getName());
            }


        } catch (IOException e) {

        }

    }
}
