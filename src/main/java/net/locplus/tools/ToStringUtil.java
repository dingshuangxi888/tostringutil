package net.locplus.tools;

import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ToStringUtil for Objects that @ToString at fields
 * Created by Dean on 14/9/27.
 */
public class ToStringUtil {
    private final static Logger logger = LoggerFactory.getLogger(ToStringUtil.class);

    enum ResultCode {
        NO_LIST_TO_STRING, NO_SHORT_TO_STRING, NO_FULL_TO_STRING;
    }

    /**
     * Default toString method, SHORT toString
     * @param object
     * @return
     */
    public static String toString(Object object) {
        if (object == null) {
            return null;
        }
        return doShortToString(object);
    }

    /**
     * type is LIST to String method
     * @param list
     * @return
     */
    public static String listToString(List list) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return list.toString();
        }
        return doListToString(list, false);
    }

    /**
     * type is FULL to String method
     * @param object
     * @return
     */
    public static String fullToString(Object object) {
        if (object == null) {
            return null;
        }
        return doFullToString(object);
    }

    /* short to string action method */
    private static String doShortToString(Object object) {
        if (!ToStringAnnotationExplainer.contains(object.getClass())) {
            return ResultCode.NO_SHORT_TO_STRING.toString();
        }
        List<String> shortFields = ToStringCache.getShortFields(object.getClass());
        if (shortFields == null || shortFields.isEmpty()) {
            return ResultCode.NO_SHORT_TO_STRING.toString();
        }
        return doToString(object, shortFields, false);
    }

    /* list to string action method */
    private static String doListToString(List list, boolean isFull) {
        List<String> result = new ArrayList<String>();
        for (Object object : list) {
            if (ToStringAnnotationExplainer.contains(object.getClass())) {
                if (isFull) {
                    List<String> listFields = ToStringCache.getListFields(object.getClass());
                    if (listFields == null || listFields.isEmpty()) {
                        result.add(ResultCode.NO_LIST_TO_STRING.toString());
                    } else {
                        result.add(doToString(object, listFields, false));
                    }
                } else {
                    List<String> fullFields = ToStringCache.getFullFields(object.getClass());
                    if (fullFields == null || fullFields.isEmpty()) {
                        result.add(ResultCode.NO_FULL_TO_STRING.toString());
                    } else {
                        result.add(doToString(object, fullFields, false));
                    }
                }
            } else {
                result.add(object.toString());
            }
        }

        return result.toString();
    }

    /* full to string action method */
    private static String doFullToString(Object object) {
        if (!ToStringAnnotationExplainer.contains(object.getClass())) {
            return ResultCode.NO_FULL_TO_STRING.toString();
        }
        List<String> fullFields = ToStringCache.getFullFields(object.getClass());
        if (fullFields == null || fullFields.isEmpty()) {
            return ResultCode.NO_FULL_TO_STRING.toString();
        }
        return doToString(object, fullFields, true);
    }

    /* to string action method */
    private static String doToString(Object object, List<String> fields, boolean isFull) {
        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(object);
        for (String fieldName : fields) {
            Object value = getFieldValue(object, fieldName);
            if (value != null && value instanceof List) {
                toStringHelper.add(fieldName, doListToString((List) value, isFull));
            } else {
                toStringHelper.add(fieldName, value);
            }
        }
        return toStringHelper.toString();
    }

    /* get value of object's field */
    public static Object getFieldValue(Object object, String fieldName) {
        Object result = null;
        Class clazz = object.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (fieldName.length() > 2 && "boolean".equals(field.getType().getName()) && ((fieldName.startsWith("is") || fieldName.startsWith("Is")) && Character.isUpperCase(fieldName.charAt(2)))) {
                String methodName = "is" + fieldName.substring(2);
                Method method = clazz.getMethod(methodName);
                return method.invoke(object);
            }
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            if (pds != null) {
                for (PropertyDescriptor pd : pds) {
                    if (pd.getName().equals(fieldName)) {
                        Method method = pd.getReadMethod();
                        result = method.invoke(object);
                        break;
                    }
                }
            }
//            PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
//            Method getMethod = pd.getReadMethod();
//            if (getMethod != null) {
//                result = getMethod.invoke(object);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Method generateGetMethod(Class clazz, String fieldName) {
        try {
            String methodName = null;
            Field field = clazz.getDeclaredField(fieldName);
            if ("boolean".equals(field.getType().getName()) && (fieldName.startsWith("is") || fieldName.startsWith("Is"))) {
                methodName = "is" + fieldName.substring(2);
                Method method = clazz.getMethod(methodName);
                return method;
            }
            char first = fieldName.charAt(0);
            char second = fieldName.charAt(1);
            if (Character.isUpperCase(second)) {
                methodName = "get" + fieldName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @ToString fields Cache
     */
    private static class ToStringCache {

        /** Cache of @ToString fields */
        private static Map<String, ToStringFields> fieldsMap = new HashMap<String, ToStringFields>();

        /*
         * Get all List fields by Class Name
         */
        private static List<String> getListFields(Class clazz) {
            String clazzName = clazz.getName();
            ToStringFields fields = fieldsMap.get(clazzName);
            if (fields == null) {
                fields = ToStringAnnotationExplainer.explain(clazz);
                if (fields == null) {
                    return null;
                }
            }
            return fields.getAllListFields();
        }

        /*
         * Get all Short fields by Class Name
         */
        private static List<String> getShortFields(Class clazz) {
            String clazzName = clazz.getName();
            ToStringFields fields = fieldsMap.get(clazzName);
            if (fields == null) {
                fields = ToStringAnnotationExplainer.explain(clazz);
                if (fields == null) {
                    return null;
                }
            }
            return fields.getAllShortFields();
        }

        /*
         * Get all Short fields by Class Name
         */
        private static List<String> getFullFields(Class clazz) {
            String clazzName = clazz.getName();
            ToStringFields fields = fieldsMap.get(clazzName);
            if (fields == null) {
                fields = ToStringAnnotationExplainer.explain(clazz);
                if (fields == null) {
                    return null;
                }
            }
            return fields.getAllFullFields();
        }

        /*
         * Fields of Object, include LIST, SHORT, FULL
         */
        private static class ToStringFields {
            /* fields @ToString type = LIST */
            private List<String> listFields = new ArrayList<String>();
            /* fields @ToString type = SHORT */
            private List<String> shortFields = new ArrayList<String>();
            /* fields @ToString type = FULL */
            private List<String> fullFields = new ArrayList<String>();

            private ToStringFields(List<String> listFields, List<String> shortFields, List<String> fullFields) {
                this.listFields = listFields;
                this.shortFields = shortFields;
                this.fullFields = fullFields;
            }

            /* get all list fields */
            private List<String> getAllListFields() {
                return listFields;
            }

            /* get all short fields, include list fields */
            private List<String> getAllShortFields() {
                List<String> result = new ArrayList<String>();
                result.addAll(shortFields);
                result.addAll(getAllListFields());
                return result;
            }

            /* get all full fields, include list and short fields */
            private List<String> getAllFullFields() {
                List<String> result = new ArrayList<String>();
                result.addAll(getAllListFields());
                result.addAll(fullFields);
                return result;
            }
        }
    }

    /* @ToString annotation explainer */
    private static class ToStringAnnotationExplainer {

        /* Explain @ToString annotation */
        private static ToStringCache.ToStringFields explain(Class clazz) {
            ToStringCache.ToStringFields result = null;
            List<String> listFields = new ArrayList<String>();
            List<String> shortFields = new ArrayList<String>();
            List<String> fullFields = new ArrayList<String>();

            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ToString.class)) {
                        ToString toString = field.getAnnotation(ToString.class);
                        if (toString.value() == ToStringType.LIST) {
                            listFields.add(field.getName());
                        }
                        if (toString.value() == ToStringType.SHORT) {
                            shortFields.add(field.getName());
                        }
                        if (toString.value() == ToStringType.FULL) {
                            fullFields.add(field.getName());
                        }
                    }
                }
            }
            if (!listFields.isEmpty() || !shortFields.isEmpty() || !fullFields.isEmpty()) {
                result = new ToStringCache.ToStringFields(listFields, shortFields, fullFields);
                ToStringCache.fieldsMap.put(clazz.getName(), result);
            }
            return result;
        }

        /* Judge is clazz contains @ToString annotation */
        private static boolean contains(Class clazz) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ToString.class)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
