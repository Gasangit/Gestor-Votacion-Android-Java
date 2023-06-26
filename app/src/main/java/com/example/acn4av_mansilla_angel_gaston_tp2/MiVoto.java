package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContentInfo;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MiVoto extends AppCompatActivity {

    private final String presentClass = this.getClass().getSimpleName();
    private String optionText = "";
    private String optionId = "";
    TextView textDescription, textMyVoteIs;
    RadioGroup optionSelection;
    RadioButton radioOpcion1, radioOpcion2, radioOpcion3;
    Button buttonVote, buttonReturn;
    HelperDatabase Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_voto);

        textDescription = findViewById(R.id.myVoteTextDescription);
        textMyVoteIs = findViewById(R.id.myVoteTextMyVoteIs);

        optionSelection = findViewById(R.id.myVoteRadioGroup);
        radioOpcion1 = findViewById(R.id.myVoteRadioButtonOption1);
        radioOpcion2 = findViewById(R.id.myVoteRadioButtonOption2);
        radioOpcion3 = findViewById(R.id.myVoteRadioButtonOption3);

        buttonReturn = findViewById(R.id.myVoteButtonReturn);
        buttonVote = findViewById(R.id.myVoteButtonVote);

        Bundle dataReception = getIntent().getExtras();
        String idUser = dataReception.getString("idUser");
        String idVoting = dataReception.getString("idVotacion");
        String idOrganized = dataReception.getString("idOrganizador");
        String votingDescription = dataReception.getString("descripcion");
        String opcion1 = dataReception.getString("opcion1");
        String opcion1Id = dataReception.getString("opcion1Id");
        String opcion2 = dataReception.getString("opcion2");
        String opcion2Id = dataReception.getString("opcion2Id");
        String opcion3 = dataReception.getString("opcion3");
        String opcion3Id = dataReception.getString("opcion3Id");

        textDescription.setText(votingDescription);

        Intent goToMyVotings = new Intent(MiVoto.this, MisVotaciones.class);
        goToMyVotings.putExtras(dataReception);

        Log.d(presentClass, "\n OPCION 1 : ID : " + opcion1Id + " DESCRIPCION : " + opcion1 +
                "\n OPCION 2 : ID : " + opcion2Id + " DESCRIPCION : " + opcion2 +
                "\n OPCION 3 : ID : " + opcion3Id + " DESCRIPCION : " + opcion3);

        if(opcion1.equalsIgnoreCase("")) { radioOpcion1.setVisibility(View.INVISIBLE); }
        else { radioOpcion1.setText(opcion1);}
        if(opcion2.equalsIgnoreCase("")) { radioOpcion2.setVisibility(View.INVISIBLE); }
        else { radioOpcion2.setText(opcion2); }
        if(opcion3.equalsIgnoreCase("")) { radioOpcion3.setVisibility(View.INVISIBLE); }
        else { radioOpcion3.setText(opcion3); }

        Helper = new HelperDatabase(this, "dbAppVotes", null, 1);

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(goToMyVotings);
            }
        });

        buttonVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase databaseRead = Helper.getReadableDatabase();

                Cursor selectResult = databaseRead.rawQuery("SELECT * " +
                                "FROM usuario_votacion " +
                                "WHERE usuario_votacion_id_usuario_votante = '" + idUser + "' " +
                                "AND usuario_votacion_id_votacion = '" + idVoting  + "'",null);

                if(selectResult.moveToFirst()) {

                    Toast.makeText(MiVoto.this, "Ya ha votado en esta votación", Toast.LENGTH_SHORT).show();

                    Cursor selectFromOpcion = databaseRead.rawQuery("SELECT * " +
                            "FROM opcion " +
                            "WHERE id_opcion = '" + selectResult.getString(3) + "'", null);

                    if(selectFromOpcion.moveToFirst()) {
                        Log.d(presentClass, "ID SELECCION PREVIA : " + selectFromOpcion.getString(0));
                        String messageText = "Ya ha votado por la opción " + selectFromOpcion.getString(1);
                        textMyVoteIs.setText(messageText);

                        if(opcion1Id.equalsIgnoreCase(selectFromOpcion.getString(0))) radioOpcion1.setChecked(true);
                        if(opcion2Id.equalsIgnoreCase(selectFromOpcion.getString(0))) radioOpcion2.setChecked(true);
                        if(opcion3Id.equalsIgnoreCase(selectFromOpcion.getString(0))) radioOpcion3.setChecked(true);
                    }
                } else {
                    Log.d(presentClass, "OPCION 1 CHECK : " + radioOpcion1.isChecked() +
                            "   OPCION 2 CHECK : " + radioOpcion2.isChecked() +
                            "   OPCION 3 CHECK : " + radioOpcion3.isChecked());

                    if(radioOpcion1.isChecked()) { optionText = opcion1; optionId = opcion1Id;}
                    if(radioOpcion2.isChecked()) { optionText = opcion2; optionId = opcion2Id;}
                    if(radioOpcion3.isChecked()) { optionText = opcion3; optionId = opcion3Id;}

                    SQLiteDatabase databaseWrite = Helper.getWritableDatabase();

                    ContentValues insertColumns = new ContentValues();
                    insertColumns.put("usuario_votacion_id_usuario_votante", idUser);
                    insertColumns.put("usuario_votacion_id_votacion", idVoting);

                    if(!optionId.equalsIgnoreCase("")) {
                        insertColumns.put("usuario_votacion_id_opcion", optionId);

                        databaseWrite.insert("usuario_votacion", null, insertColumns);
                        Toast.makeText(MiVoto.this, "Ha votado satisfactoriamente", Toast.LENGTH_SHORT).show();

                        startActivity(goToMyVotings);
                    }else {
                        Toast.makeText(MiVoto.this, "Debe seleccionar alguna opcion", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });


    }
}