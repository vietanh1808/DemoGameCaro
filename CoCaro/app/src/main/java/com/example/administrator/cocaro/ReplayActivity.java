package com.example.administrator.cocaro;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
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
import java.util.ArrayList;

/**
 * Created by Administrator on 3/14/2018.
 */


public class ReplayActivity extends AppCompatActivity {

    ImageButton play, next, prev, ff, rew;
    ConstraintLayout.LayoutParams layoutParams;
    ConstraintLayout constraintLayout;
    GridView grid;
    AdapterGridViewReplay adapter;
    ArrayList<Integer> list, rep;
    ArrayList<String> filenames;
    int vitri, vt;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_replay, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.back) finish();
        if (item.getItemId()==R.id.load) loadfile();
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        Anhxa();
        Khoitao();

        play.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                loadfile();
                return false;
            }
        });

        rew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vt=1;
                laplai(vt);
            }
        });
        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vt = rep.size()-1;
                laplai(vt);
                if (rep.get(rep.size()-1)==-2) Toast.makeText(ReplayActivity.this, "X win", 0).show();
                else Toast.makeText(ReplayActivity.this, "O win", 0).show();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vt>1) {
                    vt--;
                    laplai(vt);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vt<rep.size()-1) {
                    vt++;
                    laplai(vt);
                    if (vt==rep.size()-1)
                        if (rep.get(rep.size()-1)==-2) Toast.makeText(ReplayActivity.this, "X win", 0).show();
                        else Toast.makeText(ReplayActivity.this, "O win", 0).show();
                }
                else {
                    if (rep.get(rep.size()-1)==-2) Toast.makeText(ReplayActivity.this, "X win", 0).show();
                    else Toast.makeText(ReplayActivity.this, "O win", 0).show();
                }
            }
        });

    }

    private void laplai(int vt){
        list.clear();
        for (int i=0; i<19*19; i++) list.add(0);
        for (int i=0; i<vt; i++)
            if (i%2==0) list.set(rep.get(i), 1); else list.set(rep.get(i), -1);
        adapter.notifyDataSetChanged();
    }

    private void Khoitao() {
        next.setEnabled(false);
        prev.setEnabled(false);
        ff.setEnabled(false);
        rew.setEnabled(false);
        list = new ArrayList<>(); rep = new ArrayList<>(); filenames = new ArrayList<>();
        for (int i=0; i<19*19; i++) list.add(0);
        adapter = new AdapterGridViewReplay(ReplayActivity.this, R.layout.activity_item, list);
        grid.setAdapter(adapter);

    }

    private void Anhxa(){
        play = (ImageButton) findViewById(R.id.play);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        ff   = (ImageButton) findViewById(R.id.ff);
        rew  = (ImageButton) findViewById(R.id.rew);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constrainLayout);
        layoutParams = (ConstraintLayout.LayoutParams) constraintLayout.getLayoutParams();
        grid = (GridView) findViewById(R.id.grid);
    }

    private void loadfile() {

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
            FileInputStream fileInputStream = openFileInput("filenames");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            filenames.clear();
            String fname;
            do {
                fname = bufferedReader.readLine();
                if (fname!=null)filenames.add(fname);
            } while (fname!=null);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, filenames);
        listView.setAdapter(adapter1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textView.setText(filenames.get(i));
                delFile.setEnabled(true);
                loadFile.setEnabled(true);
                vitri = i;
            }
        });

        loadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rep.clear();
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

                next.setEnabled(true);
                prev.setEnabled(true);
                ff.setEnabled(true);
                rew.setEnabled(true);
                vt=1;
                laplai(vt);

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
                filenames.remove(vitri);
                try {
                    FileOutputStream fileOutputStream = openFileOutput("filenames", MODE_PRIVATE);
                    for (int j=0; j<filenames.size(); j++)
                        fileOutputStream.write((filenames.get(j)+"\n").getBytes());
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
}
