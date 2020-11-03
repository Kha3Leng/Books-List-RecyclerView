package com.example.AsyncTaskLoader;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchBookTask extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> title, author;

    public FetchBookTask(TextView title, TextView author) {
        this.title = new WeakReference<>(title);
        this.author = new WeakReference<>(author);
    }

    @Override
    protected String doInBackground(String... strings) {
            return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        title.get().setText();
        String titleSTR = null;
        String authorSTR = null;
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            int i=0;

            while(i < jsonArray.length() && titleSTR == null && authorSTR == null){
                JSONObject vols = jsonArray.getJSONObject(i);
                JSONObject vol = vols.getJSONObject("volumeInfo");

                titleSTR = vol.getString("title");
                authorSTR = vol.getString("authors");
                i++;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        if (titleSTR != null && authorSTR != null){
            title.get().setText(titleSTR);
            author.get().setText(authorSTR);
        }else{
            title.get().setText("No Results founds");
            author.get().setText("");
        }
    }
}
