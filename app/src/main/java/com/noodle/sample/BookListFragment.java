package com.noodle.sample;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noodle.sample.model.Book;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@OptionsMenu(R.menu.fragment_book_list)
@EFragment(R.layout.fragment_book_list)
public class BookListFragment extends Fragment {

  @Bean
  BookManager bookManager;

  @ViewById
  RecyclerView recyclerView;
  BookListAdapter adapter;

  @AfterViews
  void afterViews() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    adapter = new BookListAdapter(bookManager.getBooks());
    recyclerView.setAdapter(adapter);

    bookManager.setListener(new BookManager.Listener() {
      @Override
      public void onBooksChanged() {
        adapter.setItems(bookManager.getBooks());
      }
    });
  }

  @OptionsItem(R.id.clear)
  void onClear() {
    new AlertDialog.Builder(getActivity())
        .setTitle("Are you sure?")
        .setMessage("All items will be removed")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(final DialogInterface dialog, final int which) {
            bookManager.clear();
          }
        })
        .setNegativeButton("No", null)
        .show();
  }

  class BookListAdapter extends RecyclerView.Adapter<BookViewHolder> {

    List<Book> bookList = new ArrayList<>();
    LayoutInflater layoutInflater = getActivity().getLayoutInflater();

    BookListAdapter(final List<Book> bookList) {
      this.bookList = bookList;
    }

    @Override
    public BookViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
      final View view = layoutInflater.inflate(R.layout.item_book, parent, false);

      return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookViewHolder holder, final int position) {
      holder.bookTitle.setText(bookList.get(position).title);
      holder.bookAuthor.setText(bookList.get(position).authorName);
    }

    @Override
    public int getItemCount() {
      return bookList.size();
    }

    void setItems(final List<Book> items) {
      this.bookList = new ArrayList<>(items);
      notifyDataSetChanged();
    }
  }

  class BookViewHolder extends RecyclerView.ViewHolder {

    TextView bookTitle;
    TextView bookAuthor;

    BookViewHolder(final View itemView) {
      super(itemView);
      bookTitle = ((TextView) itemView.findViewById(R.id.bookTitle));
      bookAuthor = ((TextView) itemView.findViewById(R.id.bookAuthor));
    }

  }
}
