// WsCube Tech, 2022, Youtube, Recycler View in Android Studio Explained with Example | Android Recycler View Tutorial, https://www.youtube.com/watch?v=FEqF1_jDV-A
// WsCube Tech, 2022, Youtube, How to Add, Delete, and Update Items in Android RecyclerView | Android Studio Tutorial #26, https://www.youtube.com/watch?v=AUow1zsO6mg
// OpenAI, 2024, ChatGPT, user input validation
// OpenAI, 2024. ChatGPT, implement interface methods to update bookCount and readCount

// Most code in RecyclerBookAdapter, is taken and repurposed from the above mentioned
// 2 youtube videos and occasionally using ChatGPT

package com.example.ssajid_mybookwishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


// MainActivity does the following -
// 1. Show and update total number of books
// 2. Show and update total number of books read
// 3. Show a List of books
// 4. Add a book to the List
// updating a book and deleting a book are handled inside the RecyclerViewAdapter
// in order to update the count of books, an interface inside RecyclerViewAdapter is defined
// the interface has 2 methods that update the count of books and the count of books read
// the context is passed to RecyclerViewAdapter from MainActivity, and RecyclerViewAdapter deletes
// or updates a book by calling the interface methods
// the interface methods are implemented below onCreate()
public class MainActivity extends AppCompatActivity implements RecyclerBookAdapter.BookUpdateListener {
    private ArrayList<Book> books = new ArrayList<>();
    private RecyclerBookAdapter adapter;

    private TextView bookCountView, readCountView;

    private int bookCount = 0;
    private int readCount = 0;

    private EditText editTitle, editAuthor, editGenre, editPublished;
    private CheckBox read;
    private Button actionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookCountView = findViewById(R.id.bookCount);
        readCountView = findViewById(R.id.readCount);

        bookCountView.setText("Total Books: "+bookCount);
        readCountView.setText("Books Read: "+readCount);


        RecyclerView rView = findViewById(R.id.recylcerBook);
        rView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton btnOpenDialog = findViewById(R.id.btnOpenDialog);

        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_dialog);

                editTitle = dialog.findViewById(R.id.title);
                editAuthor = dialog.findViewById(R.id.author);
                editGenre = dialog.findViewById(R.id.genre);
                editPublished = dialog.findViewById(R.id.pubYear);
                read = dialog.findViewById(R.id.readStatus);
                actionBtn = dialog.findViewById(R.id.actionBtn);

                //for validating user input, code from chatGPT was used
                // OpenAI, 2024, ChatGPT, user input validation
                actionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = "";
                        String author = "";
                        String genre = "";
                        String pubYearTxt = "";
                        Integer pubYear = 0;
                        Boolean readStatus = false;

                        title = editTitle.getText().toString().trim();
                        author = editAuthor.getText().toString().trim();
                        genre = editGenre.getText().toString().trim();
                        pubYearTxt = editPublished.getText().toString().trim();

                        if (title.length() > 50) {
                            Toast.makeText(MainActivity.this, "Title must be at most 50 characters", Toast.LENGTH_SHORT).show();
                        } else if (author.length() > 30) {
                            Toast.makeText(MainActivity.this, "Author must be at most 30 characters", Toast.LENGTH_SHORT).show();
                        } else if (pubYearTxt.length() != 4) {
                            Toast.makeText(MainActivity.this, "Year must be a 4-digit integer", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                pubYear = Integer.parseInt(pubYearTxt);


                                readStatus = read.isChecked();
                                Book newBook = new Book(title, author, genre, pubYear, readStatus);
                                books.add(newBook);


                                bookCount++;

                                if (newBook.getRead()) {
                                    readCount++;
                                }

                                bookCountView.setText("Total Books: " + bookCount);
                                readCountView.setText("Books Read: " + readCount);

                                adapter.notifyItemInserted(books.size() - 1);
                                rView.scrollToPosition(books.size() - 1);

                                dialog.dismiss();
                            } catch (NumberFormatException e) {
                                Toast.makeText(MainActivity.this, "Invalid year format", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                dialog.show();
            }
        });

        adapter = new RecyclerBookAdapter(this, books, this);
        rView.setAdapter(adapter);

    }

    //the following two methods are declared in BookUpdateListener inside RecyclerBookAdapter
    //BookUpdateListener is the interface that is used to communicate between
    //MainActivity and RecyclerBookAdapter in order to update bookCount and readCount
    //this part of the code is taken from ChatGPT and repurposed
    @Override
    public void onBookDeleted(int position, boolean wasRead) {
        if (bookCount >= 1) {
            bookCount--;
        } else {
            bookCount = 0;
        }

        if (wasRead) {
            if(readCount > 0) {
                readCount--;
            }
        }

        updateCounts();
    }

    @Override
    public void onBookUpdated(int position,boolean wasRead, boolean isRead) {
        if (wasRead && !isRead) {
            if (readCount > 0) {
                readCount--;
            } else {
                readCount = 0;
            }
        } else if (!wasRead && isRead) {
            readCount++;
        }

        updateCounts();
    }

    //this method is for showing bookCount and readCount on screen
    private void updateCounts() {
        bookCountView.setText("Total Books: " + bookCount);
        readCountView.setText("Books Read: " + readCount);
    }
}