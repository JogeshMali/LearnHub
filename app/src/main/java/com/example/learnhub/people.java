package com.example.learnhub;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learnhub.adapter.RecyclerViewAdapterpeople;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class people extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    Button  invitebtn;
    TextView username ;
    DatabaseReference db;
    String classcode;
    CircleImageView prof;
    RecyclerView stdrecyclerview;
    LinearLayout invitelinearlayout;
    RecyclerViewAdapterpeople peopleadapter;
    List<Join> joinList ;


    public people() {
        // Required empty public constructor
    }


    public static people newInstance(String param1, String param2) {
        people fragment = new people();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        username = view.findViewById(R.id.username);
        prof = view .findViewById(R.id.profilephoto);

        invitebtn =  view.findViewById(R.id.invitebtn);

        Intent intent =  getActivity().getIntent();
        classcode = intent.getStringExtra("classcode");
        facultydetail();
        invitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendcode(classcode);
            }
        });
        stdrecyclerview = view.findViewById(R.id.stdrecyclerview);
        invitelinearlayout = view.findViewById(R.id.invitelayout);
        joinList = new ArrayList<>();
        stdrecyclerview.setHasFixedSize(true);
        stdrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        addStdinList();
        peopleadapter = new RecyclerViewAdapterpeople(getContext(),joinList);
        stdrecyclerview.setAdapter(peopleadapter);
    return view;
    }

    private void CheckStudent() {

        if (!joinList.isEmpty()){
            invitelinearlayout.setVisibility(View.GONE);
            stdrecyclerview.setVisibility(View.VISIBLE);

        }else {
            invitelinearlayout.setVisibility(View.VISIBLE);
            stdrecyclerview.setVisibility(View.GONE);
        }
    }

    private void addStdinList() {
        DatabaseReference stdjoinref = FirebaseDatabase.getInstance().getReference("Join");
        Query query = stdjoinref.orderByChild("classCode").equalTo(classcode);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                joinList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Join join = dataSnapshot.getValue(Join.class);
                    joinList.add(join);
                }
                CheckStudent();
                peopleadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void  sendcode(String classcode){
        String shareclasscode = classcode;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("Text/plain");
        String shareMessage = "Join my class on Learnhub with the code "+shareclasscode;
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareMessage);
        startActivity(Intent.createChooser(shareIntent,"Share class code via "));
    }
    private void facultydetail() {
        db = FirebaseDatabase.getInstance().getReference("Class");
        Query query = db.orderByChild("classCode").equalTo(classcode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot usersnap : snapshot.getChildren()) {
                        String user = usersnap.child("username").getValue(String.class);
                        username.setText(user);
                        String email = usersnap.child("email").getValue(String.class);
                        setProfile(email);
                    }
                } else {
                    Toast.makeText(getActivity(), "No username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
        private void setProfile (String email){
            DatabaseReference facultyref = FirebaseDatabase.getInstance().getReference("Faculty");
                facultyref.child(email.replace(".", ",")).child("imageUrl").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String imageUrl = task.getResult().getValue(String.class);
                        if (imageUrl != null) {
                            loadImageFromUrl(imageUrl);
                        }
                    }
                });

        }
        private void loadImageFromUrl (String imageUrl){

            Glide.with(this)
                    .load(imageUrl).fitCenter()
                    .placeholder(R.drawable.profileimg) // Add a placeholder image if needed
                    .into(prof);
        }
    }
