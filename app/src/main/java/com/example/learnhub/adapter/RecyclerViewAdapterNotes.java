package com.example.learnhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnhub.R;
import com.example.learnhub.classroomUi.classwork.notes;
import com.example.learnhub.classroomUi.classwork.showDocument;
import com.example.learnhub.model.Document;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterNotes extends RecyclerView.Adapter<RecyclerViewAdapterNotes.ViewHolder> {
    Context context;
    List<Document> documentList;
    public RecyclerViewAdapterNotes(Context context, List<Document> documentList) {
        this.context = context;
        this.documentList = documentList;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterNotes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_show_notes,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterNotes.ViewHolder holder, int position) {
     Document document = documentList.get(position);
     holder.topic.setText(document.getTopic());
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView topic ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            topic = itemView.findViewById(R.id.ntopic);
        }

        @Override
        public void onClick(View v) {
         Document document =  documentList.get(getAdapterPosition());
         ArrayList<String> documentArrayList = new ArrayList<>();
         documentArrayList = (ArrayList<String>)document.getDocumentList();
         Intent intent = new Intent(context, notes.class);
         intent.putExtra("topic",document.getTopic());
         intent.putExtra("description",document.getDescription());
         intent.putStringArrayListExtra("documentList",documentArrayList);
            Log.d("notesActivity",document.getTopic());
            Log.d("notesActivity",document.getDescription());
            Log.d("notesActivity", String.valueOf(document.getDocumentList().size()));
         context.startActivity(intent);
        }
    }
}
