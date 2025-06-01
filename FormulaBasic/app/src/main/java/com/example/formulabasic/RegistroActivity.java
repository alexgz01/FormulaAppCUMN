package com.example.formulabasic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextEmailRegistro, editTextPasswordRegistro, editTextConfirmarPasswordRegistro;
    private Button buttonRegistrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editTextEmailRegistro = findViewById(R.id.editTextEmailRegistro);
        editTextPasswordRegistro = findViewById(R.id.editTextPasswordRegistro);
        editTextConfirmarPasswordRegistro = findViewById(R.id.editTextConfirmarPasswordRegistro);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        mAuth = FirebaseAuth.getInstance();

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailRegistro.getText().toString().trim();
                String password = editTextPasswordRegistro.getText().toString().trim();
                String confirmarPassword = editTextConfirmarPasswordRegistro.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    editTextEmailRegistro.setError("Se requiere el correo electrónico.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPasswordRegistro.setError("Se requiere la contraseña.");
                    return;
                }

                if (TextUtils.isEmpty(confirmarPassword)) {
                    editTextConfirmarPasswordRegistro.setError("Se requiere confirmar la contraseña.");
                    return;
                }

                if (!password.equals(confirmarPassword)) {
                    editTextConfirmarPasswordRegistro.setError("Las contraseñas no coinciden.");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registro exitoso
                                    Toast.makeText(RegistroActivity.this, "Registro exitoso.",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegistroActivity.this, "Error al registrar: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

