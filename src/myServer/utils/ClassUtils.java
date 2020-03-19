package myServer.utils;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: weiqiang
 * @Time: 2020/3/17 下午8:52
 */
public class ClassUtils {
//    public static void main(String[] args) {
//        String packageName = ""; //填入完整包名，如com.org.String
//        Set<String> classNames = getClassName("cn.magicdu.think", true);
//        if (classNames != null) {
//            for (String className : classNames) {
//                System.out.println(className);
//            }
//        }
//    }

    public static Set<String> getClassName(String packageName, boolean isRecursion) {
        Set<String> classNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");

        URL url = loader.getResource(packagePath);
        if (url != null) {
            classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
        }

        return classNames;
    }

    private static Set<String> getClassNameFromDir(String filePath, String packageName, boolean isRecursion) {
        if (packageName.length() != 0)
            packageName += ".";
        Set<String> className = new HashSet<String>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (File childFile : files) {
            //�?查一个对象是否是文件�?
            if (childFile.isDirectory()) {
                if (isRecursion) {
                    className.addAll(getClassNameFromDir(childFile.getPath(), packageName  + childFile.getName(), isRecursion));
                }
            } else {
                String fileName = childFile.getName();
                //endsWith() 方法用于测试字符串是否以指定的后�?结束�?  !fileName.contains("$") 文件名中不包�? '$'
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    className.add(packageName  + fileName.replace(".class", ""));
                }
            }
        }
        return className;
    }

}
