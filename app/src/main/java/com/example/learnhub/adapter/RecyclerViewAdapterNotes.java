package com.example.learnhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnhub.R;
import com.example.learnhub.classroomUi.classwork.ShowQuiz;
import com.example.learnhub.classroomUi.classwork.ShowQuizResult;
import com.example.learnhub.classroomUi.classwork.notes;
import com.example.learnhub.classroomUi.classwork.showDocument;
import com.example.learnhub.model.Document;
import com.example.learnhub.model.UserSession;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterNotes extends RecyclerView.Adapter<RecyclerViewAdapterNotes.ViewHolder> {
    Context context;
    List<Object> itemlist;
    List<String> quizTitleList;
    String classcode;
    public RecyclerViewAdapterNotes(Context context, List<Object> itemlist,String classcode) {
        this.context = context;
        this.itemlist = itemlist;
        this.classcode = classcode;
    }

    /*public RecyclerViewAdapterNotes( List<String> quizTitleList,Context context) {
        this.context = context;
        this.quizTitleList = quizTitleList;
    }*/

    @NonNull
    @Override
    public RecyclerViewAdapterNotes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_show_notes,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterNotes.ViewHolder holder, int position) {
        Object item  = itemlist.get(position);
        if (item instanceof Document){
            Document document = (Document)item;
            holder.topic.setText(document.getTopic());
        } else if (item instanceof String) {
            holder.topic.setText((String)item);
        }
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
    public void  updateData(List<Object> newItemList) {
        itemlist.clear();
        itemlist.addAll(newItemList);
        notifyDataSetChanged();
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
            if (itemlist.get(getAdapterPosition()) instanceof Document) {
                Document document = (Document) itemlist.get(getAdapterPosition());
                ArrayList<String> documentArrayList = new ArrayList<>();
                documentArrayList = (ArrayList<String>) document.getDocumentList();
                Intent intent = new Intent(context, notes.class);
                intent.putExtra("topic", document.getTopic());
                intent.putExtra("description", document.getDescription());
                intent.putStringArrayListExtra("documentList", documentArrayList);
                Log.d("notesActivity", document.getTopic());
                Log.d("notesActivity", document.getDescription());
                Log.d("notesActivity", String.valueOf(document.getDocumentList().size()));
                context.startActivity(intent);
            }
            if (itemlist.get(getAdapterPosition()) instanceof String){
                Object item  = itemlist.get(getAdapterPosition());
                UserSession userSession = new UserSession(context);
                String usertype = userSession.getUserType();
                if(usertype.equals("Students")) {
                    context.startActivity(new Intent(context, ShowQuiz.class).putExtra("title", (String) item).putExtra("classcode", classcode));
                }
                else {
                    Toast.makeText(context, "Quiz result", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, ShowQuizResult.class).putExtra("title", (String) item).putExtra("classcode", classcode));

                }
            }
        }
    }


}
