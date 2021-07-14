package com.example.administrator.cocaro;

import android.app.Dialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 3/3/2018.
 */

public class PvsPActivity extends AppCompatActivity {
    GridView grid;
    AdapterGridViewPvsP adapter;
    ArrayList<Integer> list, rep, red;
    ArrayList<String> continues;
    TextView pwin, time, sttimes;
    String st_times;
    Button btNewGame;
    ConstraintLayout constraintLayout;
    ConstraintLayout.LayoutParams layoutParams;
    ImageView px, po;
    int pvsp_time, vitri;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pvsp, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pvsp_undo:
                    if (rep.size()>1 && rep.get(rep.size()-1)>=0) {
                        list.set(rep.get(rep.size()-1), 0);
                        red.add(rep.get(rep.size()-1));
                        rep.remove(rep.size()-1);
                        adapter.notifyDataSetChanged();
                    }
                    if (rep.size()>1 && rep.get(rep.size()-1)<0) {
                        for (int i=0; i<19*19; i++)
                            if (list.get(i)==7) list.set(i, 0);
                        rep.remove(rep.size()-1);
                        list.set(rep.get(rep.size()-1), 0);
                        rep.remove(rep.size()-1);
                        btNewGame.setEnabled(false);
                        adapter.notifyDataSetChanged();
                    }
                break;
            case R.id.pvsp_redo:
                    if (red.size()>0) {
                        if (rep.size()%2==0) list.set(red.get(red.size()-1), 1);
                        else list.set(red.get(red.size()-1), -1);
                        rep.add(red.get(red.size()-1));
                        red.remove(red.size()-1);
                        adapter.notifyDataSetChanged();
                    }
                break;
            case R.id.pvsp_save:
                if (rep.size()==0) break;
                savefile();
                break;
            case R.id.pvsp_cont:
                continues();
                break;
            case R.id.pvsp_back:
                finish();
                break;
            case R.id.time_15s:
                pvsp_time = 1;
                Toast.makeText(this, "15s", 0).show();
                st_times = "Times: 15s     ";
                break;
            case R.id.time_30s:
                pvsp_time = 2;
                Toast.makeText(this, "30s", 0).show();
                st_times = "Times: 30s     ";
                break;
            case R.id.time_60s:
                pvsp_time = 3;
                Toast.makeText(this, "60s", 0).show();
                st_times = "Times: 60s     ";
                break;
            case R.id.time_infinity:
                pvsp_time = 0;
                Toast.makeText(this, "Infinity", 0).show();
                st_times = "Times: Infinity";
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void continues() {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_load);
            ListView listView = (ListView) dialog.findViewById(R.id.listView);
            final Button loadFile = (Button) dialog.findViewById(R.id.loadFile);
            Button cancelFile = (Button) dialog.findViewById(R.id.cancelFile);
            final Button delFile = (Button) dialog.findViewById(R.id.delFile);
            delFile.setEnabled(false);
            loadFile.setEnabled(false);
            final TextView textView = (TextView) dialog.findViewById(R.id.textViewFileName);

            try {
                FileInputStream fileInputStream = openFileInput("continues");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                continues.clear();
                String fname;
                do {
                    fname = bufferedReader.readLine();
                    if (fname!=null)continues.add(fname);
                } while (fname!=null);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, continues);
            listView.setAdapter(adapter1);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    textView.setText(continues.get(i));
                    delFile.setEnabled(true);
                    loadFile.setEnabled(true);
                    vitri = i;
                }
            });

            loadFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rep.clear(); pwin.setText("");
                    try {
                        FileInputStream fileInputStream = openFileInput(textView.getText().toString());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                        int size = Integer.parseInt(bufferedReader.readLine());
                        for (int j=0; j<size; j++) rep.add(Integer.parseInt(bufferedReader.readLine()));
                        fileInputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    list.clear();
                    for (int i=0; i<19*19; i++) list.add(0);
                    for (int i=0; i<rep.size(); i++)
                        if (i%2==0) list.set(rep.get(i), 1);
                        else list.set(rep.get(i), -1);
                    adapter.notifyDataSetChanged();
                    dialog.cancel();
                }
            });

            cancelFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            delFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delFile.setEnabled(false);
                    loadFile.setEnabled(false);
                    textView.setText("");
                    File file = new File(textView.getText().toString());
                    file.delete();
                    continues.remove(vitri);
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("continues", MODE_PRIVATE);
                        for (int j=0; j<continues.size(); j++)
                            fileOutputStream.write((continues.get(j)+"\n").getBytes());
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapter1.notifyDataSetChanged();
                }
            });

            dialog.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvsp);
        continues = new ArrayList<>();
        anhxa();
        khoitaobanco();

        layoutParams.height=layoutParams.width;
        btNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                px.setImageResource(R.drawable.px);
                po.setImageResource(R.drawable.poo);
                list.clear(); rep.clear(); red.clear();
                for (int i=0; i<19*19; i++) list.add(0);
                list.set(180, 1);
                rep.add(180);
                pwin.setText("");
                time.setText("");
                sttimes.setText(st_times);
                adapter = new AdapterGridViewPvsP(PvsPActivity.this, R.layout.activity_item, list, rep, red, pwin, btNewGame, time, px, po, pvsp_time);
                grid.setAdapter(adapter);
                btNewGame.setEnabled(false);
            }
        });

    }

    private void khoitaobanco() {
        list = new ArrayList<>();
        for (int i=0; i<19*19; i++) list.add(7);
        rep = new ArrayList<>();
        red = new ArrayList<>();
        st_times="Times: Infinity";
        adapter = new AdapterGridViewPvsP(PvsPActivity.this, R.layout.activity_item, list, rep, red, pwin, btNewGame, time, px, po, pvsp_time);
        grid.setAdapter(adapter);

    }

    private void anhxa() {
        grid = (GridView) findViewById(R.id.grid);
        pwin = (TextView) findViewById(R.id.pwin);
        time = (TextView) findViewById(R.id.ptime);
        sttimes = (TextView) findViewById(R.id.sttimes);
        btNewGame = (Button) findViewById(R.id.btNewGame);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constrainLayout);
        layoutParams = (ConstraintLayout.LayoutParams) constraintLayout.getLayoutParams();
        px = (ImageView) findViewById(R.id.px);
        po = (ImageView) findViewById(R.id.po);
    }

    private void savefile(){
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_save);
            final EditText filename       =   (EditText) dialog.findViewById(R.id.filename);
            Button dialogsave      =   (Button) dialog.findViewById(R.id.dialogsave);
            Button dialogcancel    =   (Button) dialog.findViewById(R.id.dialogcancel);

            dialogsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fname = filename.getText().toString();
                    String savef;
                    if (rep.get(rep.size()-1)<0) savef = "filenames"; else savef = "continues";
                        if (fname!=""){
                        try {
                            FileOutputStream fileOutputStream = openFileOutput(savef, MODE_APPEND);
                            fileOutputStream.write((fname+"\n").getBytes());
                            fileOutputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            FileOutputStream fileOutputStream = openFileOutput(fname, MODE_PRIVATE);
                            fileOutputStream.write((rep.size()+"\n").getBytes());
                            for (int j=0; j<rep.size(); j++) fileOutputStream.write((rep.get(j)+"\n").getBytes());
                            fileOutputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(PvsPActivity.this, "Saved", 0).show();
                        dialog.cancel();
                    }

                }
            });

            dialogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();

    }
}
