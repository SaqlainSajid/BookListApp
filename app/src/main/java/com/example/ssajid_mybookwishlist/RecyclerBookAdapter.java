// WsCube Tech, 2022, Youtube, Recycler View in Android Studio Explained with Example | Android Recycler View Tutorial, https://www.youtube.com/watch?v=FEqF1_jDV-A
// WsCube Tech, 2022, Youtube, How to Add, Delete, and Update Items in Android RecyclerView | Android Studio Tutorial #26, https://www.youtube.com/watch?v=AUow1zsO6mg
// OpenAI, 2024, ChatGPT, user input validation
// OpenAI, 2024. ChatGPT, implement interface to update bookCount and readCount inside MainActivity

// Most code in RecyclerBookAdapter, is taken and repurposed from the above mentioned
// 2 youtube videos and occasionally using ChatGPT

package com.example.ssajid_mybookwishlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// RecylerBookAdapter does the following
// 1. creates views for each book, inside books, which is passed from MainActivity
// 2. Deletes a book on long press
// 3. Updates a book on press
public class RecyclerBookAdapter extends RecyclerView.Adapter<RecyclerBookAdapter.ViewHolder> {

    // ChatGPT was used to implement this interface
    // OpenAI, 2024. ChatGPT, implement interface to update bookCount and readCount inside MainActivity
    public interface BookUpdateListener {
        void onBookDeleted(int position, boolean wasRead);
        void onBookUpdated(int position, boolean wasRead, boolean isRead);
    }

    private BookUpdateListener updateListener;
    Context context;
    ArrayList<Book> books;
    public RecyclerBookAdapter(Context context, ArrayList<Book> books, BookUpdateListener listener) {
        this.context = context;
        this.books = books;
        this.updateListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bookRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_dialog);

                TextView header = dialog.findViewById(R.id.header);
                EditText editTitle = dialog.findViewById(R.id.title);
                EditText editAuthor = dialog.findViewById(R.id.author);
                EditText editGenre = dialog.findViewById(R.id.genre);
                EditText editPublished = dialog.findViewById(R.id.pubYear);
                CheckBox read = dialog.findViewById(R.id.readStatus);
                Button actionBtn = dialog.findViewById(R.id.actionBtn);

                header.setText("Update Book");
                actionBtn.setText("Update");

                editTitle.setText(books.get(position).getTitle());
                editAuthor.setText(books.get(position).getAuthor());
                editGenre.setText(books.get(position).getGenre());
                editPublished.setText(String.valueOf(books.get(position).getPubYear()));
                if(books.get(position).getRead()) {
                    read.setChecked(true);
                } else {
                    read.setChecked(false);
                }

                actionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ChatGPT was used to validate user input data
                        // OpenAI, 2024. ChatGPT, user input validation
                        boolean wasRead = books.get(position).getRead();

                        String title = editTitle.getText().toString().trim();
                        String author = editAuthor.getText().toString().trim();
                        String genre = editGenre.getText().toString().trim();
                        String pubYearTxt = editPublished.getText().toString().trim();

                        if (title.length() > 50) {
                            Toast.makeText(context, "Title must be at most 50 characters", Toast.LENGTH_SHORT).show();
                        } else if (author.length() > 30) {
                            Toast.makeText(context, "Author must be at most 30 characters", Toast.LENGTH_SHORT).show();
                        } else if (pubYearTxt.length() != 4) {
                            Toast.makeText(context, "Year must be a 4-digit integer", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                int pubYear = Integer.parseInt(pubYearTxt);


                                boolean readStatus = read.isChecked();
                                books.set(position, new Book(title, author, genre, pubYear, readStatus));
                                notifyItemChanged(position);


                                if (updateListener != null) {
                                    updateListener.onBookUpdated(position, wasRead, readStatus);
                                }

                                dialog.dismiss();
                            } catch (NumberFormatException e) {
                                Toast.makeText(context, "Invalid year format", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });
                dialog.show();
            }
        });

        holder.bookRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Delete Book").setMessage("Are you sure you want to delete this book?").setIcon(R.drawable.del)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (updateListener != null) {
                                    updateListener.onBookDeleted(position, books.get(position).getRead());
                                }
                                books.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.show();
                return true;
            }
        });

        holder.bookTitle.setText(books.get(position).getTitle());
        holder.author.setText(books.get(position).getAuthor());
        holder.genre.setText(books.get(position).getGenre());
        holder.pubYear.setText(String.valueOf(books.get(position).getPubYear()));

        if(books.get(position).getRead()){
            holder.status.setText("Read");
        }
        else {
            holder.status.setText("Unread");
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout bookRow;
        TextView bookTitle, author, genre, pubYear, status;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.bookTitleTxtV);
            author = itemView.findViewById(R.id.bookAuthorTxtV);
            genre   = itemView.findViewById(R.id.bookGenreTxtV);
            pubYear = itemView.findViewById(R.id.bookPubYearTxtV);
            status = itemView.findViewById(R.id.bookStatusTxtV);
            img = itemView.findViewById(R.id.imgBook);
            bookRow = itemView.findViewById(R.id.llrow);
        }

    }
}
