package fr.utt.if26.agenda;

import static fr.utt.if26.agenda.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTacheActivity extends AppCompatActivity {

    private String tacheName;
    private BDOpenHelper bdOpenHelper;
    private int IdAgendaList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_add_tache);

        bdOpenHelper = new BDOpenHelper(getApplicationContext());
        IdAgendaList = getIntent().getIntExtra("AgendaListId", 0);

         Button btnAdd = findViewById(R.id.btn_add);
         EditText edit_text = findViewById(R.id.edit_text);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tacheName = edit_text.getText().toString();
                bdOpenHelper.insertAgenda(tacheName,IdAgendaList);

                Intent intent = new Intent();
                intent.putExtra("AJOUT DE TACHE", "OK");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}