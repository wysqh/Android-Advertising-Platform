package com.example.gtk.platform.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.gtk.platform.R;
import com.example.gtk.platform.adapter.MyAdapter;
import com.example.gtk.platform.model.BaseResult;
import com.example.gtk.platform.model.Product;
import com.example.gtk.platform.model.UserStatus;
import com.example.gtk.platform.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity {
    private GridView gridView;
    private List<String> images;
    private List<String> texts;
    private List<String> prices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        images = new ArrayList<>();
        texts = new ArrayList<>();
        prices = new ArrayList<>();
        new NetWorkTask().execute();
    }

    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    private class NetWorkTask extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... params) {
            publishProgress();
            String responseText = null;
            InputStream inputStream = HttpUtils.post(HttpUtils.PRODUCT_URL + "/getProducts", null);
            if (inputStream != null) {
                responseText = HttpUtils.inputStream2String(inputStream);
            }

            return responseText;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
           progressDialog.dismiss();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            ArrayList<Product> products = gson.fromJson(result, type);
            for (Product product : products) {
                texts.add(product.getTitle());
                prices.add(product.getPrice() + "RMB");
                images.add(HttpUtils.BASE_URL + product.getImage());
            }

            gridView = (GridView)findViewById(R.id.gridView);
            gridView.setAdapter(new MyAdapter(GameActivity.this, images, texts, prices));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView)view.findViewById(R.id.game_name);
                    Bundle bundle = new Bundle();
                    bundle.putString("gameName", textView.getText().toString());
                    Intent intent = new Intent(GameActivity.this, SpecificGameActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(GameActivity.this,
                    "加载中", "");
        }
    }
}


