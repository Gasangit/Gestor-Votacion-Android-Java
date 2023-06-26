package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView inputUser;
    private TextView inputPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private HelperDatabase SqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUser = findViewById(R.id.loginInputUser);
        inputPassword = findViewById(R.id.loginInputPassword);
        buttonLogin = findViewById(R.id.loginButtonLogin);
        buttonRegister = findViewById(R.id.loginButtonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = inputUser.getText().toString();
                String password = inputUser.getText().toString();

                SqliteHelper = new HelperDatabase(MainActivity.this, "dbAppVotes",null, 1);
                Log.d(this.getClass().getSimpleName(), "Instanciado objeto de la clase : " + SqliteHelper.getClass().getSimpleName());

                SQLiteDatabase Database = SqliteHelper.getReadableDatabase();

                Cursor actualQuery = Database.rawQuery(
                        "SELECT id_usuario, usuario_mail, usuario_password " +
                                "FROM usuario WHERE usuario_mail = '" + inputUser.getText().toString() +
                                "' AND usuario_password = '" + inputPassword.getText().toString() +"';", null
                );

                if(actualQuery.moveToFirst()) {

                    Log.d(this.getClass().getSimpleName(), "USUARIO : " + actualQuery.getString(1) + " CONTRASEÑA : " + actualQuery.getString(2));

                    Bundle bundleUser = new Bundle();
                    bundleUser.putString("idUser", actualQuery.getString(0));

                    Intent goToStart = new Intent(MainActivity.this, Inicio.class);
                    goToStart.putExtras(bundleUser);
                    inputUser.setText("");
                    inputPassword.setText("");
                    startActivity(goToStart);
                }
                else {
                    Log.d(this.getClass().getSimpleName(),
                            "Login incorrecto. USUARIO : " + inputUser.getText().toString() +
                                " CONTRASEÑA : " + inputPassword.getText().toString());
                    Toast.makeText(MainActivity.this,"Los datos ingresados no son correctos",Toast.LENGTH_SHORT).show();
                }

                Database.close();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToRegister = new Intent(MainActivity.this, Registro.class);
                startActivity(goToRegister);
            }
        });
    }
}