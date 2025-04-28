package br.com.valorcerto.quizinterativo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.valorcerto.quizinterativo.model.Question;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "quiz.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_Q = "questions";
    private static final String COL_ID = "id";
    private static final String COL_TEXT = "text";
    private static final String COL_OPT1 = "opt1";
    private static final String COL_OPT2 = "opt2";
    private static final String COL_OPT3 = "opt3";
    private static final String COL_OPT4 = "opt4";
    private static final String COL_ANS = "answer"; // índice [1-4]

    public DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_Q + " ("
                + COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TEXT  + " TEXT, "
                + COL_OPT1  + " TEXT, "
                + COL_OPT2  + " TEXT, "
                + COL_OPT3  + " TEXT, "
                + COL_OPT4  + " TEXT, "
                + COL_ANS   + " INTEGER)";
        db.execSQL(sql);

        // Popula com perguntas iniciais:
        insert(db, "Qual a capital do Brasil?",
                new String[]{"São Paulo","Brasília","Rio","Salvador"}, 2);
        insert(db, "2 + 2 é igual a?",
                new String[]{"3","4","5","6"}, 2);
        // … mais perguntas à sua criatividade
    }

    private void insert(SQLiteDatabase db, String text, String[] opts, int ans) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TEXT, text);
        cv.put(COL_OPT1, opts[0]);
        cv.put(COL_OPT2, opts[1]);
        cv.put(COL_OPT3, opts[2]);
        cv.put(COL_OPT4, opts.length>3? opts[3] : "");
        cv.put(COL_ANS, ans);
        db.insert(TABLE_Q, null, cv);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Q);
        onCreate(db);
    }

    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_Q, null, null, null, null, null, null);
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndexOrThrow(COL_ID));
            String txt = c.getString(c.getColumnIndexOrThrow(COL_TEXT));
            String[] opts = {
                    c.getString(c.getColumnIndexOrThrow(COL_OPT1)),
                    c.getString(c.getColumnIndexOrThrow(COL_OPT2)),
                    c.getString(c.getColumnIndexOrThrow(COL_OPT3)),
                    c.getString(c.getColumnIndexOrThrow(COL_OPT4))
            };
            int ans = c.getInt(c.getColumnIndexOrThrow(COL_ANS)) - 1;
            list.add(new Question(id, txt, opts, ans));
        }
        c.close();
        return list;
    }
}