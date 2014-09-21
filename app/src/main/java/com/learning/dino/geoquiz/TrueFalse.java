package com.learning.dino.geoquiz;

/**
 * Created by dbulj on 21/09/2014.
 */
public class TrueFalse {
    private int mQuestion; //will hold resource Id of question, hence int
    private boolean mTrueQuestion;

    public TrueFalse(int question, boolean trueQuestion){
        setmQuestion(question);
        setmTrueQuestion(trueQuestion);
    }

    public int getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(int mQuestion) {
        this.mQuestion = mQuestion;
    }

    public boolean ismTrueQuestion() {
        return mTrueQuestion;
    }

    public void setmTrueQuestion(boolean mTrueQuestion) {
        this.mTrueQuestion = mTrueQuestion;
    }
}
