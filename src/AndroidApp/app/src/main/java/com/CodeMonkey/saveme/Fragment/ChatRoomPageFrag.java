package com.CodeMonkey.saveme.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Controller.EventController;
import com.CodeMonkey.saveme.Controller.TCPManager;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;

/***
 * BusPageFrag created by Wang Tianyu 04/04/2022
 * Chat room page
 */

public class ChatRoomPageFrag extends Fragment {

    private TextView title;
    private TextView button;
    private TextView textBox;
    private TextView textInputBox;
    private String phoneNumber;
    private ImageView img;
    private LinearLayout mainPage;

    public ChatRoomPageFrag(){
    };

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.chat_room_page_fragment, container, false);
        title = view.findViewById(R.id.hintText);
        mainPage = view.findViewById(R.id.mainPageContent);
        img = view.findViewById(R.id.chatGroupImg);
        button = view.findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TCPManager.getTCPManager().send("MSG;" + phoneNumber + ";" + UserController.getUserController().getUser().getName() + ";" + textInputBox.getText().toString());
                textInputBox.setText("");
            }
        });
        textBox = view.findViewById(R.id.textBox);

        textInputBox = view.findViewById(R.id.inputBox);
        textInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (textInputBox.getText().toString().length()!=0)
                    button.setVisibility(View.VISIBLE);
                else
                    button.setVisibility(View.GONE);
            }
        });

        return view;
    }

    public void newMessage(String name, String msg){
        textBox.setText(textBox.getText() + name + ":" + msg + "\n");
    }

    public void notAccept(){
        mainPage.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        title.setText(R.string.chat_group_hint_text);
    }

    public void hasAccept(){
        textBox.setText("");
        textInputBox.setText("");
        title.setText("Chat group of\n" + EventController.getEventController().getAcceptEvent().getUser().getName());
        setPhoneNumber(EventController.getEventController().getAcceptEvent().getUser().getPhoneNumber());
        mainPage.setVisibility(View.VISIBLE);
        img.setVisibility(View.GONE);
    }

}