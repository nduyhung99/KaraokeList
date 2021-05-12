package com.example.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.karaoke.MainActivity;
import com.example.karaoke.R;
import com.example.model.Music;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {
    Activity context;
    int resource;
    List<Music> objects;
    public MusicAdapter(@NonNull Activity context, int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);
        TextView txtMa=row.findViewById(R.id.txtMa);
        TextView txtTen=row.findViewById(R.id.txtTen);
        TextView txtCasi=row.findViewById(R.id.txtCasi);
        ImageButton btnLike=row.findViewById(R.id.btnLike);
        ImageButton btnDislike=row.findViewById(R.id.btnDislike);

        Music music=this.objects.get(position);
        txtMa.setText(music.getMa());
        txtTen.setText(music.getTen());
        txtCasi.setText(music.getCaSi());

        if (music.isThich()){
            btnLike.setVisibility(View.INVISIBLE);
            btnDislike.setVisibility(View.VISIBLE);
        }else{
            btnLike.setVisibility(View.VISIBLE);
            btnDislike.setVisibility(View.VISIBLE);
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThich(music);
            }
        });
        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyBoThich(music);
            }
        });
        return row;
    }

    private void xuLyBoThich(Music music) {

        ContentValues row=new ContentValues();
        row.put("YEUTHICH",0);
        MainActivity.database.update("ArirangSongList",row,"MABH=?",new String[]{music.getMa()});
    }

    private void xuLyThich(Music music)
    {
        ContentValues row=new ContentValues();
        row.put("YEUTHICH",1);
        MainActivity.database.update("ArirangSongList",row,"MABH=?",new String[]{music.getMa()});
    }
}
