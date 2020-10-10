package com.kulala.staticsfunc.static_assistant;

import java.lang.reflect.Field;

public class CompareObject {
    public static boolean compareIsSameData(Object obj1, Object obj2) {
        Class<?> clazz1 = obj1.getClass();
        Class<?> clazz2 = obj2.getClass();
        Field[]  field1 = clazz1.getDeclaredFields();
        Field[]  field2 = clazz2.getDeclaredFields();
        for (int i = 0; i < field1.length; i++) {
            for (int j = 0; j < field2.length; j++) {
                if (field1[i].getName().equals(field2[j].getName())) {//如果是同名的字段
                    field1[i].setAccessible(true);
                    field2[j].setAccessible(true);
                    String type = field1[i].getType().getName();
                    try {
                        if (type.equals("int") || type.equals("Integer")) {
                            if (field1[i].getInt(obj1) != field2[i].getInt(obj2)) return false;
                        } else if (type.equals("long") || type.equals("Long")) {
                            if (field1[i].getLong(obj1) != field2[i].getLong(obj2)) return false;
                        } else if (type.equals("float") || type.equals("Float")) {
                            if (field1[i].getFloat(obj1) != field2[i].getFloat(obj2)) return false;
                        } else if (type.equals("double") || type.equals("Double")) {
                            if (field1[i].getDouble(obj1) != field2[i].getDouble(obj2))return false;
                        } else if (type.equals("string") || type.equals("String")) {
                            if (!((String) (field1[i].get(obj1))).equals(((String) (field2[i].get(obj2)))))return false;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return true;
    }

}
