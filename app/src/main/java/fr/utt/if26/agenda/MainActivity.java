package fr.utt.if26.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {


    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);
        MaterialButton Login_btn = findViewById(R.id.Login_btn);


        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("Admin") && password.getText().toString().equals("Admin")){
                    Intent startConnexion = new Intent(getApplicationContext(), AcceuilActivity.class);
                    startActivity(startConnexion);
                    Toast.makeText(MainActivity.this, "Bienvenu Madame/Monsieur", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Mots de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
