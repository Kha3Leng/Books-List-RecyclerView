package com.example.apiasy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private TextView title, author;
    private EditText query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        query = findViewById(R.id.query);
    }

    public void search(View view) {
        String qryStr = query.getText().toString();

        new FetchBookTask(title, author).execute(qryStr);
    }
}