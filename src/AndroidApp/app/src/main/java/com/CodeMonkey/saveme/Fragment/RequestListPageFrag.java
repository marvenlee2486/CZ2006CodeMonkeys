package com.CodeMonkey.saveme.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CodeMonkey.saveme.Boundary.AchievementsPage;
import com.CodeMonkey.saveme.Controller.AchievementController;
import com.CodeMonkey.saveme.Controller.EventController;
import com.CodeMonkey.saveme.Entity.Event;
import com.CodeMonkey.saveme.R;
import com.google.android.material.transition.Hold;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * RequestListPageFrag created by Wang Tianyu 05/04/2022
 * List all the request page
 */

public class RequestListPageFrag extends Fragment {

    private Handler handler;
    private RequestListAdapter adapter;

    public RequestListPageFrag(){
        RequestListAdapter requestListAdapter = new RequestListAdapter(EventController.getEventController().getEventList());
        this.adapter = requestListAdapter;
    };

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.request_list_page_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        return view;
    }

    public class RequestListItemHolder extends RecyclerView.ViewHolder {
        private TextView phoneNumber;
        private TextView name;
        private ImageView map;
        private Button button;
        private boolean accept;

        public RequestListItemHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            name = itemView.findViewById(R.id.name);
            map = itemView.findViewById(R.id.mapIcon);
            button = itemView.findViewById(R.id.button);
            accept = false;
        }
    }

    public class RequestListAdapter extends RecyclerView.Adapter<RequestListPageFrag.RequestListItemHolder> {

        private ArrayList<Event> eventList = new ArrayList<>();

        public RequestListAdapter(Map<String, Event> eventList) {
            for (Event event: eventList.values())
                this.eventList.add(event);
        }

        @NonNull
        @Override
        public RequestListPageFrag.RequestListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RequestListPageFrag.RequestListItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RequestListPageFrag.RequestListItemHolder holder, int position) {
            Event event = eventList.get(position);
            holder.name.setText(event.getUser().getName());
            holder.phoneNumber.setText(event.getUser().getPhoneNumber());
            holder.map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message message = new Message();
                    message.what = 4;
                    message.obj = event.getUser().getPhoneNumber();
                    handler.sendMessage(message);
                }
            });
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.accept){
                        declineEvent(event, holder);
                    }
                    else{
                        if (EventController.getEventController().getAcceptEvent() == null){
                            acceptEvent(event, holder);
                        }
                        else{
                            declineEvent(EventController.getEventController().getAcceptEvent(), EventController.getEventController().getAcceptHolder());
                            acceptEvent(event, holder);
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        private void acceptEvent(Event event, RequestListPageFrag.RequestListItemHolder holder){
            EventController.getEventController().acceptEvent(event.getUser().getPhoneNumber(), holder);
            holder.button.setText("Decline");
            holder.accept = true;
        }

        private void declineEvent(Event event, RequestListPageFrag.RequestListItemHolder holder){
            EventController.getEventController().declineEvent(event.getUser().getPhoneNumber());
            holder.button.setText("Accept");
            holder.accept = false;
        }

        public void addNewEvent(String phoneNumber){
            eventList.add(EventController.getEventController().getEventList().get(phoneNumber));
            notifyItemChanged(eventList.size());
        }

        public void removeEvent(String phoneNumber){
            for (int i = 0; i < eventList.size(); i++){
                if (eventList.get(i).getUser().getPhoneNumber().equals(phoneNumber)){
                    eventList.remove(i);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public RequestListAdapter getAdapter() {
        return adapter;
    }
}