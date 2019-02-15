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

import moe.feng.support.biometricprompt.BiometricPromptCompat;

public class MainActivity extends AppCompatActivity implements BiometricPromptCompat.IAuthenticationCallback {

    public static final String EMAIL = "fr.sylvainmetayer.creationcontact.EMAIL";
    public static final String NAME = "fr.sylvainmetayer.creationcontact.NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        final BiometricPromptCompat biometricPrompt =
                new BiometricPromptCompat.Builder(MainActivity.this)
                        .setTitle("Title")
                        .setSubtitle("Subtitle")
                        .setDescription("Description: blablablablablablablablablablabla...")
                        .setNegativeButton("Use password", (dialog, which) -> Toast.makeText(
                                MainActivity.this,
                                "You requested password.",
                                Toast.LENGTH_LONG).show())
                        .build();

        final CancellationSignal cancellationSignal = new CancellationSignal();

        cancellationSignal.setOnCancelListener(() -> Toast.makeText(
                MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show());
        biometricPrompt.authenticate(cancellationSignal, this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendForm(view);
            }
        });
    }

    public void sendForm(View view) {
        Intent intent = new Intent(this, SubmitFormActivity.class);
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
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


    @Override
    public void onAuthenticationError(int errorCode, @Nullable CharSequence errString) {
        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, @Nullable CharSequence helpString) {
        Toast.makeText(MainActivity.this, "help", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(
            @NonNull BiometricPromptCompat.IAuthenticationResult result) {
        Toast.makeText(MainActivity.this, "succeed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
    }
}
