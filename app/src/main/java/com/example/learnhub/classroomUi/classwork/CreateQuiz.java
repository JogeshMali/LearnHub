package com.example.learnhub.classroomUi.classwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnhub.R;
import com.example.learnhub.adapter.RecyclerViewAdapterDocs;
import com.example.learnhub.model.QuizModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateQuiz extends AppCompatActivity {

    EditText quizTitle , quizQuestion ,correctAnswer,score;
    EditText opt1,opt2,opt3,opt4;
    Button addQuestionbtn , submitbtn;
    List<String> options ;
    List<QuizModel> quizModelList;
    String title , question  ,correctans,classcode,totalScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        classcode = intent.getStringExtra("classcode");
        quizTitle  =findViewById(R.id.quiz_title);
        quizQuestion =findViewById(R.id.question_text);
        correctAnswer =findViewById(R.id.correct_answer);
        score  =findViewById(R.id.total_score_input);
        opt1 =findViewById(R.id.option1);
        opt2 =findViewById(R.id.option2);
        opt3 =findViewById(R.id.option3);
        opt4 =findViewById(R.id.option4);
        addQuestionbtn = findViewById(R.id.nextQuestion);
        submitbtn = findViewById(R.id.submit_button);
        options  = new ArrayList<>();
        quizModelList = new ArrayList<>();

        submitbtn.setOnClickListener(v -> {
            initialize();
            if (!TextUtils.isEmpty(question) && !TextUtils.isEmpty(opt1.getText()) && !TextUtils.isEmpty(opt2.getText()) &&
                    !TextUtils.isEmpty(opt3.getText()) && !TextUtils.isEmpty(opt4.getText()) && !TextUtils.isEmpty(correctans)) {
                 quizModelList.add( new QuizModel(question,options,correctans));
            }
            if (quizModelList.isEmpty()){
                Toast.makeText(this, "No questions to save", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Quiz");

            String quizId = quizRef.push().getKey();
            quizRef.child(classcode).child(quizId).child("title").setValue(title);
            quizRef.child(classcode).child(quizId).child("score").setValue(totalScore);
            for (QuizModel question : quizModelList){
                 String questionId = quizRef.child(classcode).child(quizId).child(title).push().getKey();
                 quizRef.child(classcode).child(quizId).child(title).child(questionId).setValue(question);
        }
                    Toast.makeText(CreateQuiz.this, "Quiz created successfully", Toast.LENGTH_SHORT).show();
                    quizModelList.clear();
                    options.clear();

        });
        addQuestionbtn.setOnClickListener(v -> {
          initialize();
            if (TextUtils.isEmpty(question) || TextUtils.isEmpty(opt1.getText()) || TextUtils.isEmpty(opt2.getText()) ||
                    TextUtils.isEmpty(opt3.getText()) || TextUtils.isEmpty(opt4.getText()) || TextUtils.isEmpty(correctans)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
          QuizModel questions = new QuizModel(question,options,correctans);
          quizModelList.add(questions);
          clearFields();
        });
    }
    private void initialize(){
        title = quizTitle.getText().toString().trim();
        question = quizQuestion.getText().toString().trim();
        options.clear();
        options.add(opt1.getText().toString());
        options.add(opt2.getText().toString());
        options.add(opt3.getText().toString());
        options.add(opt4.getText().toString());
        correctans = correctAnswer.getText().toString().trim();
        totalScore = score.getText().toString().trim();

    }
    private void clearFields() {
        quizQuestion.setText("");
        opt1.setText("");
        opt2.setText("");
        opt3.setText("");
        opt4.setText("");
        correctAnswer.setText("");
    }
}