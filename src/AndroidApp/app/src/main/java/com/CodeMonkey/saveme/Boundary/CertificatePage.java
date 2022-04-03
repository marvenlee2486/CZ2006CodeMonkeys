package com.CodeMonkey.saveme.Boundary;



import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
    private Bitmap bitmap;
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
                showDialog(bitmap);
            }
        });

    }

    private void DisplayPage(){
        mainName.setText(UserController.getUserController().getUser().getName());

        new DownloadImageTask(certificateImage)
                .execute(CertificateController.getInstance().getCertificateUrl());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void showDialog(Bitmap bitmap){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.certificate_show_layout);
        ImageView imageView = dialog.findViewById(R.id.certificatePhoto);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

}