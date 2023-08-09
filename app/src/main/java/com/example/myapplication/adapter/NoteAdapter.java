package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.NoteItemBinding;
import com.example.myapplication.entity.Notes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NoteAdapter extends FirebaseRecyclerAdapter<Notes, NoteAdapter.NoteViewHolder> {

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Notes item, String noteKey);
    }

    public NoteAdapter(FirebaseRecyclerOptions<Notes> options, OnItemClickListener listener) {
        super(options);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindViewHolder(NoteViewHolder holder, int position, Notes model) {
        holder.bind(model);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NoteItemBinding binding = NoteItemBinding.inflate(inflater, parent, false);
        return new NoteViewHolder(binding);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final NoteItemBinding binding;

        public NoteViewHolder(NoteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(getItem(position), getRef(position).getKey()); // Pass the noteKey to the click listener
                }
            });
        }

        public void bind(Notes item) {
            binding.tvTitleItem.setText(item.getTitle());
            binding.tvCategoryItem.setText(item.getCategory());
            binding.tvDateItem.setText(item.getDate());
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        notifyDataSetChanged();
    }
}
