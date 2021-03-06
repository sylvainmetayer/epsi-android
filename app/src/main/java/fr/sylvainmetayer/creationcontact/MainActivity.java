package fr.sylvainmetayer.creationcontact;

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

import java.util.List;

import moe.feng.support.biometricprompt.BiometricPromptCompat;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton fab;
    public FloatingActionButton delete;
    public FloatingActionButton findBy;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = AppDatabase.getDb(this.getApplicationContext());

        fab = findViewById(R.id.fab);
        delete = findViewById(R.id.delete);
        findBy = findViewById(R.id.find_by);

        final BiometricPromptCompat biometricPrompt =
                new BiometricPromptCompat.Builder(MainActivity.this)
                        .setTitle("Authentification requise")
                        .setDescription("Vous devez être authentifié pour pouvoir soumettre le formulaire")
                        .build();

        final CancellationSignal cancellationSignal = new CancellationSignal();

        cancellationSignal.setOnCancelListener(() -> Toast.makeText(
                MainActivity.this, "Vous avez annulé", Toast.LENGTH_SHORT).show());

        delete.setOnClickListener(view -> {
            List<User> userList = this.db.userDao().getAll();

            for (User user : userList) {
                this.db.userDao().delete(user);
            }

            Toast.makeText(getBaseContext(), "Toutes les données ont été effacées !", Toast.LENGTH_LONG)
                    .show();

        });

        findBy.setOnClickListener(view -> {
            String email = ((EditText) findViewById(R.id.email)).getText().toString();
            String name = ((EditText) findViewById(R.id.name)).getText().toString();
            List<User> userList = this.db.userDao().findByNameOrEmail(name, email);


            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder
                    .append("Résultats : ")
                    .append("\n");

            if (userList.size() <= 0) {
                stringBuilder.append("Aucun");
            }

            for (User user : userList) {
                stringBuilder
                        .append(user.name)
                        .append(" - ")
                        .append(user.email)
                        .append("\n");
            }

            Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
        });

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

        User user = new User();
        user.email = email;
        user.name = name;

        this.db.userDao().insert(user);

        Intent intent = new Intent(this, SubmitFormActivity.class);
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
