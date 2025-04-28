package br.com.valorcerto.quizinterativo.ui;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.*;

import br.com.valorcerto.quizinterativo.R;
import br.com.valorcerto.quizinterativo.data.DBHelper;
import br.com.valorcerto.quizinterativo.model.Question;

import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private List<Question> questions;
    private int idx=0, score;
    private TextView tvQ;
    private RadioGroup rg;
    private RadioButton[] rbs = new RadioButton[4];
    private Button btn;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_quiz);
        score = getIntent().getIntExtra("score",0);

        tvQ = findViewById(R.id.tvQuestion);
        rg  = findViewById(R.id.rgOptions);
        rbs[0]=findViewById(R.id.rb1);
        rbs[1]=findViewById(R.id.rb2);
        rbs[2]=findViewById(R.id.rb3);
        rbs[3]=findViewById(R.id.rb4);
        btn = findViewById(R.id.btnAnswer);

        questions = new DBHelper(this).getAllQuestions();
        showQuestion();

        btn.setOnClickListener(v -> {
            int sel = rg.getCheckedRadioButtonId();
            if (sel==-1) return; // sem seleção
            int chosen = -1;
            for(int i=0;i<4;i++){
                if(rbs[i].getId()==sel) chosen=i;
            }
            boolean ok = (chosen==questions.get(idx).getCorrectIndex());
            if (ok) score++;
            new AlertDialog.Builder(this)
                    .setTitle(ok?"Correto!":"Errado!")
                    .setMessage(ok?"Boa!": "Resposta certa: "+
                            questions.get(idx).getOptions()[questions.get(idx).getCorrectIndex()])
                    .setPositiveButton("OK",(d,w)-> {
                        idx++;
                        if(idx<questions.size()) {
                            rg.clearCheck();
                            showQuestion();
                        } else {
                            Intent i = new Intent(this, ResultActivity.class);
                            i.putExtra("score", score);
                            i.putExtra("total", questions.size());
                            startActivity(i);
                            finish();
                        }
                    }).setCancelable(false).show();
        });
    }

    private void showQuestion() {
        Question q = questions.get(idx);
        tvQ.setText(q.getText());
        String[] opts = q.getOptions();
        for(int i=0;i<4;i++){
            rbs[i].setText(opts[i]);
            rbs[i].setVisibility(opts[i].isEmpty()?RadioButton.GONE:RadioButton.VISIBLE);
        }
    }
}