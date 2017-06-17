package com.example.gtk.platform.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gtk.platform.R;
import com.example.gtk.platform.model.Product;
import com.example.gtk.platform.utils.HttpUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class SpecificGameActivity extends Activity {
    private SimpleDraweeView gameImageView;
    private SimpleDraweeView detailGameImageView;
    private TextView gameTitleView;
    private TextView gameDescView;
    private TextView gameInfoView;
    private TextView gamePriceView;
    private Bitmap gameImageBitmap;
    private Bitmap detailGameImageBitmap;
    private Button purchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_game);

        gameImageView = (SimpleDraweeView)findViewById(R.id.detail_game_image);
        detailGameImageView = (SimpleDraweeView) findViewById(R.id.detail_game_detail_image);
        gameTitleView = (TextView)findViewById(R.id.detail_game_title);
        gameDescView = (TextView)findViewById(R.id.detail_game_desc);
        gameInfoView = (TextView)findViewById(R.id.detail_game_info);
        gamePriceView = (TextView)findViewById(R.id.detail_game_price);
        purchaseButton = (Button)findViewById(R.id.detail_purchase_button);

        final String name = this.getIntent().getExtras().getString("gameName");

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("gameName", name);
                Intent intent = new Intent(SpecificGameActivity.this, PurchaseActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        new NetWorkTask().execute(name);
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

    class NetWorkTask extends AsyncTask<String, Void, Product> {
        @Override
        protected Product doInBackground(String... params) {
            Map<String, String> productMap = new HashMap<>();
            productMap.put("title", params[0]);
            String desc = null;

            String httpConn = HttpUtils.PRODUCT_URL + "/getProductByTitle";
            InputStream inputStream = HttpUtils.post(httpConn, productMap);

            Product product = null;
            if (inputStream != null) {
                desc = HttpUtils.inputStream2String(inputStream);
                Log.e("GTK", desc);

                Gson gson = new Gson();
                product = gson.fromJson(desc, Product.class);
            }
            return product;
        }

        @Override
        protected void onPostExecute(Product product) {
            gameImageView.setImageURI(HttpUtils.BASE_URL + product.getImage());
            detailGameImageView.setImageURI(HttpUtils.BASE_URL + product.getDetailImage());
            Log.e("GTK", HttpUtils.BASE_URL + product.getDetailImage());
            gameTitleView.setText(product.getTitle());
            gamePriceView.setText(product.getPrice() + "RMB");
            gameDescView.setText(product.getBriefIntro());
            gameInfoView.setText(product.getCompanyInfo());
        }
    }
}
