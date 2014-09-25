package net.locplus.tools;

import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dean on 2014/9/24.
 */
public class ToStringUtil {
    private final static Logger logger = LoggerFactory.getLogger(ToStringUtil.class);

    private static Map<String, List<String>> shortFieldMap;

    private static Map<String, List<String>> listFieldMap;

    private static Map<String, List<String>> fullFieldMap;

    private static void init(Object object) {
        if (shortFieldMap == null || listFieldMap == null) {
            shortFieldMap = new HashMap<String, List<String>>();
            listFieldMap = new HashMap<String, List<String>>();
            fullFieldMap = new HashMap<String, List<String>>();
        }
        explainAnnotation(object);
        System.out.println(shortFieldMap);
        System.out.println(listFieldMap);
        System.out.println(fullFieldMap);
    }

    public static String toString(Object object) {
        if (object == null) {
            return null;
        }
        init(object);
        return doShortToString(object);
    }

    public static String listToString(List<?> list) {
        if (list == null && list.isEmpty()) {
            return null;
        }
        init(list.get(0));
        return doListToString(list);
    }

    public static String fullToString(Object object) {
        if (object == null) {
            return null;
        }
        init(object);
        return doFullToString(object);
    }


    private static String doShortToString(Object object) {
        List<String> fields = getShortFields(object.getClass());
        return doToString(object, fields, false);
    }



    private static String doListToString(List<?> list) {
        List<String> result = new ArrayList<String>();

        for (Object object : list) {
            if (isFieldBasicType(object)) {
                result.add(object.toString());
            } else {
                List<String> listFields = getListFields(object.getClass());
                String toString = doToString(object, listFields, false);
                if (toString != null) {
                    result.add(toString);
                }
            }
        }
        if (result != null && !result.isEmpty()) {
            return result.toString();
        } else {
            return null;
        }
    }



    private static String doFullToString(Object object) {
        List<String> fields = getFullFields(object.getClass());
        return doToString(object, fields, true);
    }



    private static String doListFullToString(List<?> list) {
        List<String> result = new ArrayList<String>();
        for (Object object : list) {
            if (isFieldBasicType(object)) {
                result.add(object.toString());
            } else {
                List<String> fields = getFullFields(object.getClass());
                String toString = doToString(object, fields, true);
                if (toString != null) {
                    result.add(toString);
                }
            }
        }
        if (result != null && !result.isEmpty()) {
            return result.toString();
        } else {
            return null;
        }
    }

    private static String doToString(Object object, List<String> fields, boolean isFull) {
        if (fields == null) {
            return null;
        }
        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(object);
        for (String fieldName : fields) {
            Object value = getFieldValue(object, fieldName);
            if (value != null && (value instanceof List)) {
                String listToString = null;
                if (isFull) {
                    listToString = doListFullToString((List<?>) value);
                } else {
                    listToString = doListToString((List<?>) value);
                }
                if (listToString != null) {
                    toStringHelper.add(fieldName, listToString);
                }
            } else {
                toStringHelper.add(fieldName, value);
            }
        }
        return toStringHelper.toString();
    }

    /**
     * 解析Class对象上的注解，将对应的Field放到对应的Map中
     *
     * @param object
     */
    private static void explainAnnotation(Object object) {

        List<String> shortFields = new ArrayList<String>();
        List<String> listFields = new ArrayList<String>();
        List<String> fullFields = new ArrayList<String>();

        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(ToString.class)) {
                    ToString toString = field.getAnnotation(ToString.class);
                    System.out.println(toString.value());
                    if (toString.value() == ToStringType.SHORT) {
                        System.out.println(field.getName());
                        shortFields.add(field.getName());
                    }
                    if (toString.value() == ToStringType.LIST) {
                        System.out.println(field.getName());
                        listFields.add(field.getName());
                    }
                    if (toString.value() == ToStringType.FULL || toString.value() == null) {
                        System.out.println(field.getName());
                        fullFields.add(field.getName());
                    }

                    //如果属性是List，则将其中的对象也进行注解扫描
                    Object fieldObject = getFieldValue(object, field.getName());
                    if (fieldObject != null && fieldObject instanceof List) {
                        List fieldList = (List) fieldObject;
                        if (!fieldList.isEmpty()) {
                            explainAnnotation(fieldList.get(0));
                        }
                    }
                }
            }
        }

        String clazzName = clazz.getName();
        if (!shortFields.isEmpty()) {
            shortFieldMap.put(clazzName, shortFields);
        }
        if (!listFields.isEmpty()) {
            listFieldMap.put(clazzName, listFields);
        }
        if (!fullFields.isEmpty()) {
            fullFieldMap.put(clazzName, fullFields);
        }
    }

    /**
     * 通过域名称获取域对应的值
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static Object getFieldValue(Object object, String fieldName) {
        Object result = null;
        Class clazz = object.getClass();
        try {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
            Method getMethod = pd.getReadMethod();
            if (getMethod != null) {
                result = getMethod.invoke(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有LIST的域
     * @param clazz
     * @return
     */
    private static List<String> getListFields(Class clazz) {
        return listFieldMap.get(clazz.getName());
    }

    /**
     * 获取所有SHORT的域，包括List
     * @param clazz
     * @return
     */
    private static List<String> getShortFields(Class clazz) {
        List<String> fields = new ArrayList<String>();
        List<String> listFields = listFieldMap.get(clazz.getName());
        List<String> shortFields = shortFieldMap.get(clazz.getName());
        if (listFields != null) {
            fields.addAll(listFields);
        }
        if (shortFields != null) {
            fields.addAll(shortFields);
        }
        return fields;
    }

    /**
     * 获取所有FULL的域，包括SHORT, LIST
     * @param clazz
     * @return
     */
    private static List<String> getFullFields(Class clazz) {
        List<String> fields = new ArrayList<String>();
        List<String> listFields = listFieldMap.get(clazz.getName());
        List<String> shortFields = shortFieldMap.get(clazz.getName());
        List<String> fullFields = fullFieldMap.get(clazz.getName());
        if (listFields != null) {
            fields.addAll(listFields);
        }
        if (shortFields != null) {
            fields.addAll(shortFields);
        }
        if (fullFields != null) {
            fields.addAll(fullFields);
        }
        return fields;
    }

    /**
     * 判断属性是否是基本类型
     * @param param
     * @return
     */
    private static boolean isFieldBasicType(Object param) {
        boolean result = false;
        if (param instanceof Integer) {
            result = true;
        }
        if (param instanceof String) {
            result = true;
        }
        if (param instanceof Double) {
            result = true;
        }
        if (param instanceof Float) {
            result = true;
        }
        if (param instanceof Long) {
            result = true;
        }
        if (param instanceof Boolean) {
            result = true;
        }
        return result;
    }
}
