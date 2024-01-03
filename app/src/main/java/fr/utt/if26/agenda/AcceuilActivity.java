package fr.utt.if26.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class AcceuilActivity extends AppCompatActivity {
    private ListView listeView_des_taches;
    private ArrayList<Agenda_list> liste_des_Agenda = new ArrayList<>();
    private ArrayList<Agenda_list> liste_des_Agenda_original = new ArrayList<>();
    private BDOpenHelper bdopenHelper;
    private Agenda_listAdapter Agenda_listAdapter;
    private Button addTacheButton;
    private EditText searchBarListTachesList;
    //edition definition
    public static boolean isActionMode = false;
    public static ArrayList<Agenda_list> userSelectionList = new ArrayList<>();
    public static ActionMode actionModeList = null;

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        bdopenHelper = new BDOpenHelper(getApplicationContext());
        //Recuperation des id
        listeView_des_taches = findViewById(R.id.listeView_des_taches);
        addTacheButton = findViewById(R.id.addTacheButton);
        searchBarListTachesList = findViewById(R.id.search_bar_liste_des_taches);

        //faire plusieur selection de supression
        listeView_des_taches.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listeView_des_taches.setMultiChoiceModeListener(modeListener);

        //extentiation des donnée de la base avec celuis des arraylists
        liste_des_Agenda = bdopenHelper.getAllTachesList();
        liste_des_Agenda_original = bdopenHelper.getAllTachesList();

        //creation d'un objet tacheListeAdapter
        Agenda_listAdapter = new Agenda_listAdapter(this, liste_des_Agenda);

        listeView_des_taches.setAdapter(Agenda_listAdapter);

        //ajout methode implementant l'ajout de nouvelle tache
        addTacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startActivityAddagendaList = new Intent(getApplicationContext(), Add_agenda_Activity.class);
                startActivity(startActivityAddagendaList);
            }
        });

        //alarm implement
        addTacheButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent starAlarmActivity = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(starAlarmActivity);
                return false;
            }
        });

        //Methode pour implementer la bar de recherche
        searchBarListTachesList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                liste_des_Agenda = FilterlisteDesTaches(charSequence.toString());
                Agenda_listAdapter = new Agenda_listAdapter(getApplicationContext(), liste_des_Agenda);
                listeView_des_taches.setAdapter(Agenda_listAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // fonctionne en cas de selection d'un element de la liste
        listeView_des_taches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Agenda_list agenda_list = (Agenda_list) adapterView.getItemAtPosition(i);

                Intent startAgendaList_Activity = new Intent(getApplicationContext(), AgendaList_Activity.class);
                startAgendaList_Activity.putExtra("AgendaList", agenda_list);
                startActivity(startAgendaList_Activity);
            }
        });


        //Connexion avec enprunt biometrique
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(AcceuilActivity.this, "Enprunte non reconu!", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(AcceuilActivity.this, "Enprunte ne fonctionne pas!", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(AcceuilActivity.this, "Enprunte invalide!", Toast.LENGTH_SHORT).show();
                break;

        }

        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(AcceuilActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(AcceuilActivity.this, "Connexion reuissi Bienvenu!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });



        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Utilise ton emprunt pour te connecter")
                .setDeviceCredentialAllowed(true)
                .build();

        biometricPrompt.authenticate(promptInfo);


    }

    //menu de deconnexion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                Intent ExitIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ExitIntent);
                Toast.makeText(this, "Deconnexion GOOD BYE", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Methode pour filtrer le text saisie par l'utilisateur lors de l'ajout
    private ArrayList<Agenda_list> FilterlisteDesTaches(String textFilter){

        //creation d'une variable temporaire de type arrayliste. valable que pour cette methode
        ArrayList<Agenda_list> arrayListTemp = new ArrayList<>();
        if(textFilter != null){
            for (int i=0; i<liste_des_Agenda_original.size(); i++){
                //cette condition permet de verifir si ce que l'utilisateur saisi dans labre de recherche existe bien dans la liste
                if (liste_des_Agenda_original.get(i).getName().toUpperCase().contains(textFilter.toUpperCase())){
                    //si l'element existe on l'ajoute dans arrayListTemp
                    arrayListTemp.add(liste_des_Agenda_original.get(i));
                }
            }
        }
        else {
            arrayListTemp = liste_des_Agenda_original;
        }
        return arrayListTemp;
    }

    //
    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);

            isActionMode = true;
            actionModeList = actionMode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.action_delete:
                    //supprimer
                    bdopenHelper.delete(userSelectionList);
                    updateDisplayAgenda();
                    actionMode.finish();
                    Toast.makeText(AcceuilActivity.this, "tache supprimer avec succes!", Toast.LENGTH_LONG).show();
                    return  true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            isActionMode = false;
            actionModeList = null;
            userSelectionList.clear();
            updateDisplayAgenda();

        }
    };
    private void updateDisplayAgenda(){
        liste_des_Agenda_original = bdopenHelper.getAllTachesList();
        liste_des_Agenda = bdopenHelper.getAllTachesList();

        Agenda_listAdapter = new Agenda_listAdapter(getApplicationContext(), liste_des_Agenda);
        listeView_des_taches.setAdapter(Agenda_listAdapter);
    }
}