package prj_1.stu_1609640;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;

public class Register extends AppCompatActivity {

    EditText etUserName, etPassword, etPhone, etCountry, etDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etCountry = findViewById(R.id.etCountry);
        etDisplayName = findViewById(R.id.etDisplayName);

    }


    public void doRegister(View view) {
        postData("register", "1609640", etUserName.getText().toString(), etPassword.getText().toString(), etPhone.getText().toString(), etCountry.getText().toString(), etDisplayName.getText().toString());
        Log.e("register", "başarılı");
    }

    public void postData(String op, String shash, String etUserName, String etPassword, String etPhone, String etCountry, String etDisplayName) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... p) {
                try {
                    String registerData = Jsoup.connect("https://tux.csicxt.com/index.php").ignoreContentType(true)
                            .data("op", p[0])
                            .data("shash", p[1], "un", p[2], "pw", p[3], "phone", p[4], "country", p[5], "dn", p[6])
                            .post()
                            .text();
                    JSONObject jo = new JSONObject(registerData);
                    Log.e("try", "try kısmı burası");
                    Log.e("Başarılı post ", jo.getString("r"));
                    return jo.getString("r");
                } catch (Exception e) {
                    Log.e("try", "catch kısmı burası");
                    Log.e("e", "burası catchteki hata " + e.toString());
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                Intent i = new Intent(Register.this , MainActivity.class);
                startActivity(i);
            }


        }.execute(op, shash, etUserName, etPassword, etPhone, etCountry, etDisplayName);
    }

}
