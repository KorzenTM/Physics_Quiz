package pl.edu.pum.physicsquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    public static final int TEXT_REQUEST = 1;

    private ImageButton mSearchButton;
    private ImageButton mAddQuestionButton;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPreviousButton;
    private Button mRestartButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mSummaryTextView;
    private TextView mCheatedSummaryTextView;
    private TextView mScoreTextView;
    private Button mShowAnswerButton;
    private static ArrayList<Question> mQuestionBank2 = new ArrayList<Question>()
    {
        {
            add(new Question("Was relative theory created by Isaac Newton?", false, false));
            add(new Question("Can a fire have a shadow?", true, false));
            add(new Question("Can air make shadows?", true, false));
        }
    };
    private static int mCurrentIndex = 0;
    private static int mCorrectAnswersCounter = 0;
    private static int mIncorrectAnswersCounter = 0;
    private static int mCheatedAnswersCounter = 0;
    private static boolean mIsAnswered = false;
    private int mHowManySubtract = 15;
    private double mScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddQuestionButton = findViewById(R.id.addQuestionButton);
        mSearchButton = findViewById(R.id.search);
        mTrueButton = findViewById(R.id.trueButton);
        mFalseButton = findViewById(R.id.falseButton);
        mNextButton = findViewById(R.id.nextButton);
        mPreviousButton = findViewById(R.id.previousButton);
        mQuestionTextView = findViewById(R.id.questionTextView);
        mCheatButton = findViewById(R.id.cheatButton);
        mRestartButton = findViewById(R.id.restartButton);
        mSummaryTextView = findViewById(R.id.summaryTextView);
        mCheatedSummaryTextView = findViewById(R.id.cheatedSummaryTextView);
        mScoreTextView = findViewById(R.id.ScoreTextView);
        mShowAnswerButton = findViewById(R.id.showCorrectAnswerButton);
        mRestartButton.setVisibility(View.INVISIBLE);
        updateQuestion();

        mPreviousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (endQuiz())
                {
                    mScore = ((double) mCorrectAnswersCounter / mQuestionBank2.size()) * 100.0;
                    summaryQuiz(mCorrectAnswersCounter, mIncorrectAnswersCounter, mScore);
                }
                else
                {
                    mIsAnswered = false;
                    mCurrentIndex--;

                    if (mCurrentIndex < 0)
                    {
                        mCurrentIndex = mQuestionBank2.size() - 1;
                    }
                    if(mQuestionBank2.get(mCurrentIndex).getIsAnswered())
                    {
                        setAnswerButtonVisibility(false);
                        mShowAnswerButton.setEnabled(true);
                    }
                    else
                    {
                        setAnswerButtonVisibility(true);
                        mShowAnswerButton.setEnabled(false);
                    }
                    updateQuestion();
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (endQuiz())
                {
                    mScore = ((double) mCorrectAnswersCounter / mQuestionBank2.size()) * 100.0;
                    summaryQuiz(mCorrectAnswersCounter, mIncorrectAnswersCounter, mScore);
                }
                else
                {
                    mIsAnswered = false;
                    mCurrentIndex++;

                    if (mCurrentIndex == mQuestionBank2.size())
                    {
                        mCurrentIndex = 0;
                    }
                    if(mQuestionBank2.get(mCurrentIndex).getIsAnswered())
                    {
                        setAnswerButtonVisibility(false);
                        mShowAnswerButton.setEnabled(true);
                    }
                    else
                    {
                        setAnswerButtonVisibility(true);
                        mShowAnswerButton.setEnabled(false);
                    }
                    updateQuestion();
                }
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mShowAnswerButton.setEnabled(true);
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mShowAnswerButton.setEnabled(true);
                checkAnswer(false);
            }
        });

        mShowAnswerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(MainActivity.this, String.valueOf(mQuestionBank2.get(mCurrentIndex).isAnswerTrue()), Toast.LENGTH_SHORT).show();
            }
        });

        mRestartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                for (int i = 0; i < mQuestionBank2.size(); i++) mQuestionBank2.get(i).setIsAnswered(false);
                mCorrectAnswersCounter = mIncorrectAnswersCounter = mCurrentIndex = mCheatedAnswersCounter = 0;
                setAnswerButtonVisibility(true);
                mShowAnswerButton.setEnabled(false);
                setUnusedObjectVisibility(View.VISIBLE);
                updateQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, CheatAnswer.class);
                intent.putExtra("Question", mQuestionBank2.get(mCurrentIndex).getQuestion());
                intent.putExtra("CorrectAnswer", mQuestionBank2.get(mCurrentIndex).isAnswerTrue());
                startActivity(intent);
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = mQuestionTextView.getText().toString();

                Uri webpage = Uri.parse("https://www.google.com/search?q=" + url);

                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

                if (intent.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(intent);
                }
            }
        });

        mAddQuestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, AddQuestion.class);
                startActivityForResult(intent, TEXT_REQUEST);
            }
        });


        if (savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt("Index");
            mCorrectAnswersCounter = savedInstanceState.getInt("CorrectAnswers");
            mIncorrectAnswersCounter = savedInstanceState.getInt("IncorrectAnswers");
            mCheatedAnswersCounter = savedInstanceState.getInt("CheatedAnswers");

            if (endQuiz())
            {
                setUnusedObjectVisibility(View.INVISIBLE);
                mScore = savedInstanceState.getDouble("Score");
                summaryQuiz(mCorrectAnswersCounter, mIncorrectAnswersCounter, mScore);
            }
            else
            {
                for(int i = 0; i < mQuestionBank2.size(); i++)
                {
                    mQuestionBank2.get(i).setIsAnswered(savedInstanceState.getBoolean("IsAnswered" + i));
                    if(mQuestionBank2.get(mCurrentIndex).getIsAnswered())
                    {
                        setAnswerButtonVisibility(false);
                        mShowAnswerButton.setEnabled(true);
                    }
                    else
                    {
                        setAnswerButtonVisibility(true);
                        mShowAnswerButton.setEnabled(false);
                    }
                }
                updateQuestion();
            }
        }
    }

    private void updateQuestion()
    {
        String question = mQuestionBank2.get(mCurrentIndex).getQuestion();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank2.get(mCurrentIndex).isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue)
        {
            messageResId = R.string.correct_answer;
            mCorrectAnswersCounter++;
        }
        else
        {
            messageResId = R.string.incorrect_answer;
            mIncorrectAnswersCounter++;
        }
        mIsAnswered = true;
        mQuestionBank2.get(mCurrentIndex).setIsAnswered(true);
        setAnswerButtonVisibility(false);
        mShowAnswerButton.setEnabled(true);
        if (CheatAnswer.isCheated)
        {
            mCheatedAnswersCounter++;
            CheatAnswer.isCheated = false;
            Toast.makeText(this, "CHEATER", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAnswerButtonVisibility(boolean b)
    {
        mTrueButton.setEnabled(b);
        mFalseButton.setEnabled(b);
        mCheatButton.setEnabled(b);
    }

    private boolean endQuiz()
    {
        if (mCorrectAnswersCounter + mIncorrectAnswersCounter == mQuestionBank2.size())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void summaryQuiz(int CorrectAnswers, int IncorrectAnswers, double Score)
    {
        setUnusedObjectVisibility(View.INVISIBLE);
        for (int i = 0; i < mQuestionBank2.size(); i++) mQuestionBank2.get(i).setIsAnswered(false);

        mSummaryTextView.setText("Correct answer: " + CorrectAnswers + "\n" +
                "Wrong answer: " + IncorrectAnswers + "\n\n");
        if (mCheatedAnswersCounter > 0)
        {
            System.out.println(mCheatedAnswersCounter);
            Score -= (mHowManySubtract * mCheatedAnswersCounter);
            mCheatedSummaryTextView.setText("Cheated questions: " + mCheatedAnswersCounter +
                    "\nAnd therefore this fact subtracts you:\n" +
                    mHowManySubtract + "%\n");
        }
        mScoreTextView.setText("Score: " + Math.round(Score) + "%");
    }

    private void setUnusedObjectVisibility(int isVisible)
    {
        if (isVisible == View.INVISIBLE)
        {
            mRestartButton.setVisibility(View.VISIBLE);
            mScoreTextView.setVisibility(View.VISIBLE);
            mCheatedSummaryTextView.setVisibility(View.VISIBLE);
            mSummaryTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            mRestartButton.setVisibility(View.INVISIBLE);
            mScoreTextView.setText(null);
            mCheatedSummaryTextView.setText(null);
            mSummaryTextView.setText(null);
        }

        mTrueButton.setVisibility(isVisible);
        mFalseButton.setVisibility(isVisible);
        mNextButton.setVisibility(isVisible);
        mPreviousButton.setVisibility(isVisible);
        mCheatButton.setVisibility(isVisible);
        mQuestionTextView.setVisibility(isVisible);
        mShowAnswerButton.setVisibility(isVisible);
        mSearchButton.setVisibility(isVisible);
        mAddQuestionButton.setVisibility(isVisible);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        for(int i = 0; i < mQuestionBank2.size(); i++)
        {
            outState.putBoolean("IsAnswered" + i, mQuestionBank2.get(i).getIsAnswered());
        }
        outState.putInt("Index", mCurrentIndex);
        outState.putInt("CorrectAnswers", mCorrectAnswersCounter);
        outState.putInt("IncorrectAnswers", mIncorrectAnswersCounter);
        outState.putInt("CheatedAnswers", mCheatedAnswersCounter);
        outState.putDouble("Score", mScore);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
        {
            mQuestionBank2.add(new Question(data.getStringExtra("Question"), data.getBooleanExtra("Answer", false), false));
        }
    }

}