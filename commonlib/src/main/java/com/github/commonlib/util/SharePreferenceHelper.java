package com.github.commonlib.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharePreferenceHelper {

    private Application mContext;

    /*****sp默认文件名*****/
    private static final String SP_DEFAULT_NAME = "xxx_sp";

    public SharePreferenceHelper(Application mContext) {
        this.mContext = mContext;
    }

    private SharedPreferences.Editor getSharePreferenceEditor(String fileName) {
        return getSharePreferences(fileName).edit();
    }

    private SharedPreferences getSharePreferences(String fileName) {
        return mContext.getSharedPreferences(TextUtils.isEmpty(fileName) ? SP_DEFAULT_NAME : fileName, Context.MODE_PRIVATE);
    }

    /**
     * 数据存储方法
     *
     * @param fileName 文件名，可为空有默认值
     * @param key      存储的key
     * @param object   储存的对象
     */
    public void put(String fileName, String key, Object object) {
        if (object == null) {
            return;
        }
        SharedPreferences.Editor editor = getSharePreferenceEditor(fileName);
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    public void put(String key, Object object) {
        this.put(null, key, object);
    }

    /**
     * 根据key取值
     *
     * @param fileName      文件名，可为空
     * @param key           键
     * @param defaultObject 默认值
     * @return
     */
    public Object get(String fileName, String key, Object defaultObject) {
        SharedPreferences sp = getSharePreferences(fileName);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public Object get(String key, Object defaultObject) {
        return this.get(null, key, defaultObject);
    }

    public String getString(String key, String defaultObject) {
        SharedPreferences sp = getSharePreferences(null);
        return sp.getString( key, defaultObject);
    }

    public boolean getBoolean(String key, boolean defaultObject) {
        SharedPreferences sp = getSharePreferences(null);
        return sp.getBoolean( key, defaultObject);
    }

    public int getInt(String key, int defaultObject) {
        SharedPreferences sp = getSharePreferences(null);
        return sp.getInt( key, defaultObject);
    }

    /**
     * 移除某个key对应的值
     *
     * @param fileName 文件名
     * @param key      key
     */
    public void remove(String fileName, String key) {
        SharedPreferences.Editor editor = getSharePreferenceEditor(fileName);
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    public void remove(String key) {
        this.remove(null, key);
    }

    /**
     * 清除所有的数据
     *
     * @param fileName 文件名
     */
    public void clear(String fileName) {
        SharedPreferences.Editor editor = getSharePreferenceEditor(fileName);
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    public void clear() {
        this.clear(null);
    }

    /**
     * 查询是否存在某个键
     *
     * @param fileName 文件名
     * @param key      键
     * @return
     */
    public boolean contains(String fileName, String key) {
        SharedPreferences sp = getSharePreferences(fileName);
        return sp.contains(key);
    }

    public boolean contains(String key) {
        return this.contains(null, key);
    }

    /**
     * map的形式存储数据
     *
     * @param maps
     */
    public void saveData(Map<String, String> maps) {
        this.saveData(maps, null);
    }

    public void saveData(Map<String, String> maps, String fileName) {
        if (null == maps || maps.size() == 0) return;
        SharedPreferences.Editor editor = getSharePreferenceEditor(fileName);
        for (Map.Entry<String, String> map : maps.entrySet()) {
            String key = map.getKey();
            String value = map.getValue();
            editor.putString(key, value);
        }
        editor.commit();
    }

    public void putData(String tag,Object t){
        SharedPreferences.Editor editor = getSharePreferenceEditor(null);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(t);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();
    }

    public <T> T getData(String tag){
        SharedPreferences preferences = getSharePreferences(null);
        String strJson = preferences.getString(tag, null);
        T t;
        if (null == strJson) {
            return null;
        }
        Gson gson = new Gson();
        t = gson.fromJson(strJson, new TypeToken<T>() {}.getType());
        return t;
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        SharedPreferences.Editor editor = getSharePreferenceEditor(null);
        if (null == datalist || datalist.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        SharedPreferences preferences = getSharePreferences(null);
        List<T> datalist = new ArrayList<T>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}
