package com.example.learnhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnhub.Class;
import com.example.learnhub.Join;
import com.example.learnhub.R;
import com.example.learnhub.classroom;
import com.example.learnhub.classroomUi.chatroom.chatroom;
import com.example.learnhub.faculty.Facultyhome;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Class> classList;

    private String usertype;

    public RecyclerViewAdapter(Context context, List<Class> classList) {
        this.context = context;
        this.classList = classList;
    }



    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_showclasses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Class aClass = classList.get(position);
            holder.classname.setText(aClass.getClassname() != null ? aClass.getClassname() : "No Class Name");
            holder.section.setText(aClass.getSection() != null ? aClass.getSection() : "No Section");
            holder.subject.setText(aClass.getSubject() != null ? aClass.getSubject() : "No Subject");



    }

    @Override
    public int getItemCount() {
        return  classList.size() ;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView classname,section,subject;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);


            classname = itemView.findViewById(R.id.classname);


                section = itemView.findViewById(R.id.section);
                subject = itemView.findViewById(R.id.subject);

        }
        @Override
        public void onClick(View v) {
         int position = this.getAdapterPosition();
         Class selectedClass = classList.get(position);
         String classname = selectedClass.getClassname();
         String section = selectedClass.getSection();
         String subject = selectedClass.getSubject();
            Intent intent = new Intent(context, classroom.class);
            intent.putExtra("classname",classname);
            intent.putExtra("section",section);
            intent.putExtra("subject",subject);
            intent.putExtra("classcode",selectedClass.getClassCode());
            context.startActivity(intent);




        }
    }
}
