package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MisVotaciones extends AppCompatActivity {

    private final String presentClass = this.getClass().getSimpleName();
    RecyclerView RecyclerViewMyVotings;
    HelperDatabase Helper;
    Button buttonReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_votaciones);

        RecyclerViewMyVotings = findViewById(R.id.recyclerViewMyVotings);
        buttonReturn = findViewById(R.id.myVotingsButtonReturn);

        Bundle idUserReception = getIntent().getExtras();
        String idUser = idUserReception.getString("idUser");

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToStart = new Intent(MisVotaciones.this, Inicio.class);
                goToStart.putExtras(idUserReception);
                startActivity(goToStart);
            }
        });

        Helper = new HelperDatabase(this, "dbAppVotes", null, 1);
        SQLiteDatabase databaseRead = Helper.getReadableDatabase();

        Cursor selectResult = databaseRead.rawQuery(
                "SELECT  * FROM votacion", null);

        VotacionObjeto [] arrayVotings = new VotacionObjeto[selectResult.getCount()];

        while(selectResult.moveToNext()) {

            Log.d(presentClass, "idVotacion : " + selectResult.getString(0) +
                    " idOrganizador : " + selectResult.getString(1)+" Descripcion : " + selectResult.getString(2));
            VotacionObjeto voting = new VotacionObjeto(
                    selectResult.getString(0),
                    selectResult.getString(1),
                    selectResult.getString(2)
            );

            Cursor selectResultOptions = databaseRead.rawQuery(
                    "SELECT op.id_opcion, op.opcion_descripcion\n" +
                            "FROM opcion AS op\n" +
                            "INNER JOIN opcion_votacion AS opvo\n" +
                            "ON op.id_opcion = opvo.opcion_votacion_id_opcion\n" +
                            "WHERE opvo.opcion_votacion_id_votacion = '" + selectResult.getString(0) + "'", null);

            while(selectResultOptions.moveToNext()) {
                if(selectResultOptions.getPosition() == 0) {
                    voting.setOpcion1(selectResultOptions.getString(1));
                    voting.setOpcion1Id(selectResultOptions.getString(0));
                    Log.d(presentClass, "OPCION 1 : id : " + selectResultOptions.getString(0) +
                            " Descripcion : " + selectResultOptions.getString(1));
                }
                if(selectResultOptions.getPosition() == 1) {
                    voting.setOpcion2(selectResultOptions.getString(1));
                    voting.setOpcion2Id(selectResultOptions.getString(0));
                    Log.d(presentClass, "OPCION 2 : id : " + selectResultOptions.getString(0) +
                            " Descripcion : " + selectResultOptions.getString(1));
                }
                if(selectResultOptions.getPosition() == 2) {
                    voting.setOpcion3(selectResultOptions.getString(1));
                    voting.setOpcion3Id(selectResultOptions.getString(0));
                    Log.d(presentClass, "OPCION 3 : id : " + selectResultOptions.getString(0) +
                            " Descripcion : " + selectResultOptions.getString(1));
                }
            }
            arrayVotings[selectResult.getPosition()] = voting;
        }

        adaptadorDeVistas adapter = new adaptadorDeVistas(arrayVotings,idUser);
        RecyclerViewMyVotings.setAdapter(adapter);
    }
}