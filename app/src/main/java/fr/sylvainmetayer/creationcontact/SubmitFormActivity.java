package fr.sylvainmetayer.creationcontact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SubmitFormActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_form);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.NAME);
        String email = intent.getStringExtra(MainActivity.EMAIL);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.form_result);
        textView.setText("Hello " + name + ", we recorded your email '" + email + "'");

    }
}
