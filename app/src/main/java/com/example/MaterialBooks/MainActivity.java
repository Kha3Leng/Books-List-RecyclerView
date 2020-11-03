package com.example.MaterialBooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private TextView title, author;
    private EditText query;
    private ArrayList<Books> books;
    private RecyclerView recyclerView;
    private BooksAdapter adapter;

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        query = findViewById(R.id.query);
        recyclerView = findViewById(R.id.recyclerView);
        setTitle("Search Book On GOogle Book");
        books = new ArrayList<>();


//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BooksAdapter(books, this);
        recyclerView.setAdapter(adapter);


        if (getLoaderManager().getLoader(1) != null)
            getLoaderManager().initLoader(1, null, this);

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
            Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show();
        } else {
            if (qryStr.length() == 0) {
                Toast.makeText(this, R.string.query, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
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
        int i = 0;

        books.clear();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            i = 0;
            while (i < jsonArray.length()) {
                JSONObject vols = jsonArray.getJSONObject(i);
                JSONObject vol = vols.getJSONObject("volumeInfo");

                titleSTR = vol.getString("title");
                authorSTR = vol.getJSONArray("authors").getString(0);
                books.add(new Books(authorSTR, titleSTR));
                adapter.notifyItemInserted(i);
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (i>0) {
            Toast.makeText(this, i+" Results Found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}