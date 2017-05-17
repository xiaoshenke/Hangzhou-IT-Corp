package wuxian.me.spidersdk.distribute;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by wuxian on 12/5/2017.
 */
public class ClassHelper {

    private ClassHelper() {
    }

    public static Class getClassByName(String name) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(name);
    }

    public static Set<Class<?>> getClasses(String pack) throws IOException {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        boolean recursive = true;
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);

            } else if ("jar".equals(protocol)) {

                JarFile jar;
                jar = ((JarURLConnection) url.openConnection()).getJarFile();
                Set<Class<?>> set = getJarFileClasses(jar, packageName);

                classes.addAll(set);
            }
        }

        return classes;
    }

    public static Set<Class<?>> getJarFileClasses(@NotNull JarFile jar) {
        return getJarFileClasses(jar, null);
    }

    public static Set<Class<?>> getJarFileClasses(JarFile jar, @Nullable String packageName, @Nullable CheckFilter filter) {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        boolean recursive = true;
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            if (packageName != null) {
                String packageDirName = packageName.replace('.', '/');
                if (name.startsWith(packageDirName)) {
                    int idx = name.lastIndexOf('/');
                    if (idx != -1) {
                        packageName = name.substring(0, idx)
                                .replace('/', '.');
                    }
                    if ((idx != -1) || recursive) {
                        if (name.endsWith(".class") && !entry.isDirectory()) {

                            String className = name.substring(packageName.length() + 1, name.length() - 6);
                            try {
                                classes.add(Class.forName(packageName + '.' + className));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                System.out.println(name);
                if (filter != null && !filter.apply(name)) {
                    break;
                }
                int idx = name.lastIndexOf('/');
                if (idx != -1 && name.endsWith(".class") && !entry.isDirectory()) {
                    String packageName1 = name.substring(0, idx).replace('/', '.');
                    String className = name.substring(packageName1.length() + 1, name.length() - 6);
                    try {
                        classes.add(Class.forName(packageName1 + '.' + className));

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return classes;
    }

    public static Set<Class<?>> getJarFileClasses(JarFile jar, @Nullable String packageName) {
        return getJarFileClasses(jar, packageName, null);
    }

    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath,
                                                        final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        for (File file : dirfiles) {

            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().
                            loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface CheckFilter {
        boolean apply(String name);
    }
}
