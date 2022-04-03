package com.CodeMonkey.saveme.Boundary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CodeMonkey.saveme.Controller.AchievementController;
import com.CodeMonkey.saveme.R;

import java.util.ArrayList;
import java.util.List;

public class AchievementsPage extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_page);

        ArrayList<String> achievementNameList = AchievementController.getInstance().GetAchievement();

        AchievementAdapter achievementAdapter = new AchievementAdapter(achievementNameList);
        RecyclerView achievementVie = findViewById(R.id.recyclerView);
        achievementVie.setAdapter(achievementAdapter);
        achievementVie.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    private class AchievementVH extends RecyclerView.ViewHolder {
        ImageView tv1;
        TextView tv2;

        public AchievementVH(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
        }
    }

    private class AchievementAdapter extends RecyclerView.Adapter<AchievementVH> {

        private List<String> dataList;

        public AchievementAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public AchievementVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AchievementVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.achievements_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AchievementVH holder, int position) {
            String s = dataList.get(position);
            holder.tv2.setText(s);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}

