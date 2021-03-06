package com.learning.dino.geoquiz;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*

 Sep 24, 2014
    - Reduced minimum SDK to API8 (Froyo)
    - Added ActionBar display in QuizActivity with guarding for minimum API and TargetAPI Annotation
    - Added display of API level in CheatActivity.


 */
public class QuizActivity extends ActionBarActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATER = "cheater";
    private static final String KEY_CHEAT_INDEX = "index_cheated"; //index of cheated question

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;

    private ImageButton mPrevButton;
    private ImageButton mNextButton;

    private TextView mQuestionTextView;

    private TrueFalse[] mQuestionBank = new TrueFalse[]{
        new TrueFalse(R.string.question_africa, false),
        new TrueFalse(R.string.question_americas, true),
        new TrueFalse(R.string.question_asia, true),
        new TrueFalse(R.string.question_mideast, false),
        new TrueFalse(R.string.question_oceans, true),
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private int mCheatIndex = -1;

    private void updateQuestion(){
        //Log.d(TAG, "updateQuestion() call for question # " + mCurrentIndex, new Exception());
        int question = mQuestionBank[mCurrentIndex].getmQuestion();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismTrueQuestion();
        int messageResId = 0;

        if (mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else{
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInscanceBundle(Bundle) called");

        //Save current question index and whether user cheated so the data is not lost when
        //user changes device screen orientation.
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
        savedInstanceState.putInt(KEY_CHEAT_INDEX, mCheatIndex);
    }

    //Annotation @TargetApi(11) tells Android Lint to ignore possible lint reported errors due to
    //use of AndroidBar which was added in API11 while our MinSDK is 8.  Even though we guard below
    //to use ActionBar only if we have API released after Honeycomb, we still need this annotation
    //to tell Android Lint to surpress errors explicitly.
    @TargetApi(11)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        //Code below will not compile since MinSDK=8, ActionBar was introduced in API11.
        //To overcome the issue, you can either raise MinSDK to API11 but that removes support for
        //majority of Android devices.
        //Instead, wrap ActionBar code in a conditional statement that checks the device's version
        //of Android.
        //ActionBar actionBar = getActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            ActionBar actionBar = getActionBar();
            actionBar.setSubtitle("Bodies of Water");
        }

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = (mCheatIndex == mCurrentIndex);
                updateQuestion(); //tap on text view behave as Next button, too
            }
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkAnswer(false);
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Start CheatActivity
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                //startActivity(i); //starts activity, but does not expects any result from it

                //Start activity with expectation of getting a result back.  Pass it your explicit
                //intent and a user defined integer representing request code.  This request code
                //is used when an activity starts more than one type of child activity and needs to
                //tell who is reporting back.
                startActivityForResult(i, 0);
            }
        });

        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mCurrentIndex = mCurrentIndex - 1;
                if (mCurrentIndex < 0){
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                mIsCheater = (mCheatIndex == mCurrentIndex);
                updateQuestion();
            }
        });

        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = (mCheatIndex == mCurrentIndex);
                updateQuestion();
            }
        });

        //get saved info (i.e. if orientation changes, stay on same question)
        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
            mCheatIndex = savedInstanceState.getInt(KEY_CHEAT_INDEX, -1);
        }

        updateQuestion();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data == null){
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
        if (mIsCheater){
            mCheatIndex = mCurrentIndex; //record current question as cheated
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
