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

public class AgendaAdapter extends ArrayAdapter<Agenda>{
    public ArrayList<Agenda> agendas;
    public BDOpenHelper bdOpenHelper;

    public AgendaAdapter(@NonNull Context context, ArrayList<Agenda> agendas) {
        super(context, 0, agendas);
        this.agendas = agendas;
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
                    R.layout.agenda_listview_row, parent, false
            );
        }

        TextView textviewtachename = convertView.findViewById(R.id.text_view_name_row);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox_for_agenda);

        Agenda curentAgenda = getItem(position);

        if(curentAgenda != null ){
            textviewtachename.setText(curentAgenda.getName());
        }

        checkBox.setChecked(curentAgenda.isDone());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    bdOpenHelper.updateAgenda(1, curentAgenda.getId());
                }else {
                    bdOpenHelper.updateAgenda(0, curentAgenda.getId());
                }
            }
        });

        return convertView;
    }
}
