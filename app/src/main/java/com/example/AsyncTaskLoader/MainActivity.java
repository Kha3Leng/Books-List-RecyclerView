package com.example.AsyncTaskLoader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private TextView title, author;
    private EditText query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        query = findViewById(R.id.query);

        if(getLoaderManager().getLoader(1) != null)
            getLoaderManager().initLoader(1,null, this);

    }

    public void search(View view) {
        String qryStr = query.getText().toString();
        Bundle queryBundle = new Bundle();
        queryBundle.putString("query", qryStr);

        InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethod != null) {
            inputMethod.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }


        ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = null;

        if (connMngr != null) {
            network = connMngr.getActiveNetworkInfo();
        }

        if (network != null && qryStr.length() != 0) {
            getLoaderManager().restartLoader(1, queryBundle, this);
            title.setText(R.string.loading);
            author.setText("");
        } else {
            if (qryStr.length() == 0) {
                title.setText(R.string.query);
                author.setText("");
            } else {
                title.setText(R.string.no_internet);
                author.setText("");
            }
        }


    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        String query = null;
        if (args != null)
            query = args.getString("query");
        return new BookLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        String titleSTR = null;
        String authorSTR = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            int i = 0;

            while (i < jsonArray.length() && titleSTR == null && authorSTR == null) {
                JSONObject vols = jsonArray.getJSONObject(i);
                JSONObject vol = vols.getJSONObject("volumeInfo");

                titleSTR = vol.getString("title");
                authorSTR = vol.getString("authors");
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (titleSTR != null && authorSTR != null) {
            title.setText(titleSTR);
            author.setText(authorSTR);
        } else {
            title.setText("No Results founds");
            author.setText("");
        }
    }


    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}