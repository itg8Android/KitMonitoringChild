package itg8.com.kitmonitoringchild.CommonMethod;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by itg_Android on 9/24/2016.
 */
public class SharePrefrancClass {

    private static SharePrefrancClass ourInstance ;
    private static Context context;
    SharedPreferences preference;


    public static SharePrefrancClass getInstance(Context mcontext) {
        context = mcontext;
        if (ourInstance == null){
            ourInstance = new SharePrefrancClass();
        }
        return ourInstance;
    }

    private SharePrefrancClass() {
        preference = context.getSharedPreferences(CommonMethod.SHARED, Context.MODE_PRIVATE);
    }

    /**
     * savePref()  for save
     * @param key,value  Key value of Shared Prefrance
     * @return
     */
    public void savePref(String key, String val) {
        if (preference != null) {
            SharedPreferences.Editor editor = preference.edit();
            editor.putString(key, val);
            editor.apply();
        }
    }

    /**
     * setBoolean() for set
     * @param key,b  Key value of Shared Prefrance
     * @return
     */
    public void setPrefrance(String key, boolean b) {
        if (preference != null) {
            SharedPreferences.Editor editor = preference.edit();
            editor.putBoolean(key, b);
            editor.apply();
        }
    }

    /**
     * clearPrefra()  for delete
     * @param key Key value of Shared Prefrance
     * @return
     */
    public void clearPref(String key) {
        if (preference != null) {
            try {
                SharedPreferences.Editor editor = preference.edit();
                editor.remove(key);
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getString()  for use
     * @param key      Key value of Shared Prefrance
     * @return
     */

    public String getPref(String key) {
        if (preference != null) {
            return preference.getString(key, null);
        }
        return null;
    }

    /**
     * getBoolean()  for use
     * @param name Key value of Shared Prefrance
     * @return
     */
    public boolean hasPreference(String name) {
        if (preference != null) {
            return preference.getBoolean(name, false);
        }
        return false;
    }

}
