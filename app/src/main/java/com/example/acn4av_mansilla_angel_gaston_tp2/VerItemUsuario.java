package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class VerItemUsuario extends AppCompatActivity {

    private final String presentClass = this.getClass().getSimpleName().toString();

    TextView inputFirstName, inputLastName, inputDni, inputEmail, inputStreet, inputStreetNumber;
    TextView inputPassword, inputRepitePassword;
    CheckBox checkAdmin;
    Button buttonModify, buttonDelete, buttonMap, buttonReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_item_usuario);

        inputFirstName = findViewById(R.id.userItemInputFirstName);
        inputLastName = findViewById(R.id.userItemInputLastName);
        inputDni = findViewById(R.id.userItemInputDni);
        inputEmail = findViewById(R.id.userItemInputMail);
        inputStreet = findViewById(R.id.userItemInputStreet);
        inputStreetNumber = findViewById(R.id.userItemInputStreetNumber);
        inputPassword = findViewById(R.id.userItemInputPassword);
        inputRepitePassword = findViewById(R.id.userItemInputRepitePassword);

        checkAdmin = findViewById(R.id.userItemCheckBoxAdmin);

        buttonReturn = findViewById(R.id.userItemButtonReturn);
        buttonModify = findViewById(R.id.userItemButtonModify);
        buttonDelete = findViewById(R.id.userItemButtonDelete);
        buttonMap = findViewById(R.id.userItemButtonMap);

        Intent goToUserManager = new Intent(VerItemUsuario.this, UsersManager.class);

        HelperDatabase Helper = new HelperDatabase(this,"dbAppVotes", null, 1);

        Bundle dataFromUserManagerActivity = getIntent().getExtras();
        String idUser = dataFromUserManagerActivity.getString("idUser");
        String idItemUser = dataFromUserManagerActivity.getString("idItemUser");

        SQLiteDatabase databaseRead = Helper.getReadableDatabase();
        SQLiteDatabase databaseWrite = Helper.getWritableDatabase();

        Cursor selectFromUser = databaseRead.rawQuery("SELECT * FROM usuario WHERE id_usuario = '" + idItemUser + "'",null);

        if(selectFromUser.moveToFirst()) {
            String itemUserFirstName = selectFromUser.getString(1);
            String itemUserLastName = selectFromUser.getString(2);
            String itemUserDni = selectFromUser.getString(3);
            String itemUserMail = selectFromUser.getString(4);
            String itemUserPassword = selectFromUser.getString(5);
            String itemUserStreet = selectFromUser.getString(6);
            String itemUserStreetNumber = selectFromUser.getString(7    );

            int itemUserAdmin = selectFromUser.getInt(8);

            inputFirstName.setText(itemUserFirstName);inputLastName.setText(itemUserLastName);
            inputDni.setText(itemUserDni); inputEmail.setText(itemUserMail);
            inputStreet.setText(itemUserStreet); inputStreetNumber.setText(itemUserStreetNumber);
            inputPassword.setText(itemUserPassword); inputRepitePassword.setText(itemUserPassword);

            if(itemUserAdmin == 1) checkAdmin.setChecked(true);
            else checkAdmin.setChecked(false);
        }
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserManager.putExtras(dataFromUserManagerActivity);
                startActivity(goToUserManager);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int deleteResult = databaseWrite.delete("usuario","id_usuario = '" + idItemUser + "'",null);

                if(deleteResult == 1){
                    Toast.makeText(VerItemUsuario.this, "Se eliminó el usuario " +
                            inputFirstName.getText() + " " + inputLastName.getText(), Toast.LENGTH_SHORT).show();
                    goToUserManager.putExtras(dataFromUserManagerActivity);
                    startActivity(goToUserManager);
                }else {
                    Toast.makeText(VerItemUsuario.this, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show();
                }

            }
        });
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address = inputStreet.getText().toString() + " " + inputStreetNumber.getText().toString();
                Uri map = Uri.parse("geo:0,0?q=" + Uri.encode(address));

                Intent goToMap = new Intent(Intent.ACTION_VIEW, map);
                startActivity(goToMap);
            }
        });

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputPassword.getText().toString().equals(inputRepitePassword.getText().toString())) {

                    boolean emptyInput = false;

                    String arrayUpdate [][] = {
                            {"usuario_nombre", inputFirstName.getText().toString()},
                            {"usuario_apellido", inputLastName.getText().toString()},
                            {"usuario_dni", inputDni.getText().toString()},
                            {"usuario_mail", inputEmail.getText().toString()},
                            {"usuario_password", inputPassword.getText().toString()},
                            {"usuario_calle", inputStreet.getText().toString()},
                            {"usuario_altura", inputStreetNumber.getText().toString()}
                    };

                    ContentValues updateUser = new ContentValues();

                    for(int i = 0; i < arrayUpdate.length; i++) {

                        Log.d(presentClass, "Se suma dato a ContentValues " + "Columna : " + arrayUpdate[i][0] + " dato : " + arrayUpdate[i][1]);
                        updateUser.put(arrayUpdate[i][0], arrayUpdate[i][1]);
                        Log.d(presentClass, "ContentValues vacio ? : " + updateUser.isEmpty());

                        if(arrayUpdate[i][1].equals("")) emptyInput = true;
                        if(emptyInput) {
                            Log.d(presentClass,"Hay un campo vacio");
                            updateUser.clear();
                            break;
                        }
                    }

                    if(emptyInput) {
                        Log.d(presentClass, "Existe un campo vacio se muestra TOAST");
                        Toast.makeText(VerItemUsuario.this,
                                "Debe completar todos los campos",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        updateUser.put("usuario_admin", checkAdmin.isChecked());
                        int updateResult = databaseWrite.update("usuario", updateUser, "id_usuario = '" + idItemUser + "'",null);
                        databaseWrite.close();

                        if(updateResult == 1) {
                            Log.d(presentClass, "UPDATE usuario id : " + idItemUser);
                            Toast.makeText(VerItemUsuario.this, "Se actualizaron los datos", Toast.LENGTH_SHORT).show();

                            inputFirstName.setText(arrayUpdate[0][1]);inputLastName.setText(arrayUpdate[1][1]);
                            inputDni.setText(arrayUpdate[2][1]); inputEmail.setText(arrayUpdate[3][1]);
                            inputStreet.setText(arrayUpdate[5][1]); inputStreetNumber.setText(arrayUpdate[6][1]);
                            inputPassword.setText(arrayUpdate[4][1]); inputRepitePassword.setText(arrayUpdate[4][1]);
                            checkAdmin.setChecked(checkAdmin.isChecked());
                        }
                    }
                }else {

                    Toast.makeText(VerItemUsuario.this, "Password y repeticion no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}