package com.example.gtk.platform.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtk.platform.R;
import com.example.gtk.platform.model.BaseResult;
import com.example.gtk.platform.model.UserInfo;
import com.example.gtk.platform.model.UserInfoModel;
import com.example.gtk.platform.model.UserStatus;
import com.example.gtk.platform.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends Activity {
    private final String Tags = "gtk.test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button logInButton = (Button)findViewById(R.id.login_in_button);
        TextView registerTextView = (TextView)findViewById(R.id.register);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)SignUpActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 得到InputMethodManager的实例
                if (imm.isActive()) {
                    // 如果开启
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                            InputMethodManager.HIDE_NOT_ALWAYS);

                }
                UserInfoModel userInfoModel = new UserInfoModel(((EditText)findViewById(R.id.acc_text)).getText().toString(),
                        ((EditText)findViewById(R.id.pass_text)).getText().toString());
                new NetWorkTask().execute(userInfoModel.getUserAcc(), userInfoModel.getUserPass());
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent integer = new Intent(SignUpActivity.this, RegisterActivity.class);
                startActivity(integer);
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
            InputStream inputStream = HttpUtils.post(HttpUtils.USER_URL + "/login", userMap);
            if (inputStream != null) {
                responseContent = HttpUtils.inputStream2String(inputStream);
            }
            return responseContent;
        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson =  new Gson();
            Type type = new TypeToken<BaseResult<UserStatus>>(){}.getType();
            BaseResult<UserStatus> baseResult = gson.fromJson(result, type);
            Toast.makeText(SignUpActivity.this, baseResult.getMessage(), Toast.LENGTH_LONG).show();
            if (baseResult.isSuccess()){
                //存储全局信息
                UserInfo.bLogin = baseResult.getData().isbLogin();
                UserInfo.userName = baseResult.getData().getName();
                UserInfo.userId = baseResult.getData().getUserId();

                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

}
