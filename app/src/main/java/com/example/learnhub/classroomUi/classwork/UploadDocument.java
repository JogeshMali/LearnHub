package com.example.learnhub.classroomUi.classwork;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnhub.R;
import com.example.learnhub.adapter.RecyclerViewAdapterDocs;
import com.example.learnhub.model.Document;
import com.example.learnhub.model.DocumentModel;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadDocument extends AppCompatActivity {
    Toolbar toolbar ;
    EditText topic, description;
    String ntopic,ndecription,classcode;
    ImageButton attachfile;
    List<DocumentModel> documentModelList;
    List<Uri> documentList;
    RecyclerView docsrecyclerview;
    RecyclerViewAdapterDocs adapterDocs;
    Button uploadbtn;
    ProgressBar progressBar;
    private static final int PICK_DOCUMENT_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_document);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadDocument.this, classwork.class));
                finish();
            }
        });
        topic = findViewById(R.id.topic);
        description= findViewById(R.id.notesDescription);
        uploadbtn = findViewById(R.id.uploadbtn);
        progressBar = findViewById(R.id.progressbar);
        Intent intent= getIntent();
        classcode = intent.getStringExtra("classcode");


        documentModelList = new ArrayList<>();
        documentList = new ArrayList<>();

        docsrecyclerview =findViewById(R.id.pdfrecyclerview);
        docsrecyclerview.setHasFixedSize(true);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(UploadDocument.this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
       
        docsrecyclerview.setLayoutManager(flexboxLayoutManager);
        adapterDocs = new RecyclerViewAdapterDocs(UploadDocument.this,documentModelList);
        docsrecyclerview.setAdapter(adapterDocs);

        attachfile =findViewById(R.id.attachfile);
        attachfile.setOnClickListener(v -> {openDocumentPicker();});

        uploadbtn.setOnClickListener(v -> {StoredDocumentInFirebase(documentList);});
    }
   
    private void openDocumentPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        documentPickerLauncher.launch(Intent.createChooser(intent,"Select Document"));
    }
    
    private final ActivityResultLauncher<Intent> documentPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri documentUri = result.getData().getData();
                        if (documentUri != null) {
                            String fileName = getFileName(documentUri);
                            documentModelList.add(new DocumentModel(fileName, documentUri));
                            documentList.add(documentUri);
                            adapterDocs.notifyDataSetChanged();
                        }
                    }
                } );

    private String getFileName(Uri documentUri) {
        String result = null;
        if (documentUri.getScheme().equals("content")){
            try (Cursor cursor = UploadDocument.this.getContentResolver().query(documentUri, null, null, null, null)) {
                if(cursor!= null && cursor.moveToFirst() ){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result==null){
           result = documentUri.getPath();
           int cut = result.lastIndexOf('/');
           if (cut!=-1){
               result =result.substring(cut +1 );
           }

        }
        return result;
    }
   private void StoredDocumentInFirebase(List<Uri> documentList) {
       ntopic  = topic.getText().toString().trim();
       ndecription = description.getText().toString().trim();
       changeProgressBar(true);
       DatabaseReference notesref = FirebaseDatabase.getInstance().getReference("Document");
       StorageReference storageref = FirebaseStorage.getInstance().getReference();
       List<String> documentUrlList = new ArrayList<>();
       for (Uri documentUri : documentList) {
           String filename = getFileName(documentUri);
           StorageReference fileref = storageref.child(classcode + "/"+ntopic +"/" + filename);
           fileref.putFile(documentUri).addOnCompleteListener(task -> {
               fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       documentUrlList.add(uri.toString());
                       if (documentUrlList.size() == documentList.size()) {
                           Document document = new Document(ntopic, ndecription, documentUrlList);
                           notesref.child(classcode).child(ntopic).setValue(document).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       Toast.makeText(UploadDocument.this, "Document Added Successfully", Toast.LENGTH_SHORT).show();
                                       startActivity(new Intent(UploadDocument.this, classwork.class));
                                   }else
                                       Toast.makeText(UploadDocument.this, "Error while adding documents", Toast.LENGTH_SHORT).show();
                               }
                           });
                       }
                   }
               });
           });


       }
   }

    private void changeProgressBar(boolean progress) {
        if (progress){
            uploadbtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            uploadbtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}

