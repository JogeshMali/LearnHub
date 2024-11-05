package com.example.learnhub.classroomUi.classwork;

import android.content.Intent;
import android.health.connect.datatypes.units.Length;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.learnhub.R;
import com.example.learnhub.adapter.RecyclerViewAdapterNotes;
import com.example.learnhub.model.Document;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class classwork extends Fragment {

   FloatingActionButton assignbtn,quizbtn,notesbtn;
   FloatingActionMenu fabmenu;
   RecyclerView notesrecyclerview;
   RecyclerViewAdapterNotes notesAdapter;
    List<String> quizTitleList ;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2,classCode;

    public classwork() {
        // Required empty public constructor
    }


    public static classwork newInstance(String param1, String param2) {
        classwork fragment = new classwork();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_classwork, container, false);

        fabmenu = view.findViewById(R.id.fab_menu);
        assignbtn = view.findViewById(R.id.fab_assignment);
        quizbtn = view.findViewById(R.id.fab_quiz);
        notesbtn = view.findViewById(R.id.fab_notes);
        classCode = getArguments().getString("classcode");
        quizTitleList = new ArrayList<>();
        notesrecyclerview =view.findViewById(R.id.notesrecyclerview);
        notesrecyclerview.setHasFixedSize(true);
        notesrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*addDocument();
        notesAdapter = new RecyclerViewAdapterNotes(getContext(),documentList);*/
        addDocument();
        notesAdapter = new RecyclerViewAdapterNotes(getContext(),new ArrayList<>());
        notesrecyclerview.setAdapter(notesAdapter);
        notesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UploadDocument.class)
                        .putExtra("classcode",classCode));
            }
        });
        assignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        quizbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateQuiz.class)
                        .putExtra("classcode",classCode));
            }
        });
        return view;
    }

    private void addDocument() {
        DatabaseReference notesref = FirebaseDatabase.getInstance().getReference("Document");
        Query query = notesref.child(classCode);
        query .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Object> documentList = new ArrayList<>();
                for(DataSnapshot topicSnapshot : snapshot.getChildren()){

                        String topicname = topicSnapshot.child("topic").getValue(String.class).toString();
                        String description = topicSnapshot.child("description").getValue(String.class).toString();
                        List<String> documentURllist = new ArrayList<>();
                        for (DataSnapshot docSnapshot : topicSnapshot.child("documentList").getChildren()){
                            String documentUrl = docSnapshot.getValue(String.class);

                            documentURllist.add(documentUrl);
                        }
                        documentList.add(new Document(topicname,description,documentURllist));
            }
            notesAdapter.updateData(documentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.classwork_toolbar_menu, menu); // Inflate classwork menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.menu_notes == (item.getItemId())) {
            Toast.makeText(getContext(), "Notes selected", Toast.LENGTH_SHORT).show();
            addDocument();
        } else if (R.id.menu_assignment == (item.getItemId())) {
            Toast.makeText(getContext(), "Assignment selected", Toast.LENGTH_SHORT).show();

        }else if (R.id.menu_quiz == (item.getItemId())) {
            Toast.makeText(getContext(), "Quiz selected", Toast.LENGTH_SHORT).show();
            addQuiz();
        }
        return super.onOptionsItemSelected(item);

    }
    private void addQuiz(){
        DatabaseReference quizref  =FirebaseDatabase.getInstance().getReference("Quiz");
        quizTitleList.clear();
        quizref.child(classCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Object> quiztitle = new ArrayList<>(); // Get existing items
                for (DataSnapshot quizsnapshot :snapshot.getChildren()){
                        String title = quizsnapshot.child("title").getValue(String.class);
                        quiztitle.add(title);
}
             notesAdapter.updateData(quiztitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error fetching quiz data", Toast.LENGTH_SHORT).show();
            }

        });
    }


}




