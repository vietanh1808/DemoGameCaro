package com.example.administrator.cocaro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/12/2018.
 */

public class AdapterGridViewPvsP extends BaseAdapter{
    Context myContext;
    int myLayout;
    ArrayList<Integer> arr, rep, red;
    TextView pwin, time;
    Button btNewGame;
    ImageView px, po;
    int kt=0, pvsp_time;

    CountDownTimer timer1 = new CountDownTimer(15000, 1000) {
        @Override
        public void onTick(long l) {
            if (rep.size()%2==1) {
                time.setText("    "+l/1000 + "s -->");
                time.setTextColor(Color.parseColor("#FF0B00E2"));
            }
            else {
                time.setText("<-- "+l/1000+"s    ");
                time.setTextColor(Color.parseColor("#FFFF0004"));
            }
        }

        @Override
        public void onFinish() {
            if (rep.size()%2==0) o_win(); else x_win();
        }
    };
    CountDownTimer timer2 = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long l) {
            if (rep.size()%2==1) {
                time.setText("    "+l/1000 + "s -->");
                time.setTextColor(Color.parseColor("#FF0B00E2"));
            }
            else {
                time.setText("<-- "+l/1000+"s    ");
                time.setTextColor(Color.parseColor("#FFFF0004"));
            }
        }

        @Override
        public void onFinish() {
            if (rep.size()%2==0) o_win(); else x_win();
        }
    };
    CountDownTimer timer3 = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            if (rep.size()%2==1) {
                time.setText("    "+l/1000 + "s -->");
                time.setTextColor(Color.parseColor("#FF0B00E2"));
            }
            else {
                time.setText("<-- "+l/1000+"s    ");
                time.setTextColor(Color.parseColor("#FFFF0004"));
            }
        }

        @Override
        public void onFinish() {
            if (rep.size()%2==0) o_win(); else x_win();
        }
    };

    public AdapterGridViewPvsP(Context myContext, int myLayout, ArrayList<Integer> arr, ArrayList<Integer> rep, ArrayList<Integer> red,
                               TextView pwin, Button btNewgame, TextView time, ImageView px, ImageView po, Integer pvsp_time) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.arr = arr;
        this.rep = rep;
        this.red = red;
        this.pwin = pwin;
        this.btNewGame = btNewgame;
        this.time = time;
        this.px = px;
        this.po = po;
        this.pvsp_time = pvsp_time;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return arr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);

        final CustomTextView customTextView = view.findViewById(R.id.custom_text);
        customTextView.setBackgroundResource(R.drawable.oco);

        if (arr.get(i)==1) customTextView.setBackgroundResource(R.drawable.ocox);
        if (arr.get(i)==-1) customTextView.setBackgroundResource(R.drawable.ocoo);
        if (rep.size()>0){
            if (rep.get(rep.size()-1)==i && rep.size()%2==0) customTextView.setBackgroundResource(R.drawable.ocooo);
            if (rep.get(rep.size()-1)==i && rep.size()%2==1) customTextView.setBackgroundResource(R.drawable.ocoxx);
        }
        customTextView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (arr.get(i) == 0) {
                    if (pvsp_time!=0) timer_start();
                    red.clear();
                    kt = 0;
                    if (rep.size() % 2 == 0) px_danh();
                    else po_danh();
                    notifyDataSetChanged();
                }
            }

            private void px_danh() {
                arr.set(i, 1);
                rep.add(i);
                kt = kthang(i, 1) + ktcot(i, 1) + ktcheo1(i, 1) + ktcheo2(i, 1);
                if (kt>0) x_win();
                po.setImageResource(R.drawable.poo);
                px.setImageResource(R.drawable.px);
            }
            private void po_danh() {
                arr.set(i, -1);
                rep.add(i);
                kt = kthang(i, -1) + ktcot(i, -1) + ktcheo1(i, -1) + ktcheo2(i, -1);
                if (kt<0) o_win();
                px.setImageResource(R.drawable.pxx);
                po.setImageResource(R.drawable.po);
            }

            private int kthang(int i, int i1) {
                int vthang = i/19*19, diem = 0;
                while (vthang<i/19*19+19)  {
                    if (arr.get(vthang)==i1) diem = diem+i1;
                    else diem=0;
                    if (diem==5*i1) {
                        if (vthang % 19 == 18 || vthang % 19 == 4) return i1;
                        if (arr.get(vthang + 1) != -i1 || arr.get(vthang - 5) != -i1) return i1;
                    }

                    vthang++;
                }
                return 0;

            }
            private int ktcot(int i, int i1){
                int vtcot = i%19, diem = 0;
                while (vtcot<19*19) {
                    if (arr.get(vtcot)==i1) diem = diem+i1;
                    else diem =0;
                    if (diem==5*i1){
                        if (vtcot/19==18 || vtcot/19==4) return i1;
                        if (arr.get(vtcot+19)!=-i1 || arr.get(vtcot-19*5)!=-i1) return i1;
                    }

                    vtcot=vtcot+19;
                }
                return 0;

            }
            private int ktcheo1(int i, int i1){
                int vtcheo1=i, diem = 0;
                while (vtcheo1%19!=0 && vtcheo1/19!=0) vtcheo1=vtcheo1-20;
                do {
                    if (arr.get(vtcheo1)==i1) diem = diem + i1;
                    else diem = 0;
                    if (diem==5*i1) {
                        if (vtcheo1%19==18 || vtcheo1/19==18 || vtcheo1/19==4 || vtcheo1%19==4) return i1;
                        if (arr.get(vtcheo1+20)!=-i1 || arr.get(vtcheo1-20*5)!=-i1) return i1;
                    }
                    vtcheo1=vtcheo1+20;
                } while (vtcheo1%19!=0 && vtcheo1/19!=19);
                return 0;

            }
            private int ktcheo2(int i, int i1){
                int vtcheo2=i, diem =0;
                while (vtcheo2%19!=18 && vtcheo2/19!=0) vtcheo2=vtcheo2-18;
                do {
                    if (arr.get(vtcheo2)==i1) diem = diem + i1;
                    else diem =0;
                    if (diem == 5*i1){
                        if (vtcheo2%19==0 || vtcheo2/19==18 || vtcheo2/19==3 || vtcheo2%19==14) return i1;
                        if (arr.get(vtcheo2+18)!=-i1 || arr.get(vtcheo2-18*5)!=-i1) return i1;
                    }
                    vtcheo2=vtcheo2+18;

                }  while (vtcheo2%19!=18 && vtcheo2/19!=19);
                return 0;
            }

        });
        return view;
    }
    private void x_win(){
        timer_cancel();
        time.setText("");
        pwin.setText("X win");
        rep.add(-2);
        pwin.setTextColor(Color.parseColor("#FFFF0004"));
        btNewGame.setEnabled(true);
        for (int j=0; j<19*19; j++)
            if (arr.get(j)==0) arr.set(j, 7);
    }
    private void o_win(){
        timer_cancel();
        time.setText("");
        pwin.setText("O win");
        pwin.setTextColor(Color.parseColor("#FF0B00E2"));
        rep.add(-1);
        btNewGame.setEnabled(true);
        for (int j=0; j<19*19; j++)
            if (arr.get(j)==0) arr.set(j, 7);
    }
    private void timer_start(){
        if (pvsp_time==1) timer1.start();
        if (pvsp_time==2) timer2.start();
        if (pvsp_time==3) timer3.start();
    }
    private void timer_cancel(){
        timer1.cancel();
        timer2.cancel();
        timer3.cancel();
    }

}
