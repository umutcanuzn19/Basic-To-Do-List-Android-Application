package prj_1.stu_1609640;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StoredData {

    SharedPreferences sp ;

    SharedPreferences.Editor edit ;


    public StoredData(Context c) {
        sp = PreferenceManager.getDefaultSharedPreferences(c);
        edit = sp.edit();
    }



    public void save(String key , String val){
        edit.putString(key,val);
        edit.commit();
    }

    public void clearData(){
        edit.clear();
        edit.commit();
    }
    public String getData(String key){
        return sp.getString(key,"");
    }
}
