package sdsu.andriod.assignment2a;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private TextView scoreView;
    private EditText firstName;
    private EditText familyName;
    private EditText nickName;
    private EditText age;

    private static final int INTENT_REQUEST_ID = 12345;
    private String score;
    private static final String FILE_NAME = "user-info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkViewWidgetsWithClassAttributes();
        loadInfoFromFile();
    }

    public void takeQuiz(View view) {
        Intent startQuizActivity = new Intent(this, QuizActivity.class);
//        startActivity(startQuizActivity);
        startActivityForResult(startQuizActivity, INTENT_REQUEST_ID);
    }


    public void saveInfo(View view) {

        File file = new File(this.getFilesDir(), FILE_NAME);
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;

        try {
            Log.d("sachin", file.getAbsoluteFile().toString());
            fileWriter = new FileWriter(file.getAbsoluteFile());
            bufferedWriter = new BufferedWriter(fileWriter);

            JSONObject jsonObject = createInfoInJsonObj();
            bufferedWriter.write(jsonObject.toString());
            bufferedWriter.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONObject createInfoInJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", firstName.getText());
        jsonObject.put("familyName", familyName.getText());
        jsonObject.put("nickName", nickName.getText());
        jsonObject.put("age", age.getText());
        jsonObject.put("score", score);
        return jsonObject;
    }

    private void loadInfoFromFile() {

        StringBuffer output = new StringBuffer();
        Context ctx = getApplicationContext();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = ctx.openFileInput(FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            bufferedReader.close();
            if (output.equals("")) return;

            Log.d("sachin", output.toString());
            JSONObject jsonResponse = new JSONObject(output.toString());
            Log.d("sachin", jsonResponse.toString());

            populateView(jsonResponse);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != INTENT_REQUEST_ID) {
            return;
        }
        switch (resultCode) {
            case RESULT_OK:
                score = data.getStringExtra("score");
                break;
            case RESULT_CANCELED:
                break;
        }
        scoreView.setText(score);
    }

    private void linkViewWidgetsWithClassAttributes() {

        scoreView = findViewById(R.id.score);
        firstName = findViewById(R.id.firstName);
        familyName = findViewById(R.id.familyName);
        nickName = findViewById(R.id.nickName);
        age = findViewById(R.id.age);
    }

    private void populateView(@NonNull JSONObject jsonObj) throws JSONException {

        firstName.setText(jsonObj.get("firstName").toString());
        familyName.setText(jsonObj.get("familyName").toString());
        nickName.setText(jsonObj.get("nickName").toString());
        age.setText(jsonObj.get("age").toString());
        scoreView.setText(jsonObj.get("score").toString());
    }
}
