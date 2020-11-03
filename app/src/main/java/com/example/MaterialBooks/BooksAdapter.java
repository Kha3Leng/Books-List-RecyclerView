package com.example.MaterialBooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    private ArrayList<Books> booksList;

    public BooksAdapter(ArrayList<Books> booksList, Context mContext) {
        this.booksList = booksList;
        this.mContext = mContext;
    }

    private Context mContext;
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(mContext).inflate(R.layout.book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Books book = booksList.get(position);
        holder.BindTo(book);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.author = itemView.findViewById(R.id.author);
        }

        public void BindTo(Books book){
            title.setText(book.getTitle().toString());
            author.setText(book.getAuthor().toString());
        }
    }
}
