package com.example.gtk.platform.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtk.platform.R;
import com.example.gtk.platform.model.BaseResult;
import com.example.gtk.platform.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {
    private TextView accountView;
    private TextView passwordView;
    private TextView nameView;
    private TextView emailView;
    private TextView phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        accountView = (TextView)findViewById(R.id.account);
        passwordView = (TextView)findViewById(R.id.password);
        nameView = (TextView)findViewById(R.id.username);
        emailView = (TextView)findViewById(R.id.useremail);
        phoneView = (TextView)findViewById(R.id.userphone);

        Button button = (Button)findViewById(R.id.registerButton);
        TextView resumeTextView = (TextView)findViewById(R.id.resumeTextView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)RegisterActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 得到InputMethodManager的实例
                if (imm.isActive()) {
                    // 如果开启
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                            InputMethodManager.HIDE_NOT_ALWAYS);

                }
                new NetWorkTask().execute(accountView.getText().toString(), passwordView.getText().toString(),
                        nameView.getText().toString(), emailView.getText().toString(), phoneView.getText().toString());
            }
        });

        resumeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    class NetWorkTask extends AsyncTask<String, Void, BaseResult<String>> {
        @Override
        protected BaseResult<String> doInBackground(String... params) {
            Map<String, String> userMap = new HashMap<>();
            userMap.put("account", params[0]);
            userMap.put("password", params[1]);
            userMap.put("name", params[2]);
            userMap.put("emailAddr", params[3]);
            userMap.put("phone", params[4]);

            InputStream inputStream = HttpUtils.post(HttpUtils.USER_URL+"/register", userMap);
            BaseResult<String> baseResult = null;
            if (inputStream != null){
                Gson gson = new Gson();
                String content = HttpUtils.inputStream2String(inputStream);
                Log.e("GTK", content);
                baseResult = gson.fromJson(content, BaseResult.class);
                Log.e("GTK", baseResult.getMessage());
            }
            return baseResult;
        }

        @Override
        protected void onPostExecute(BaseResult<String> stringBaseResult) {
            if (stringBaseResult == null) {
                Toast.makeText(RegisterActivity.this, "对象为空", Toast.LENGTH_LONG).show();
                return;
            }

            if (stringBaseResult.isSuccess()) {
                Toast.makeText(RegisterActivity.this, stringBaseResult.getMessage(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(RegisterActivity.this, stringBaseResult.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
