package pl.edu.pum.physicsquiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable
{
    private String mQuestion;
    private boolean mAnswerTrue;
    private boolean mIsAnswered;

    public Question(String Question, boolean answerTrue, boolean IsAnswered)
    {
        this.mQuestion = Question;
        this.mAnswerTrue = answerTrue;
        this.mIsAnswered = IsAnswered;
    }

    protected Question(Parcel in)
    {
        mQuestion = in.readString();
        mAnswerTrue = in.readByte() != 0;
        mIsAnswered = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mQuestion);
        dest.writeByte((byte) (mAnswerTrue ? 1 : 0));
        dest.writeByte((byte) (mIsAnswered ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>()
    {
        @Override
        public Question createFromParcel(Parcel in)
        {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size)
        {
            return new Question[size];
        }
    };

    public String getQuestion()
    {
        return mQuestion;
    }

    public void setTextResId(String textResId)
    {
        this.mQuestion = textResId;
    }

    public boolean isAnswerTrue()
    {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue)
    {
        this.mAnswerTrue = answerTrue;
    }

    public void setIsAnswered(boolean isAnswered) { this.mIsAnswered = isAnswered; }

    public boolean getIsAnswered() {return mIsAnswered;}
}
