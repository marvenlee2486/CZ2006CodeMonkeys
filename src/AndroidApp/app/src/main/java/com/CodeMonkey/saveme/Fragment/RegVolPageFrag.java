package com.CodeMonkey.saveme.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.Certificate;
import com.CodeMonkey.saveme.Entity.UserRsp;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.RequestUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observer;

/***
 * RegVolPageFrag created by Wang Tianyu 14/02/2022
 * Page for user to register to be a rescuer
 */
public class RegVolPageFrag extends Fragment {

    private ImageView uploadPhoto;
    private Context context;
    private ImageView warningIcon;

    public RegVolPageFrag(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.reg_vol_page_fragment, container, false);

        uploadPhoto = view.findViewById(R.id.uploadPhoto);
        warningIcon = view.findViewById(R.id.warningIcon);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri img = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = context.getContentResolver().query(img, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(index);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        FileInputStream file = new FileInputStream(path);
                        byte[] base64Data = new byte[file.available()];
                        file.read(base64Data);
                        String base64 = Base64.encodeToString(base64Data, Base64.NO_CLOSE);
                        Log.e("test", base64);
                        warningIcon.setImageResource(R.drawable.image_icon);
                        warningIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDialog(bitmap);
                            }
                        });
                        initData(base64);
                        file.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void showDialog(Bitmap bitmap){
        Dialog dialog = new Dialog(context);
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

    private void initData(String data) {
        Log.e("test", UserController.getUserController().getToken());
        Certificate certificate = new Certificate();
        certificate.setFileData(getResources().getString(R.string.testBase64));
        certificate.setFileExtension(".png");
        certificate.setPhoneNumber("12345678");
        RequestUtil.postCertData(new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("ji", e.getMessage());
            }

            @Override
            public void onNext(ResponseBody certificate) {
                Log.e("test", "Success");
                try {
                    Log.e("!!!!!!!!!!!!!!!!!!!!", certificate.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, certificate, UserController.getUserController().getToken());
    }


}