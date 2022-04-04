package com.CodeMonkey.saveme.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.Certificate;
import com.CodeMonkey.saveme.Entity.CertificateRsp;
import com.CodeMonkey.saveme.Entity.CertificateURLScheme;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.RequestUtil;
import com.CodeMonkey.saveme.Util.URLUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import rx.Completable;
import rx.Observer;

/***
 * RegVolPageFrag created by Wang Tianyu 14/02/2022
 * Page for user to register to be a rescuer
 */
public class RegVolPageFrag extends Fragment {

    private ImageView uploadPhoto;
    private Context context;
    private ImageView warningIcon;
    private TextView textView;

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
        textView = view.findViewById(R.id.explainText);
        switch (UserController.getUserController().getUser().getIsVolunteer()){
            case "PENDING":
                textView.setText("Your CPR certificate is under checking by our stuff...");
                break;
            case "REJECTED":
                textView.setText("Your CPR has expired the date! Please re-upload one");
                break;
        }
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
                        //Read photo that user selected
                        Uri img = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = context.getContentResolver().query(img, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(index);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        FileInputStream file = new FileInputStream(path);
                        File photo = new File(path);
                        String extension = path.split("\\.")[path.split("\\.").length-1];
                        //Initialize upload requests
                        CertificateURLScheme certificateURLScheme = new CertificateURLScheme();
                        certificateURLScheme.setFileExtension(extension);
                        certificateURLScheme.setPhoneNumber(UserController.getUserController().getUser().getPhoneNumber());
                        final MultipartBody.Part[] key = {null};
                        final MultipartBody.Part[] token = new MultipartBody.Part[1];
                        final MultipartBody.Part[] accessKey = new MultipartBody.Part[1];
                        final MultipartBody.Part[] policy = new MultipartBody.Part[1];
                        final MultipartBody.Part[] signature = new MultipartBody.Part[1];
                        final RequestBody[] filePart = new RequestBody[1];
                        RequestUtil.postCertData(new Observer<CertificateRsp>() {
                            @Override
                            public void onCompleted() {

                                RequestUtil.postRealCertData(new Observer<Completable>() {
                                    @Override
                                    public void onCompleted() {

                                        UserController.getUserController().getUser().setCertificateUrl(URLUtil.awsS3Base + certificateURLScheme.getPhoneNumber() + "." + certificateURLScheme.getFileExtension());
                                        UserController.getUserController().getUser().setIsVolunteer("PENDING");
                                        RequestUtil.postUserData(new Observer<User>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(User user) {
                                                Log.i("User", user.toString());
                                                textView.setText("Your CPR certificate is under checking by our stuff...");
                                            }
                                        }, UserController.getUserController().getUser(), UserController.getUserController().getToken());

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }

                                    @Override
                                    public void onNext(Completable response) {
                                    }
                                }, key[0], token[0], accessKey[0], policy[0], signature[0], filePart[0]);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(CertificateRsp certificateRsp) {

                                key[0] = MultipartBody.Part.createFormData("key", certificateRsp.getFields().getKey());
                                token[0] = MultipartBody.Part.createFormData("x-amz-security-token", certificateRsp.getFields().getAmzToken());
                                accessKey[0] = MultipartBody.Part.createFormData("AWSAccessKeyId", certificateRsp.getFields().getAWSAccessKeyId());
                                policy[0] = MultipartBody.Part.createFormData("policy", certificateRsp.getFields().getPolicy());
                                signature[0] = MultipartBody.Part.createFormData("signature", certificateRsp.getFields().getSignature());
                                filePart[0] = RequestBody.create(MediaType.parse("image"),photo);
                            }
                        }, certificateURLScheme, UserController.getUserController().getToken());
                        warningIcon.setImageResource(R.drawable.image_icon);
                        warningIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDialog(bitmap);
                            }
                        });
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

    public void setCertificate(){
        RequestUtil.getCertData(new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Error", e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                warningIcon.setImageResource(R.drawable.image_icon);
                warningIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(bitmap);
                    }
                });
            }
        }, UserController.getUserController().getUser().getCertificateUrl());
    }


}
