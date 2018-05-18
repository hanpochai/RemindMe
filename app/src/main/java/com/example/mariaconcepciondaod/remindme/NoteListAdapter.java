package com.example.mariaconcepciondaod.remindme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<note> notelist;

    public NoteListAdapter(Context context, int layout, ArrayList<note> notelist) {
        this.context = context;
        this.layout = layout;
        this.notelist = notelist;
    }

    @Override
    public int getCount() {
        return notelist.size();
    }

    @Override
    public Object getItem(int position) {
        return notelist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle, txtdesc;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtTitle = row.findViewById(R.id.notetitle);
            holder.txtdesc = row.findViewById(R.id.notedesc);
            holder.imageView = row.findViewById(R.id.noteimage);
            row.setTag(holder);


        } else {
            holder = (ViewHolder) row.getTag();

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
                holder.imageView.setImageResource(R.drawable.lockedfile);
            }


        } catch (Exception e) {

        } return row;

    }

}
