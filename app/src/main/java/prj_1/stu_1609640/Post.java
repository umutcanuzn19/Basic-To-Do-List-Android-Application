package prj_1.stu_1609640;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;

public class Post extends AppCompatActivity {

    EditText etText, etTitle;
    String account_id , user_name , display_name , country , phone , password ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.add_post);
        etText = findViewById(R.id.etText);
        etTitle = findViewById(R.id.etTitle);
        account_id = getIntent().getExtras().getString("accountId");
        user_name = getIntent().getExtras().getString("user_name");
        display_name = getIntent().getExtras().getString("display_name");
        country = getIntent().getExtras().getString("country");
        phone = getIntent().getExtras().getString("phone");
        password = getIntent().getExtras().getString("password");

        super.onCreate(savedInstanceState);
    }


    public void postTask(View view) {
        postData("add_post", account_id, "1609640", etTitle.getText().toString(), etText.getText().toString());
        Log.e("postTask", "başarılı");
    }


    public void postData(String op, String id, String shash, String etTitle, String etText) {

        new AsyncTask<String, String, String>() {

            String postState;

            @Override
            protected String doInBackground(String... p) {

                try {

                    String postTask = Jsoup.connect("https://tux.csicxt.com/index.php").ignoreContentType(true)
                            .data("op", p[0])
                            .data("id", p[1], "shash", p[2], "title", p[3], "txt", p[4])
                            .post()
                            .text();
                    JSONObject jo = new JSONObject(postTask);
                    Log.e("Post durumu ", jo.getString("r"));
                    postState = jo.getString("r");
                    return jo.getString("r");
                } catch (Exception e) {
                    Log.e("Hata kısmı", e.toString());
                    return e.toString();
                }


            }

            @Override
            protected void onPostExecute(String s) {
                if (postState.equals("1")) {
                    Intent i = new Intent(Post.this, PostList.class);
                    i.putExtra("accountId",account_id);
                    i.putExtra("display_name", display_name);
                    i.putExtra("user_name", user_name);
                    i.putExtra("phone", phone);
                    i.putExtra("country", country);
                    i.putExtra("password", password);
                    startActivity(i);
                } else if (postState.equals(("0"))) {
                    Toast.makeText(getApplicationContext(), "Post işleminizde problem var", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(op, String.valueOf(id), shash, etTitle, etText);

    }


}
