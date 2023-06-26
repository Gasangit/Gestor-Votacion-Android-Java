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
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    private final String presentClass = this.getClass().getSimpleName();
    private TextView inputFirstName, inputLastName, inputDni, inputEmail, inputPassword,
    inputPasswordRepite, inputStreet, inputNumber;
    private Button buttonRegister, buttonReturn;
    private HelperDatabase SqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        buttonReturn = findViewById(R.id.registerButtonReturn);
        inputFirstName = findViewById(R.id.registerInputFirstName);
        inputLastName = findViewById(R.id.registerInputLastName);
        inputDni = findViewById(R.id.registerInputDni);
        inputEmail = findViewById(R.id.registerInputEmail);
        inputPassword = findViewById(R.id.registerInputPassword);
        inputPasswordRepite = findViewById(R.id.registerInputPasswordRepite);
        inputStreet = findViewById(R.id.registerInputStreet);
        inputNumber = findViewById(R.id.registerInputNumber);
        buttonRegister = findViewById(R.id.registerButtonRegister);
        Log.d(this.getClass().getSimpleName(), "Se instanciaron los elementos de la VISTA (inputs y button)");

        SqliteHelper = new HelperDatabase(Registro.this, "dbAppVotes",null,1);
        Log.d(this.getClass().getSimpleName(), "Se instanció el HELPER");

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoLogin = new Intent(Registro.this, MainActivity.class);
                startActivity(gotoLogin);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Log.d(presentClass, "Se accionó botón de registro");
                SQLiteDatabase databaseRead = SqliteHelper.getReadableDatabase();

                Cursor mailVerification = databaseRead.rawQuery(
                        "SELECT usuario_mail" +
                            " FROM usuario " +
                            "WHERE usuario_mail = '" +
                            inputEmail.getText().toString() + "'", null);

                if(mailVerification.moveToFirst()) {
                    Toast.makeText(
                            Registro.this,
                            "El mail que acaba de ingresar se encuentra" +
                                " en uso intente loguearse si el mismo le pertenece", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase database = SqliteHelper.getWritableDatabase();
                    Log.d(presentClass, "Se instanció la base de datos");

                    ContentValues ColumnsValues = new ContentValues();
                    Log.d(this.getClass().getSimpleName(), "Se instanció el contendor de valores a ingresar en la base de datos");

                    if(inputPassword.getText().toString().equals(inputPasswordRepite.getText().toString())) {
                        Log.d(presentClass, "Password y repetición coinciden");

                        ColumnsValues.put("usuario_dni", inputDni.getText().toString());
                        ColumnsValues.put("usuario_mail", inputEmail.getText().toString());
                        ColumnsValues.put("usuario_password", inputPassword.getText().toString());
                        ColumnsValues.put("usuario_nombre", inputFirstName.getText().toString());
                        ColumnsValues.put("usuario_apellido", inputLastName.getText().toString());
                        ColumnsValues.put("usuario_calle", inputStreet.getText().toString());
                        ColumnsValues.put("usuario_altura", inputNumber.getText().toString());
                        ColumnsValues.put("usuario_admin", false);

                        database.insert("usuario", null, ColumnsValues);
                        Log.d(presentClass, "Se realizó insert a tabla usuario");

                        database.close();
                        Log.d(presentClass, "Base de datos cerrada");

                        inputFirstName.setText("");
                        inputLastName.setText("");
                        inputDni.setText("");
                        inputEmail.setText("");
                        inputPassword.setText("");
                        inputPasswordRepite.setText("");
                        inputStreet.setText("");
                        inputNumber.setText("");

                        Intent goToLogin = new Intent(Registro.this, MainActivity.class);
                        startActivity(goToLogin);
                    }else {

                        Log.d(presentClass, "No coinciden password y repetición");
                        Toast.makeText(Registro.this, "Password y repetición no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}