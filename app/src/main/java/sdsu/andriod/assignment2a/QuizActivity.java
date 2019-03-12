package sdsu.andriod.assignment2a;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements Q1Fragment.UpdateScoreListener {

    private List<QuestionModel> questions;
    private List<Fragment> questionFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        loadQuestions();
        createFragments();
        showQuestion(0);
        printQuestion();

    }


    private void createFragments() {

        questionFragments = new ArrayList<>();
        //        Q1Fragment q1Fragment = (Q1Fragment) fragmentManager.findFragmentById(R.id.fragment_q1);
        Q1Fragment q1Fragment = new Q1Fragment();
        Q1Fragment q2Fragment = new Q1Fragment();
        Q1Fragment q3Fragment = new Q1Fragment();
        Q1Fragment q4Fragment = new Q1Fragment();

        q1Fragment.setQuestion(questions.get(0));
        q2Fragment.setQuestion(questions.get(1));
        q3Fragment.setQuestion(questions.get(2));
        q4Fragment.setQuestion(questions.get(3));

        questionFragments.add(0, q1Fragment);
        questionFragments.add(1, q2Fragment);
        questionFragments.add(2, q3Fragment);
        questionFragments.add(3, q4Fragment);
    }


    public void loadQuestions() {

        List<QuestionModel> questions = new ArrayList<>();
        String fileName = "questions.json";
        try {
            JSONArray jArray = new JSONArray(readJSONFromAsset(fileName));

            for (int i = 0; i < jArray.length(); ++i) {

                QuestionModel question = new QuestionModel();
                question.setId(jArray.getJSONObject(i).getInt("id"));// id of the question
                question.setQuestion(jArray.getJSONObject(i).getString("question")); // question description
                question.setOption1(jArray.getJSONObject(i).getString("option1"));
                question.setOption2(jArray.getJSONObject(i).getString("option2"));
                question.setOption3(jArray.getJSONObject(i).getString("option3"));
                question.setOption4(jArray.getJSONObject(i).getString("option4"));
                question.setAnswer(jArray.getJSONObject(i).getString("answer"));
                questions.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.questions = questions;
        printQuestion();
    }

    public String readJSONFromAsset(String fileName) {
        String json;
        try {

            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


//    public void back(View button) {
//        Log.i("sachin", "Back");
//        Intent toPassBack = getIntent();
//        toPassBack.putExtra("age", 10);
//        setResult(RESULT_OK, toPassBack);
//        finish();
//    }

    public void printQuestion() {
        for (int i = 0; i < questions.size(); ++i) {
            Log.d("sachin", questions.get(i).toString());
        }
    }

    @Override
    public void updateScore(int questionID, int score) {
        QuestionModel questionModel = questions.get(questionID);
        questionModel.setScore(score);
    }

    @Override
    public void showQuestion(int questionID) {

        if (questionID >= 4 || questionID < 0) {

            Log.i("sachin", "Back");
            Intent toPassBack = getIntent();
            toPassBack.putExtra("score", calculateTotalScore());
            setResult(RESULT_OK, toPassBack);
            finish();
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, questionFragments.get(questionID));
        if (questionID != 0) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public String calculateTotalScore() {

        int totalScore = 0;
        for (int i = 0; i < questions.size(); ++i) {
            Log.d("sachin", questions.get(i).toString());
            totalScore += questions.get(i).getScore();
        }
        return Integer.toString(totalScore);
    }
}

