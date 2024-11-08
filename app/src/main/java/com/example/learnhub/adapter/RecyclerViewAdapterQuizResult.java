package com.example.learnhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnhub.R;
import com.example.learnhub.model.QuizResultModel;

import java.util.List;

public class RecyclerViewAdapterQuizResult extends RecyclerView.Adapter<RecyclerViewAdapterQuizResult.ViewHolder> {
    private Context context;
    private List<QuizResultModel> quizResultModelList;

    public RecyclerViewAdapterQuizResult(Context context, List<QuizResultModel> quizResultModelList) {
        this.context = context;
        this.quizResultModelList = quizResultModelList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterQuizResult.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_quiz_result,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterQuizResult.ViewHolder holder, int position) {
      QuizResultModel quizResultModel = quizResultModelList.get(position);
      holder.qname.setText(quizResultModel.getUsername());
      holder.score.setText(quizResultModel.getScore());
    }

    @Override
    public int getItemCount() {
        return quizResultModelList.size();
    }
    public void updateData(List<QuizResultModel> newQuizResultModelList) {
        this.quizResultModelList = newQuizResultModelList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView qname ,score;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            qname = itemView.findViewById(R.id.qname);
            score = itemView.findViewById(R.id.userscore);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
