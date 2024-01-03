package fr.utt.if26.agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Agenda_listAdapter extends ArrayAdapter<Agenda_list> {
    public ArrayList<Agenda_list> agendaListArrayList;
    public BDOpenHelper bdOpenHelper;

    public Agenda_listAdapter(Context context, ArrayList<Agenda_list> agendaListArrayList){
        super(context, 0, agendaListArrayList);
        this.agendaListArrayList = agendaListArrayList;
        this.bdOpenHelper = new BDOpenHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    public View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.agenda_view_row, parent, false
            );
        }

        TextView textviewtachelistname = convertView.findViewById(R.id.text_view_liste_name_row);

        Agenda_list curentliste = getItem(position);

        if(curentliste != null ){
            textviewtachelistname.setText(curentliste.getName());
        }

        CheckBox checkBox = convertView.findViewById(R.id.checkbox_check);
        checkBox.setTag(position);

        ImageButton edit_btn = convertView.findViewById(R.id.edit_image);
        ImageButton button_check = convertView.findViewById(R.id.button_check);
        EditText edit_text = convertView.findViewById(R.id.edit_text_name_row);

        if (AcceuilActivity.isActionMode){
            checkBox.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.VISIBLE);
        }else{
            checkBox.setVisibility(View.GONE);
            edit_btn.setVisibility(View.GONE);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = (int) compoundButton.getTag();

                if(AcceuilActivity.userSelectionList.contains(agendaListArrayList.get(position))){
                    AcceuilActivity.userSelectionList.remove(agendaListArrayList.get(position));
                }else {
                    AcceuilActivity.userSelectionList.add(agendaListArrayList.get(position));
                }

                AcceuilActivity.actionModeList.setTitle(AcceuilActivity.userSelectionList.size() + " Tâche selectionnee...");

            }
        });

            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.setVisibility(View.GONE);
                    edit_btn.setVisibility(View.GONE);
                    textviewtachelistname.setVisibility(View.GONE);

                    edit_text.setVisibility(View.VISIBLE);
                    edit_text.setText(curentliste.getName());
                    button_check.setVisibility(View.VISIBLE);
                }
            });

            button_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.setVisibility(View.VISIBLE);
                    edit_btn.setVisibility(View.VISIBLE);
                    textviewtachelistname.setVisibility(View.VISIBLE);

                    String newText = edit_text.getText().toString();
                    edit_text.setVisibility(View.GONE);
                    button_check.setVisibility(View.GONE);
                    textviewtachelistname.setText(newText);

                    // modifier le nom de la tâche dans la base de donnee

                    bdOpenHelper.update(newText, curentliste.getId());

                }
            });

        return convertView;
    }
}
