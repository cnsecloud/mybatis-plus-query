package com.cnsecloud.plugins.mybatis.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * 工具类，参考 Hutool
 * </p>
 *
 * @author ZhangHongYuan
 * @date 2020/11/17
 */
public class Utils {

    private static final WeakHashMap<Class<?>,List<Field>> CLASS_CACHE = new WeakHashMap<>();
    private static final ReentrantReadWriteLock CLASS_CACHE_READ_WRITE_LOCK = new ReentrantReadWriteLock();

    public static boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 获得一个类中所有字段列表，包括其父类中的字段
     *
     * @param beanClass 类
     * @return 字段列表
     * @throws SecurityException 安全检查异常
     */
    public static List<Field> getFields(Class<?> beanClass) throws SecurityException {
        List<Field> allFields = getClassCache(beanClass);
        if (null != allFields) {
            return allFields;
        }

        allFields = getFieldsDirectly(beanClass, true);
        return putClassCache(beanClass, allFields);
    }

    private static List<Field> putClassCache(Class<?> key,List<Field> value){
        CLASS_CACHE_READ_WRITE_LOCK.writeLock().lock();
        try {
            CLASS_CACHE.put(key, value);
        } finally {
            CLASS_CACHE_READ_WRITE_LOCK.writeLock().unlock();
        }
        return value;
    }

    private static List<Field> getClassCache(Class<?> key){
        CLASS_CACHE_READ_WRITE_LOCK.readLock().lock();
        try {
            return CLASS_CACHE.get(key);
        } finally {
            CLASS_CACHE_READ_WRITE_LOCK.readLock().unlock();
        }
    }

    /**
     * 获得一个类中所有字段列表，直接反射获取，无缓存
     *
     * @param beanClass           类
     * @param withSuperClassFieds 是否包括父类的字段列表
     * @return 字段列表
     * @throws SecurityException 安全检查异常
     */
    public static List<Field> getFieldsDirectly(Class<?> beanClass, boolean withSuperClassFieds) throws SecurityException {
        if(beanClass == null){
            throw new IllegalArgumentException("[Assertion failed] - this argument is required; it must not be null");
        }
        List<Field> allFields = new ArrayList<>();
        Class<?> searchType = beanClass;
        Field[] declaredFields;
        while (searchType != null) {
            declaredFields = searchType.getDeclaredFields();
            allFields.addAll(Arrays.asList(declaredFields));
            searchType = withSuperClassFieds ? searchType.getSuperclass() : null;
        }

        return allFields;
    }

    /**
     * 获取字段值
     *
     * @param obj   对象，static字段则此字段为null
     * @param field 字段
     * @return 字段值
     */
    public static Object getFieldValue(Object obj, Field field) throws IllegalAccessException {
        if (null == field) {
            return null;
        }
        if (obj instanceof Class) {
            // 静态字段获取时对象为null
            obj = null;
        }

        setAccessible(field);
        return field.get(obj);
    }

    public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
        if (null != accessibleObject && false == accessibleObject.isAccessible()) {
            accessibleObject.setAccessible(true);
        }
        return accessibleObject;
    }
}
