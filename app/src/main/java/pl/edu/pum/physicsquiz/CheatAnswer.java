package pl.edu.pum.physicsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CheatAnswer extends AppCompatActivity
{
    private TextView mAnswerTextView;
    private TextView mQuestionTextView;
    private Button mShowAnswerButton;
    private String mQuestion;
    private boolean mGetAnswer;
    public static boolean isCheated = false;
    private static boolean isButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat_answer);

        mAnswerTextView = findViewById(R.id.answerTextView);
        mShowAnswerButton = findViewById(R.id.showAnswerButton);
        mQuestionTextView = findViewById(R.id.questionTextView);


        Intent intent = getIntent();
        mQuestion = intent.getStringExtra("Question");
        mGetAnswer = intent.getBooleanExtra("CorrectAnswer", true);

        if (mQuestion != null)
        {
            mQuestionTextView.setText(mQuestion);
        }

        mShowAnswerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                isButtonClicked = true;
                isCheated = true;
                String answer = String.valueOf(mGetAnswer);
                setAnswerColor(answer);
                mAnswerTextView.setText(answer);
            }
        });

        if(savedInstanceState != null)
        {
            if (isButtonClicked == false)
            {
                mAnswerTextView.setText("");
            }
            else
            {
                setAnswerColor(String.valueOf(savedInstanceState.getBoolean("Answer")));
                mAnswerTextView.setText(String.valueOf(savedInstanceState.getBoolean("Answer")));
            }
            mQuestionTextView.setText(savedInstanceState.getString("Question"));
            isCheated = savedInstanceState.getBoolean("isCheated");
        }
    }

    private void setAnswerColor(String answer)
    {
        if (answer == "true")
        {
            mAnswerTextView.setTextColor(Color.GREEN);
        }
        else
        {
            mAnswerTextView.setTextColor(Color.RED);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("Question", mQuestion);
        outState.putBoolean("Answer", mGetAnswer);
        outState.putBoolean("isCheated", isCheated);
    }
}