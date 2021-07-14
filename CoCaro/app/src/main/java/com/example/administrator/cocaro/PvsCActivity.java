package com.example.administrator.cocaro;

import android.app.Dialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 3/3/2018.
 */

public class PvsCActivity extends AppCompatActivity {

    GridView grid;
    AdapterGridViewPvsC adapter;
    ArrayList<Integer> list, rep, red;
    Button btNewGame;
    ConstraintLayout constraintLayout;
    ConstraintLayout.LayoutParams layoutParams;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pvsc, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pvsc_undo:
                if (rep.size()>1 && rep.get(rep.size()-1)>=0) {
                    list.set(rep.get(rep.size()-1), 0);
                    list.set(rep.get(rep.size()-2), 0);
                    red.add(rep.get(rep.size()-1));
                    red.add(rep.get(rep.size()-2));
                    rep.remove(rep.size()-1);
                    rep.remove(rep.size()-1);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.pvsc_redo:
                if (red.size()>0) {
                    list.set(red.get(red.size()-1), -1);
                    rep.add(red.get(red.size()-1));
                    red.remove(red.size()-1);

                    list.set(red.get(red.size()-1), 1);
                    rep.add(red.get(red.size()-1));
                    red.remove(red.size()-1);

                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.pvsc_save:
                if (rep.size()==0) break;
                if (rep.get(rep.size()-1)<0) savefile();
                break;
            case R.id.pvsc_back:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savefile() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_save);
        final EditText filename       =   (EditText) dialog.findViewById(R.id.filename);
        Button dialogsave      =   (Button) dialog.findViewById(R.id.dialogsave);
        Button dialogcancel    =   (Button) dialog.findViewById(R.id.dialogcancel);

        dialogsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = filename.getText().toString();
                if (fname!=""){
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("filenames", MODE_APPEND);
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
                    Toast.makeText(PvsCActivity.this, "Saved", 0).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvsc);

        anhxa();
        khoitaobanco();
        layoutParams.height=layoutParams.width;
        btNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear(); rep.clear(); red.clear();
                for (int i=0; i<19*19; i++) list.add(0);
                list.set(180, 1);
                rep.add(180);
                btNewGame.setEnabled(false);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void khoitaobanco() {
        list = new ArrayList<>();
        for (int i=0; i<19*19; i++) list.add(7);
        rep = new ArrayList<>();
        red = new ArrayList<>();
        adapter = new AdapterGridViewPvsC(PvsCActivity.this, R.layout.activity_item, list, rep, red, btNewGame);
        grid.setAdapter(adapter);
    }

    private void anhxa() {
        grid = (GridView) findViewById(R.id.grid);
        btNewGame = (Button) findViewById(R.id.btNewGame);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constrainLayout);
        layoutParams = (ConstraintLayout.LayoutParams) constraintLayout.getLayoutParams();
    }
}
