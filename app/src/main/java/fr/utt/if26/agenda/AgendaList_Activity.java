package fr.utt.if26.agenda;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AgendaList_Activity extends AppCompatActivity {

    private Agenda_list agenda_list;
    private int agendaListId;
    private BDOpenHelper bdOpenHelper;

    private ArrayList<Agenda> agendas;
    private ListView listViewAgenda;
    private TextView agendaListeNameView;
    private Button addbtn1;
    private AgendaAdapter adapter;

    public ActivityResultLauncher<Intent> launchAddAgendaActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_list);

        //findViewById()
        agendaListeNameView = findViewById(R.id.AgendaListNameView);
        listViewAgenda = findViewById(R.id.ListviewAgendaListNameView);
        addbtn1 = findViewById(R.id.addAgendaButton);
        bdOpenHelper = new BDOpenHelper(getApplicationContext());
        agendas = new ArrayList<>();

        agenda_list = getIntent().getParcelableExtra("AgendaList");
        agendaListId = agenda_list.getId();

        agendaListeNameView.setText(agenda_list.getName());

        UpdateDisplay();
        launchAddAgendaActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        String message = result.getData().getStringExtra("RESULT_ADD_AGENDA");

                        UpdateDisplay();
                    }
                }
        );

        addbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAddAgendaActivity = new Intent(getApplicationContext(), AddTacheActivity.class);
                startAddAgendaActivity.putExtra("AgendaListId", agendaListId);
                launchAddAgendaActivity.launch(startAddAgendaActivity);
            }
        });
    }
    private void UpdateDisplay(){
        agendas = bdOpenHelper.getAllAgenda(agendaListId);

        adapter = new AgendaAdapter(getApplicationContext(), agendas);

        listViewAgenda.setAdapter(adapter);
    }
}