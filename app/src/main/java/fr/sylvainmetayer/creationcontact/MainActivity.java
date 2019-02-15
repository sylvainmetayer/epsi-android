package fr.sylvainmetayer.creationcontact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import moe.feng.support.biometricprompt.BiometricPromptCompat;

public class MainActivity extends AppCompatActivity {

    public static final String EMAIL = "fr.sylvainmetayer.creationcontact.EMAIL";
    public static final String NAME = "fr.sylvainmetayer.creationcontact.NAME";

    public static final String filename = "name_email";

    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);

        final BiometricPromptCompat biometricPrompt =
                new BiometricPromptCompat.Builder(MainActivity.this)
                        .setTitle("Authentification requise")
                        .setDescription("Vous devez être authentifié pour pouvoir soumettre le formulaire")
                        .build();

        final CancellationSignal cancellationSignal = new CancellationSignal();

        cancellationSignal.setOnCancelListener(() -> Toast.makeText(
                MainActivity.this, "Vous avez annulé", Toast.LENGTH_SHORT).show());

        fab.setOnClickListener(view -> biometricPrompt.authenticate(cancellationSignal, new BiometricPromptCompat.IAuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @Nullable CharSequence errString) {
                Toast.makeText(MainActivity.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationHelp(int helpCode, @Nullable CharSequence helpString) {
                Toast.makeText(MainActivity.this, "HELP", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPromptCompat.IAuthenticationResult result) {
                sendForm(view);
                Toast.makeText(MainActivity.this, "Vous êtes authentifié avec succès !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(MainActivity.this, "L'identification a échoué !", Toast.LENGTH_SHORT).show();

            }
        }));

    }

    public void sendForm(View view) {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String fileContents = name + " - " + email + "\n";

        try {
            FileOutputStream outputStream;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                FileInputStream inputStream = openFileInput(MainActivity.filename);
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString;
                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found");
            }

            stringBuilder.append(fileContents);
            outputStream = openFileOutput(MainActivity.filename, Context.MODE_PRIVATE);
            outputStream.write(stringBuilder.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, SubmitFormActivity.class);

        intent.putExtra(EMAIL, email);
        intent.putExtra(NAME, name);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
