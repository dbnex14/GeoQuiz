package com.learning.dino.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CheatActivity extends ActionBarActivity {

    //This is an intent extra passed into receiving activity.  Since activities can be called
    //from many places, so best way to define keys for an extra is in the recieving  activity.
    //Using your package name as a qualifier for your extra prevents name colisions with extras
    //from other applications.
    public static final String EXTRA_ANSWER_IS_TRUE = "com.learning.dino.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "com.learning.dino.geoquiz.answer_shown";

    private static final String TAG = "CheatActivity";
    private static final String KEY_CHEATER = "cheater"; //key-value to record cheat action and save it

    private boolean mAnswerIsTrue;
    private boolean mIsCheater;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    private void setAnswerTextBox(boolean isTrue){
        if (isTrue){
            mAnswerTextView.setText(R.string.true_button);
        }else{
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        //Answer is not shown until user clicks on button to show it.
        setAnswerShownResult(false);
        //mIsCheater = false;

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView)findViewById(R.id.answerTextView);
        mShowAnswerButton = (Button)findViewById(R.id.showAnswerButton);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsCheater = true; //user clicked Show Answer button to cheat.
                setAnswerTextBox(mAnswerIsTrue);
                setAnswerShownResult(true); //user viewed answer
            }
        });

        if (savedInstanceState != null){
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
            if (mIsCheater){
                setAnswerTextBox(mAnswerIsTrue); //if orientation changed, show answer is user saw it already
                setAnswerShownResult(true); //user viewed answer
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState called");
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cheat, menu);
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
