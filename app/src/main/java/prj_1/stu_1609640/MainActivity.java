package prj_1.stu_1609640;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;

public class MainActivity extends AppCompatActivity {

    EditText etUserName, etPassword;
    String user_id,display_name,user_name , phone , country , password;
    StoredData storedData ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        storedData = new StoredData(MainActivity.this);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        try {
            postData("login",storedData.getData("user_name"),storedData.getData("password"),"1609640");
        }catch (Exception e ){

        }
    }

    public void doLogin(View view) {

        postData("login", etUserName.getText().toString(), etPassword.getText().toString(), "1609640");

        Log.e("login", "başarılı");
    }

    public void postData(String op, String etUserName, String etPassword, String shash) {


        new AsyncTask<String, String, String>() {
            String loginState;

            @Override
            protected String doInBackground(String... p) {

                try {
                    String loginData = Jsoup.connect("https://tux.csicxt.com/index.php").ignoreContentType(true)
                            .data("op", p[0])
                            .data("un", p[1], "pw", p[2], "shash", p[3])
                            .post()
                            .text();
                    JSONObject jo = new JSONObject(loginData);
                    Log.e("try", "try kısmı burası");
                    Log.e("Başarılı post ", jo.getString("r"));
                    loginState = jo.getString("r");
                    JSONObject user = jo.getJSONObject("user");
                    user_id = user.getString("id");
                    display_name = user.getString("display_name");
                    user_name = user.getString("un");
                    phone = user.getString("phone");
                    country = user.getString("country");

                    if(jo.getString("r").equals("1")){
                        password = etPassword ;
                    }

                    return jo.getString("r");
                } catch (Exception e) {
                    Log.e("try", "catch kısmı burası");
                    Log.e("e", "burası catchteki hata " + e.toString());
                    return e.toString();
                }

            }

            @Override
            protected void onPostExecute(String s) {
                Log.e("loginstate ", loginState);
                if (loginState.equals("1")) {
                    Intent i = new Intent(MainActivity.this, PostList.class);
                    i.putExtra("accountId", user_id);
                    i.putExtra("display_name", display_name);
                    i.putExtra("user_name", user_name);
                    i.putExtra("phone", phone);
                    i.putExtra("country", country);
                    i.putExtra("password" , password);

                    storedData.save("accountId", user_id);
                    storedData.save("display_name", display_name);
                    storedData.save("user_name", user_name);
                    storedData.save("phone", phone);
                    storedData.save("country", country);
                    storedData.save("password", password);

                    startActivity(i);
                } else if (loginState.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Login işleminizde problem var", Toast.LENGTH_LONG).show();
                }

            }
        }.execute(op, etUserName, etPassword, shash);


    }


    public void doRegister(View view) {
        Intent i = new Intent(MainActivity.this, Register.class);

        startActivity(i);
    }
}