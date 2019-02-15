package fr.sylvainmetayer.creationcontact;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

public class SubmitFormActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_form);

        AppDatabase db = AppDatabase.getDb(this.getApplicationContext());

        List<User> userList = db.userDao().getAll();
        StringBuilder stringBuilder = new StringBuilder();

        for (User user : userList) {
            stringBuilder
                    .append(user.email)
                    .append(" - ")
                    .append(user.name)
                    .append("\n");
        }

        TextView textView = findViewById(R.id.form_result);
        textView.setText(stringBuilder.toString());

    }
}
