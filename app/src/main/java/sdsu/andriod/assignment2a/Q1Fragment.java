package sdsu.andriod.assignment2a;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
//import android.support.v7.widget.AppCompatButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Q1Fragment extends Fragment {


    private TextView question;
    private RadioGroup radioGroup;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton option4;
    private RadioButton selectedRadioButton;
    private String answer;

    private static final int CORRECT_SCORE = 1;
    private static final int INCORRECT_SCORE = 0;
    private int score = 0;
    private View fragmentView;
    private QuestionModel questionModel;

    public Q1Fragment() {
        // Required empty public constructor
    }

    public void setQuestion(QuestionModel question) {
        this.questionModel = question;
    }

    public interface UpdateScoreListener {
        public void updateScore(int questionID, int score);
        public void showQuestion(int questionID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_q1, container, false);
        linkWidgetsWithAttributes(fragmentView);
        populateAttributes();

        final Button nextButton = fragmentView.findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonID != -1) {
                    // find the radiobutton by returned id
                    selectedRadioButton = fragmentView.findViewById(selectedRadioButtonID);
                    Log.d("sachin", selectedRadioButton.getText().toString());

                    String selectedText = selectedRadioButton.getText().toString();
                    score = (answer.equalsIgnoreCase(selectedText)) ? CORRECT_SCORE : INCORRECT_SCORE;
                    updateScore();
                }
            }
        });

        return fragmentView;
    }

    private void updateScore() {
        UpdateScoreListener listener = (UpdateScoreListener) getActivity();
        listener.updateScore(questionModel.getId(), score);
        listener.showQuestion(questionModel.getId() + 1);
    }


    //    Below method won't be called for some strange reason. All Fragment UI calls goes to Activity java class only
    public void onNextClick(View view) {

        // get selected radio button from radioGroup
        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonID != -1) {
            // find the radiobutton by returned id
            selectedRadioButton = view.findViewById(selectedRadioButtonID);
        }
        Log.d("sachin", selectedRadioButton.getText().toString());
    }


    private void linkWidgetsWithAttributes(View view) {

        question = view.findViewById(R.id.question);
        radioGroup = view.findViewById(R.id.radioGroup);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
    }

    @SuppressLint("SetTextI18n")
    private void populateAttributes() {
        question.setText((questionModel.getId() + 1) + ". " + questionModel.getQuestion());
        option1.setText(questionModel.getOption1());
        option2.setText(questionModel.getOption2());
        option3.setText(questionModel.getOption3());
        option4.setText(questionModel.getOption4());
        answer = questionModel.getAnswer();
    }

}
