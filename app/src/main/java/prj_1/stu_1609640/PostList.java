package prj_1.stu_1609640;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

public class PostList extends AppCompatActivity {
    ListView listView;
    JSONArray dataSet = new JSONArray();
    BaseAdapter ba;
    String account_id,display_name,user_name,phone,country, password;
    String post_id;
    ArrayList<String> itemList = new ArrayList<>();
    StoredData storedData ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);
        storedData = new StoredData(PostList.this);
        listView = findViewById(R.id.listView);
        String data = getIntent().getExtras().getString("accountId");
        String data2 = getIntent().getExtras().getString("display_name");
        String data3 = getIntent().getExtras().getString("user_name");
        String data4 = getIntent().getExtras().getString("phone");
        String data5 = getIntent().getExtras().getString("country");
        String data6 = getIntent().getExtras().getString("password");
        display_name = data2 ;
        user_name = data3 ;
        phone = data4 ;
        country = data5 ;
        password = data6 ;

        if(data!= null) {
            account_id = data;
        };

        ba = new BaseAdapter() {
            @Override
            public int getCount() {
                return dataSet.length();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.post, null);
                }
                TextView tvTitle = convertView.findViewById(R.id.tvTitle);
                TextView tvText = convertView.findViewById(R.id.tvText);
                try {
                    JSONObject jo = dataSet.getJSONObject(position);
                    tvTitle.setText(jo.getString("title"));
                    tvText.setText(jo.getString("txt"));
                    itemList.add(jo.getString("id"));
                    post_id = jo.getString("id");
                } catch (Exception e) {
                    Log.e("getView", "sıkıntı " + e);
                }
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        final int selectedIndex = position;
                        AlertDialog.Builder adb = new AlertDialog.Builder(PostList.this);
                        adb.setTitle("Delete Task");
                        adb.setMessage("Are you sure to delete selected Task ?");
                        adb.setNegativeButton("Cancel", null);
                        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("Account id ", account_id);
                                Log.e("Post_id", post_id);
                                deleteTask("del_post", account_id, "1609640", itemList.get(selectedIndex));
                                itemList.remove(selectedIndex);
                                Log.e("Silme işlemi", "Silme işlemi başarıyla gerçekleşmiştir deletetaskın hemen altı");
                                dialog.dismiss();
                                ba.notifyDataSetChanged();
                                getData("list_posts", account_id, "1609640");
                            }
                        });
                        adb.show();
                        return true;
                    }
                });
                ba.notifyDataSetChanged();
                return convertView;
            }
        };
        listView.setAdapter(ba);
        getData("list_posts", account_id, "1609640");
        //deleteTask("del_post",account_id,"1609640","1");


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Account Info").setIcon(R.drawable.account_info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("Add Task").setIcon(R.drawable.add_task).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("Log Out").setTitle("Log Out").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String buttonName = item.getTitle().toString();
        if (buttonName.equals("Add Task")) {
            Intent i = new Intent(PostList.this, Post.class);
            i.putExtra("accountId", account_id);
            i.putExtra("display_name", display_name);
            i.putExtra("user_name", user_name);
            i.putExtra("phone", phone);
            i.putExtra("country", country);
            i.putExtra("password", password);
            startActivity(i);
        }else if (buttonName.equals("Account Info")){
            Intent i = new Intent(this,AccountInfo.class);
            i.putExtra("accountId", account_id);
            i.putExtra("display_name", display_name);
            i.putExtra("user_name", user_name);
            i.putExtra("phone", phone);
            i.putExtra("country", country);
            i.putExtra("password", password);
            Log.e("Cridentials " , account_id + display_name+user_name+phone + country + password );
            startActivity(i);
        }else if(buttonName.equals("Log Out")){
            Intent i = new Intent(this,MainActivity.class);
            storedData.clearData();
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData(String op, String id, String shash) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... p) {

                try {
                    String str = Jsoup.connect("https://tux.csicxt.com/index.php").ignoreContentType(true)
                            .data("op", p[0])
                            .data("id", p[1], "shash", p[2])
                            .post()
                            .text();
                    dataSet = new JSONArray(str);
                } catch (Exception e) {
                    Log.e("postlist info ", "Sıkıntı var bir yerde " + e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                ba.notifyDataSetChanged();
            }
        }.execute(op, id, shash);
    }

    public void deleteTask(String op, String account_id, String shash, String post_id) {
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... p) {

                try {
                    String str = Jsoup.connect("https://tux.csicxt.com/index.php").ignoreContentType(true)
                            .data("op", p[0])
                            .data("account_id", p[1], "shash", p[2], "post_id", p[3])
                            .post()
                            .text();

                    JSONObject jo = new JSONObject(str);
                    Log.e("Server Response", jo.getString("r"));
                    return jo.getString("r");
                } catch (Exception e) {
                    Log.e("Silme işlemi", "bir hata var " + e);
                    return e.toString();
                }

            }

            @Override
            protected void onPostExecute(String s) {
                ba.notifyDataSetChanged();
            }

        }.execute(op, account_id, shash, post_id);
    }


}
