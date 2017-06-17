package com.example.gtk.platform.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtk.platform.R;
import com.example.gtk.platform.model.BaseResult;
import com.example.gtk.platform.model.Product;
import com.example.gtk.platform.model.UserInfo;
import com.example.gtk.platform.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PurchaseActivity extends Activity {
    private TextView titleTextView;
    private TextView priceTextView;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private RadioGroup methodRadioGroup;
    private int selectedType;
    private int selectItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        String name = this.getIntent().getExtras().getString("gameName");
        titleTextView = (TextView)findViewById(R.id.purchase_game_name);
        priceTextView = (TextView)findViewById(R.id.purchase_game_price);
        nameEditText = (EditText)findViewById(R.id.purchase_name);
        addressEditText = (EditText)findViewById(R.id.purchase_address);
        phoneEditText = (EditText)findViewById(R.id.purchase_phone);
        methodRadioGroup = (RadioGroup)findViewById(R.id.purchase_method);

        new NetWorkTask().execute(name);

        Button resumeButton = (Button) findViewById(R.id.resume_index);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button purchaseButton = (Button)findViewById(R.id.purchase_button);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserInfo.bLogin) {
                    Toast.makeText(PurchaseActivity.this, "请先登录！", Toast.LENGTH_LONG).show();
                    return;
                }

                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                new SavePurchaseInfoTask().execute(name, address, phone, priceTextView.getText().toString(), selectedType+"", UserInfo.userId+"", selectItem+"");
            }
        });

        methodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radio_pay_pal:
                        selectedType = 1;
                        Log.d("GTK", "pay pal is selected");
                        break;
                    case R.id.radio_master_card:
                        selectedType = 2;
                        Log.d("GTK", "master card id selected");
                        break;
                    case R.id.radio_visa_card:
                        selectedType = 3;
                        Log.d("GTK", "visa card is selected");
                        break;
                    default:  break;
                }
            }
        });
    }

    class NetWorkTask extends AsyncTask<String, Void, Product> {
        @Override
        protected Product  doInBackground(String... params) {
            Product product = null;
            String uri = HttpUtils.PRODUCT_URL + "/getProductByTitle";
            Map<String, String> productMap = new HashMap<>();
            productMap.put("title", params[0]);

            InputStream inputStream = HttpUtils.post(uri,productMap);
            if (inputStream != null) {
                String responseText = HttpUtils.inputStream2String(inputStream);
                Gson gson = new Gson();

                product = gson.fromJson(responseText, Product.class);
                Log.e("TEST", responseText);
            }

            return product;
        }

        @Override
        protected void onPostExecute(Product  result) {
            if(result == null)
                return;

            titleTextView.setText(result.getTitle());
            priceTextView.setText(result.getPrice() + "RMB");
            selectItem = result.getId();
        }
    }

    class SavePurchaseInfoTask extends AsyncTask<String, Void, BaseResult<String>>{
        @Override
        protected BaseResult<String> doInBackground(String... params) {
            BaseResult<String> baseResult = null;
            Map<String, String> map = new HashMap<>();
            map.put("name", params[0]);
            map.put("address", params[1]);
            map.put("phone", params[2]);
            map.put("money", params[3].substring(0, params[3].length()-3));
            map.put("payMethod", params[4]);
            map.put("userId", params[5]);
            map.put("proId", params[6]);

            String url = HttpUtils.USER_URL + "/savePurchaseInfo";
            InputStream inputStream = HttpUtils.post(url, map);
            if (inputStream != null){
                String result = HttpUtils.inputStream2String(inputStream);
                Gson gson = new Gson();
                baseResult = gson.fromJson(result, BaseResult.class);
                Log.e("GTK", result);
            }

            return baseResult;
        }

        @Override
        protected void onPostExecute(BaseResult<String> baseResult) {
            if (baseResult == null) {
                Toast.makeText(PurchaseActivity.this, "对象为空", Toast.LENGTH_LONG).show();
                return;
            }
            if (baseResult.isSuccess()) {
                Toast.makeText(PurchaseActivity.this, baseResult.getMessage(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(PurchaseActivity.this, baseResult.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
