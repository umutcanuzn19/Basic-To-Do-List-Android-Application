package prj_1.stu_1609640;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;

public class AccountInfo extends AppCompatActivity {

    TextView tvUserName, tvPassword, tvCountry, tvPhone, tvDisplayName, tvAccountId;
    final Context context = this;
    String newPassword ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        tvUserName = findViewById(R.id.tvUserName);
        tvPassword = findViewById(R.id.tvPassword);
        tvCountry = findViewById(R.id.tvCountry);
        tvPhone = findViewById(R.id.tvPhone);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        tvAccountId = findViewById(R.id.tvAccountId);

        String data1 = getIntent().getExtras().getString("accountId");
        String data2 = getIntent().getExtras().getString("display_name");
        String data3 = getIntent().getExtras().getString("user_name");
        String data4 = getIntent().getExtras().getString("phone");
        String data5 = getIntent().getExtras().getString("country");
        String data6 = getIntent().getExtras().getString("password");

        tvUserName.setText(data3);
        tvPassword.setText(data6);
        tvCountry.setText(data5);
        tvPhone.setText(data4);
        tvDisplayName.setText(data2);
        tvAccountId.setText(data1);
        Log.e("Account id " ,tvAccountId.getText().toString());


    }

    /*
        public void changePassword(View view) {

            final Dialog passwordDialog  = new Dialog(this);
            passwordDialog.setContentView(R.layout.change_password);
            final EditText  etOldPassword = passwordDialog.findViewById(R.id.etOldPassword);
            final EditText  etNewPassword = passwordDialog.findViewById(R.id.etNewPassword);

            final Button changePasswordButton = passwordDialog.findViewById(R.id.changePassword);
            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 postData("change_password",tvAccountId.toString() ,etOldPassword.toString() ,etNewPassword.toString(),"1609640");

                }
            });

        }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Change Password").setIcon(R.drawable.change_password).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String selName = item.getTitle().toString();
        Log.e("selnamein hemen altı", "Buraya geldi");
        if (selName.equals("Change Password")) {
            Log.e("ifin içi", "Buraya geldi");
            final Dialog passwordDialog = new Dialog(context);
            passwordDialog.setContentView(R.layout.change_password);
            final EditText etOldPassword = passwordDialog.findViewById(R.id.etOldPassword);
            final EditText etNewPassword = passwordDialog.findViewById(R.id.etNewPassword);

            final Button changePasswordButton = passwordDialog.findViewById(R.id.changePassword);
            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("selnamein hemen altı", "Buraya geldi");

                    postData("change_password", tvAccountId.getText().toString(), etOldPassword.getText().toString(), etNewPassword.getText().toString(), "1609640");
                    newPassword = etNewPassword.getText().toString();
                    passwordDialog.dismiss();
                }

            });
            passwordDialog.show();



        }
        return super.onOptionsItemSelected(item);
    }

    public void postData(String op, String id, String opw, String npw, String shash) {

        new AsyncTask<String, String, String>() {


            @Override
            protected String doInBackground(String... p) {

                try {
                    String str = Jsoup.connect("https://tux.csicxt.com/index.php").ignoreContentType(true)
                            .data("op", p[0])
                            .data("id", p[1], "opw", p[2], "npw", p[3], "shash", p[4])
                            .post()
                            .text();
                    JSONObject jo = new JSONObject(str);
                    Log.e("JsonObject", jo.getString("r"));
                    return jo.getString("r");
                } catch (Exception e) {
                    Log.e("Hatalı ", e.toString());
                    return e.toString();
                }


            }


        }.execute(op, id, opw, npw, shash);


    }


}
