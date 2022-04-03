package com.CodeMonkey.saveme.Boundary;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.CodeMonkey.saveme.Controller.CertificateController;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class CertificatePage extends BaseActivity {
    private TextView mainName;
    private ImageView certificateImage;

    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_profile2);
        init();
        DisplayPage();
    }

    private void init(){
        mainName = findViewById(R.id.mainName);
        certificateImage = findViewById(R.id.certificatePhoto);

        certificateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void DisplayPage(){
        mainName.setText(UserController.getUserController().getUser().getName());

        Bitmap bmp = getImageBitmap(CertificateController.getInstance().getCertificateUrl());
        certificateImage.setImageBitmap(bmp);
    }

    public Bitmap getImageBitmap(String url) {
        Log.e("Certificate", url);
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            Log.e("Certificate", e.toString());
            e.printStackTrace();
        }
        return bitmap;
    }


}