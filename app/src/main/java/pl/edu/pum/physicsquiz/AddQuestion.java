package pl.edu.pum.physicsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class AddQuestion extends AppCompatActivity
{
    public static final String EXTRA_REPLY =
            "pl.edu.pum.physicsquiz.extra.REPLY";

    private RadioButton mTrueRadioButton;
    private RadioButton mFalseRadioButton;
    private EditText mQuestionEditText;
    private Button mAddQuestionButton;
    private String mQuestion;
    private boolean mAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        mTrueRadioButton = findViewById(R.id.trueRadioButton);
        mFalseRadioButton = findViewById(R.id.falseRadioButton);
        mQuestionEditText = findViewById(R.id.questionEditText);
        mAddQuestionButton = findViewById(R.id.addButton);

        mTrueRadioButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAnswer = true;
            }
        });

        mFalseRadioButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAnswer = false;
            }
        });

        mAddQuestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mQuestion = mQuestionEditText.getText().toString();
                Intent replyintent = new Intent();
                replyintent.putExtra("Question", mQuestion);
                replyintent.putExtra("Answer", mAnswer);
                setResult(RESULT_OK, replyintent);
                finish();
            }
        });
    }
}