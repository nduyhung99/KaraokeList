package com.example.karaoke;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.adapter.MusicAdapter;
import com.example.model.Music;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvBaiHatGoc;
    ArrayList<Music>dsBaiHatGoc;
    MusicAdapter adapterBaiHatGoc;

    ListView lvBaiHatYeuThich;
    ArrayList<Music>dsBaiHatYeuThich;
    MusicAdapter adapterBaiHatYeuThich;

    TabHost tabHost;

    public static String DATABASE_NAME= "Arirang.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();
        addControls();
        addEvents();
        xuLyHienThiBaiHatGoc();
    }
    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists())
        {
            try
            {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Sao chép CSDL vào hệ thống thành công!", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput=getAssets().open(DATABASE_NAME);
            String outFileName=layDuonDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()){
                f.mkdir();
            }
            OutputStream myOutput= new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }catch (Exception ex){
            Log.e("Lỗi sao chép:",ex.toString());
        }
    }

    private String layDuonDanLuuTru(){
        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }
    private void addEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equalsIgnoreCase("t2")){
                    xuLyHienThiBaiHatYeuThich();
                }else if (tabId.equalsIgnoreCase("t1")){
                    xuLyHienThiBaiHatGoc();
                }
            }
        });
    }

    private void xuLyHienThiBaiHatGoc() {
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("ArirangSongList",null,null,null,null,null,null);
        dsBaiHatGoc.clear();
        while (cursor.moveToNext()){
            String mabh=cursor.getString(0);
            String tenbh=cursor.getString(1);
            String casi=cursor.getString(3);
            int yeuthich=cursor.getInt(5);
            Music music=new Music();
            music.setMa(mabh);
            music.setTen(tenbh);
            music.setCaSi(casi);
            music.setThich(yeuthich==1);
            dsBaiHatGoc.add(music);

        }
        cursor.close();
        adapterBaiHatGoc.notifyDataSetChanged();
    }

    private void xuLyHienThiBaiHatYeuThich() {
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("ArirangSonglist",null,
                "YEUTHICH=?",new String[]{"1"},null,null,null);
        dsBaiHatYeuThich.clear();
        while (cursor.moveToNext()){
            String mabh=cursor.getString(0);
            String tenbh=cursor.getString(1);
            String casi=cursor.getString(3);
            int yeuthich=cursor.getInt(5);
            Music music=new Music();
            music.setMa(mabh);
            music.setTen(tenbh);
            music.setCaSi(casi);
            music.setThich(yeuthich==1);
            dsBaiHatYeuThich.add(music);

        }
        cursor.close();
        adapterBaiHatYeuThich.notifyDataSetChanged();
    }

    private void addControls() {

        tabHost=findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1=tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("",getResources().getDrawable(R.drawable.music));
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2=tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("",getResources().getDrawable(R.drawable.musicfavorite));
        tabHost.addTab(tab2);

        lvBaiHatGoc=findViewById(R.id.lvBaiHatGoc);
        dsBaiHatGoc=new ArrayList<>();
        adapterBaiHatGoc= new MusicAdapter(MainActivity.this,R.layout.item,dsBaiHatGoc);
        lvBaiHatGoc.setAdapter(adapterBaiHatGoc);

        lvBaiHatYeuThich=findViewById(R.id.lvBaiHatYeuThich);
        dsBaiHatYeuThich=new ArrayList<>();
        adapterBaiHatYeuThich= new MusicAdapter(MainActivity.this,R.layout.item,dsBaiHatYeuThich);
        lvBaiHatYeuThich.setAdapter(adapterBaiHatYeuThich);
        
        //giaLapBaiHat();
    }

//    private void giaLapBaiHat() {
//        dsBaiHatGoc.add(new Music("bh1","Bài hát 1","Ca sĩ 1",false));
//        dsBaiHatGoc.add(new Music("bh2","Bài hát 2","Ca sĩ 2",false));
//        dsBaiHatGoc.add(new Music("bh3","Bài hát 3","Ca sĩ 3",false));
//        adapterBaiHatGoc.notifyDataSetChanged();
//    }
}