package myServer.json;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author: weiqiang
 * @Time: 2020/3/19 下午10:06
 */
public class MyJson {

    public static String json(Object o) {
        try {
            return toJson(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toJson(Object o) {
        if (isBase(o)) {
            if (o.getClass() == String.class)
                return "\"" + o + "\"";
            return o + "";
        }
        if (isEntity(o))
            return toJsonBase(o);
        if (isArray(o))
            return toJsonArray(o);
        if (isCollection(o))
            return toJsonCollection(o);
        if (isMap(o))
            return toJsonMap(o);
        return "";
    }


    private static String toJsonBase(Object o) {
        Class c = o.getClass();
        Field[] fs = c.getDeclaredFields();
        String result = "{";
        if (fs == null || fs.length == 0) {
            return "";
        }
        for (Field f : fs) {
            f.setAccessible(true);
            try {
                Object value = f.get(o);
                String key = f.getName();
                if (value == null)
                    value = "\"\"";
                else
                    value = toJson(value);
                result += "\"" + key + "\":" + value + ",";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.substring(0, result.length() - 1) + "}";
    }

    private static String toJsonArray(Object o) {
        if (isBaseArry(o))
            return toBaseArrayJson(o);
        Object[] arr = (Object[]) o;
        String result = "[";
        for (Object a : arr) {
            if (a == null)
                continue;
            result += toJson(a) + ",";
        }
        return result.substring(0, result.length() - 1) + "]";
    }

    private static String toBaseArrayJson(Object o) {
        Object[] arr = (Object[]) o;
        String result = "[";
        if (o.getClass() == String[].class)
            for (Object a : arr)
                result += "\"" + a + "\"" + ",";
        else for (Object a : arr)
            result += a + ",";
        return result.substring(0, result.length() - 1) + "]";
    }

    private static String toJsonMap(Object o) {
        Map<Object, Object> map = (Map<Object, Object>) o;
        String result = "{";
        Set<Object> set = map.keySet();
        if (set.size() == 0)
            return "{}";
        Iterator<Object> iter = set.iterator();
        while (iter.hasNext()) {
            String str = iter.next() + "";
            result += ("\"" + str + "\"");
            Object obj = map.get(str);
            result += (":" + toJson(obj) + ",");
        }
        return result.substring(0, result.length() - 1) + "}";
    }

    private static String toJsonCollection(Object o) {
        Collection<Object> cs = (Collection) o;
        if (cs == null || cs.size() == 0)
            return "\"\"";
        String resule = "[";
        for (Object oc : cs)
            resule += toJson(oc) + ",";
        return resule.substring(0, resule.length() - 1) + "]";
    }

    private static boolean isArray(Object o) {
        return o.getClass().toString().startsWith("class [");
    }

    private static boolean isMap(Object o) {
        return isMap_(o.getClass());
    }

    private static boolean isEntity(Object o) {
        return !(isBase(o) || isArray(o) || isCollection(o) || isMap(o));
    }

    private static boolean isCollection(Object o) {
        return isCollection_(o.getClass());
    }

    private static boolean isMap_(Class o) {
        Class[] interfaces = o.getInterfaces();
        for (Class i : interfaces) {
            if (i == Map.class)
                return true;
            if (i.getInterfaces() != null)
                return isMap_(i);
        }
        return false;
    }

    private static boolean isCollection_(Class o) {
        Class[] interfaces = o.getInterfaces();
        for (Class i : interfaces) {
            if (i == Collection.class)
                return true;
            if (i.getInterfaces() != null)
                return isCollection_(i);
        }
        return false;
    }

    private static boolean isBase(Object o) {
        Class c = o.getClass();
        return c == Integer.class || c == Byte.class || c == Boolean.class
                || c == Double.class || c == Float.class || c == Long.class
                || c == String.class || c == Short.class
                || c == Character.class;
    }

    private static boolean isBaseArry(Object o) {
        Class c = o.getClass();
        return c == int[].class || c == double[].class || c == long[].class
                || c == char[].class || c == byte[].class || c == boolean[].class
                || c == float[].class || c == short[].class;
    }
}
