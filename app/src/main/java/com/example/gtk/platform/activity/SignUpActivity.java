package com.example.gtk.platform.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gtk.platform.R;
import com.example.gtk.platform.model.UserInfoModel;
import com.example.gtk.platform.utils.HttpUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends Activity {
    private final String Tags = "gtk.test";
    final String urls = "http://192.168.43.36:8080/users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button logInButton = (Button)findViewById(R.id.login_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoModel userInfoModel = new UserInfoModel(((EditText)findViewById(R.id.acc_text)).getText().toString(),
                        ((EditText)findViewById(R.id.pass_text)).getText().toString());
                new NetWorkTask().execute(userInfoModel.getUserAcc(), userInfoModel.getUserPass());
            }
        });
    }

    class NetWorkTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            Log.i(Tags, "Http Connection");
            String responseContent = "";
            Map<String, String> userMap = new HashMap<String, String>();
            userMap.put("userAcc", params[0]);
            userMap.put("userPass", params[1]);
            InputStream inputStream = HttpUtils.post(urls + "/login", userMap);
            if (inputStream != null) {
                responseContent = HttpUtils.inputStream2String(inputStream);
            }
            return responseContent;
        }

        @Override
        protected void onPostExecute(String result) {
            InputMethodManager imm = (InputMethodManager)SignUpActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            // 得到InputMethodManager的实例
            if (imm.isActive()) {
                // 如果开启
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
            Toast.makeText(SignUpActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
