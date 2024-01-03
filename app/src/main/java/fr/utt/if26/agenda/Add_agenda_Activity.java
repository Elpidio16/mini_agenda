package fr.utt.if26.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_agenda_Activity extends AppCompatActivity {
    private String agendaListeName;
    private BDOpenHelper BDOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);

        BDOpenHelper = new BDOpenHelper(getApplicationContext());
        Button confirmBtn = findViewById(R.id.Confirm_btn);

        EditText tacheList_edit_text = findViewById(R.id.List_edit_text);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agendaListeName = tacheList_edit_text.getText().toString();
                BDOpenHelper.insertTache(agendaListeName);

                Intent startTcheListActivity = new Intent(getApplicationContext(), AcceuilActivity.class);
                Toast.makeText(Add_agenda_Activity.this, "Tache ajouter avec succes", Toast.LENGTH_LONG).show();
                startActivity(startTcheListActivity);

            }
        });
    }
}