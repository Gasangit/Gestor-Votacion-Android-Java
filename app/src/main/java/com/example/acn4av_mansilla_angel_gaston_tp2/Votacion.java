package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Votacion extends AppCompatActivity {

    private TextView inputDescription, inputOpcion1, inputOpcion2, inputOpcion3;
    private Button buttonGenerate, buttonReturn;
    HelperDatabase Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votacion);

        inputDescription = findViewById(R.id.votingInputDescription);
        buttonGenerate = findViewById(R.id.votingButtonGenerate);
        buttonReturn = findViewById(R.id.votingButtonReturn);

        inputOpcion1 = findViewById(R.id.inputOption1);
        inputOpcion2 = findViewById(R.id.inputOption2);
        inputOpcion3 = findViewById(R.id.inputOption3);

        TextView [] arrayInputs = {inputOpcion1, inputOpcion2, inputOpcion3};

        Bundle bundleUser = getIntent().getExtras();
        String idUser = bundleUser.getString("idUser");

        Helper = new HelperDatabase(this,"dbAppVotes",null, 1);

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToVotingCreate = new Intent(Votacion.this, CrearVotacion.class);
                goToVotingCreate.putExtras(bundleUser);
                startActivity(goToVotingCreate);
            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase databaseWrite = Helper.getWritableDatabase();
                SQLiteDatabase databaseRead = Helper.getReadableDatabase();

                ContentValues containerColumnsVotacion = new ContentValues();
                ContentValues containerColumnsOpcion = new ContentValues();
                ContentValues containerColumnsOpcionVotacion = new ContentValues();

                containerColumnsVotacion.put("votacion_id_usuario_organizador", idUser);
                containerColumnsVotacion.put("votacion_descripcion", inputDescription.getText().toString());

                databaseWrite.insert("votacion",null,containerColumnsVotacion);

                Cursor actualQuery = databaseRead.rawQuery("SELECT MAX(id_votacion) FROM votacion;",null);
                actualQuery.moveToNext();
                String idVotacion = actualQuery.getString(0);

                for (int i = 0; i < arrayInputs.length; i++)
                {
                    if(!arrayInputs[i].getText().toString().equalsIgnoreCase("")){
                        Log.d("CLASE : " + this.getClass().getSimpleName().toString(), "Se guarda opcion " + i);

                        containerColumnsOpcion.put("opcion_descripcion", arrayInputs[i].getText().toString());
                        databaseWrite.insert("opcion", null, containerColumnsOpcion);

                        actualQuery = databaseRead.rawQuery("SELECT MAX(id_opcion) FROM opcion;",null);
                        actualQuery.moveToNext();
                        String idOpcion = actualQuery.getString(0);

                        containerColumnsOpcionVotacion.put("opcion_votacion_id_votacion",idVotacion);
                        containerColumnsOpcionVotacion.put("opcion_votacion_id_opcion", idOpcion);
                        databaseWrite.insert("opcion_votacion",null, containerColumnsOpcionVotacion);
                    }
                    containerColumnsOpcionVotacion.clear();
                }

                databaseWrite.close();
                databaseRead.close();

                inputDescription.setText("");
                inputOpcion1.setText("");
                inputOpcion2.setText("");
                inputOpcion3.setText("");

                Bundle bundleUser = new Bundle();
                bundleUser.putString("idUser", idUser);

                Intent goToCreatevote = new Intent(Votacion.this, CrearVotacion.class);
                goToCreatevote.putExtras(bundleUser);
                startActivity(goToCreatevote);
            }
        });
    }
}