package com.example.mariaconcepciondaod.remindme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class LockNotelistAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<note> notelist;

    public LockNotelistAdapter(Context context, int layout, ArrayList<note> notelist) {
        this.context = context;
        this.layout = layout;
        this.notelist = notelist;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle, txtdesc;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
       LockNotelistAdapter.ViewHolder holder = new LockNotelistAdapter.ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtTitle = row.findViewById(R.id.notetitle);
            holder.txtdesc = row.findViewById(R.id.notedesc);
            holder.imageView = row.findViewById(R.id.noteimage);
            row.setTag(holder);


        } else {
            holder = ( LockNotelistAdapter.ViewHolder) row.getTag();

        }
        try {
            note Note = notelist.get(position);
            holder.txtTitle.setText(Note.getTitle());
            holder.txtdesc.setText(Note.getDesc());

            byte[] noteImage = Note.getImage();

            Bitmap bitmap = BitmapFactory.decodeByteArray(noteImage, 0, noteImage.length);

            String types = Note.getType();
            if (types.equals("Unlocked")) {
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_action_lockeddd);
            }


        } catch (Exception e) {

        } return row;

    }

}
