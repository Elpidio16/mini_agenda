package fr.utt.if26.agenda;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BDOpenHelper extends SQLiteOpenHelper {
    //déclaration de la base de donnée et deça version
    private static final String DATABASE_NAME = "Mon_Agenda.db";
    private static final int DATABASE_VERSION = 2;

    public BDOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Création de la table AgendaList
        String strSql = "create table AgendaList ("
                +"id integer primary key autoincrement,"
                +"name text not null"
                +")";
        db.execSQL(strSql);

        //Création de la table Agenda
        String strSql1 = "create table Agenda ("
                +"id integer primary key autoincrement,"
                +"name text not null,"
                +"isDone bool not null,"
                +"idAgendaList int not null"
                +")";
        db.execSQL(strSql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String strSql = "DROP TABLE Agenda";
        db.execSQL(strSql);

        String strSql1 = "create table Agenda ("
                +"id integer primary key autoincrement,"
                +"name text not null,"
                +"isDone int not null,"
                +"idAgendaList int not null"
                +")";
        db.execSQL(strSql1);
    }

    public ArrayList<Agenda_list> getAllTachesList(){
        ArrayList<Agenda_list> listTache = new ArrayList<Agenda_list>();

        //pour selectionné tous les élement de la table AgendaList
        String strSql = "select * from AgendaList";
        //pour acceder à la base de donnée.
        //rawQuery permet de retournée les valeurs
        Cursor cursor = this.getWritableDatabase().rawQuery(strSql, null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String AgendaListtName =  cursor.getString(cursor.getColumnIndex("name"));
                //recuperation de la chaine de carractere % utilisé lors de l'insetion de
                //l'insertion dela nouvelle tache
                AgendaListtName = AgendaListtName.replace("((%))","'");

                Agenda_list agendaListtobj = new Agenda_list();
                agendaListtobj.setId(id);
                agendaListtobj.setName(AgendaListtName);

                listTache.add(agendaListtobj);
                cursor.moveToNext();
            }
        }
        return listTache;
    }

    //pour ajouter les taches dans la table TachesListName
    public void insertTache(String AgendaListtName){
        String name = AgendaListtName.replace("'", "((%))");
        String strSql = "INSERT INTO AgendaList"
                + "(name) VALUES ('"
                + name + "')";
        this.getWritableDatabase().execSQL(strSql);
    }
    //modifier
    public void update(String newText, int idAgendaList){
        newText = newText.replace("'", "((%))");
        String strSql = "update AgendaList SET name ='" + newText + "'WHERE id=" + idAgendaList;
        this.getWritableDatabase().execSQL(strSql);
    }
    //supprimer
    public void delete(ArrayList<Agenda_list> listTache){
        for (int i=0; i<listTache.size(); i++){
            String strSql = "DELETE FROM AgendaList WHERE id =" +listTache.get(i).getId();
            this.getWritableDatabase().execSQL(strSql);

            String strSql2 = "DELETE FROM Agenda WHERE idAgendaList=" +listTache.get(i).getId();
            this.getWritableDatabase().execSQL(strSql2);
        }
    }

    public ArrayList<Agenda> getAllAgenda(int agendaListId){
        ArrayList<Agenda> agendas = new ArrayList<>();
        String strSql = "SELECT * FROM Agenda WHERE idAgendaList =" + agendaListId;
        Cursor cursor = this.getWritableDatabase().rawQuery(strSql, null);

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") boolean isDone = cursor.getInt(cursor.getColumnIndex("isDone")) >0;

                Agenda agenda = new Agenda();
                agenda.setId(id);
                agenda.setName(name);
                agenda.setDone(isDone);

                agendas.add(agenda);
                cursor.moveToNext();

            }
        }
        return agendas;
    }

    void insertAgenda(String tacheName, int IdAgendaList){
        tacheName = tacheName.replace("'", "((%))");

        String strSql = "INSERT INTO Agenda(name, isDone, idAgendaList) VALUES('" + tacheName + "'," + 0 + "," + IdAgendaList +")";
        this.getWritableDatabase().execSQL(strSql);
    }

    public void updateAgenda(int newIsDone, int AgendaId){
        String strSql = "UPDATE Agenda SET isDone =" + newIsDone + " WHERE id =" + AgendaId;
        this.getWritableDatabase().execSQL(strSql);
    }
}
