package br.com.valorcerto.quizinterativo.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import br.com.valorcerto.quizinterativo.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_welcome);
        Button btn = findViewById(R.id.btnStart);
        btn.setOnClickListener(v -> {
            Intent i = new Intent(this, QuizActivity.class);
            i.putExtra("score", 0);
            startActivity(i);
            finish();
        });
    }
}