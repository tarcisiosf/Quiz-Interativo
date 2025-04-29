package br.com.valorcerto.quizinterativo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import br.com.valorcerto.quizinterativo.R;

public class ResultActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_result);
        int score = getIntent().getIntExtra("score",0);
        int total = getIntent().getIntExtra("total",1);

        TextView tvS = findViewById(R.id.tvScore);
        TextView tvM = findViewById(R.id.tvMessage);
        Button  bt = findViewById(R.id.btnRestart);

        tvS.setText("Você acertou " + score + " de " + total);

        float pct = 100f * score / total;
        tvM.setText(pct >= 70 ? "Parabéns!" : "Tente novamente!");


        SharedPreferences p = getSharedPreferences("quiz_prefs", MODE_PRIVATE);
        int best = p.getInt("best_score", 0);
        if (score > best) {
            p.edit().putInt("best_score", score).apply();
        }

        bt.setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });
    }
}