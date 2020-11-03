package com.example.apiasy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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

        InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethod != null){
            inputMethod.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }



        ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = null;

        if (connMngr != null){
            network = connMngr.getActiveNetworkInfo();
        }

        if (network != null && qryStr.length() != 0){
            new FetchBookTask(title, author).execute(qryStr);
            title.setText(R.string.loading);
            author.setText("");
        }else {
            if (qryStr.length() == 0){
                title.setText(R.string.query);
                author.setText("");
            }else{
                title.setText(R.string.no_internet);
                author.setText("");
            }
        }


    }
}