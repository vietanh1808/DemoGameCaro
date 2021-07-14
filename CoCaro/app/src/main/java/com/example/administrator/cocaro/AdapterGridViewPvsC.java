package com.example.administrator.cocaro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 3/12/2018.
 */

public class AdapterGridViewPvsC extends BaseAdapter {
    Context myContext;
    int myLayout;
    ArrayList<Integer> arr, rep, red, ATK, DEF, heuristic, random;
    Button btNewGame;
    int kt=0;
    public AdapterGridViewPvsC(Context myContext, int myLayout, ArrayList<Integer> arr, ArrayList<Integer> rep,
                               ArrayList<Integer> red, Button btNewgame) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.arr = arr;
        this.rep = rep;
        this.red = red;
        this.btNewGame = btNewgame;
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

        ATK = new ArrayList(); ATK.add(0);ATK.add(3);ATK.add(24);ATK.add(192);ATK.add(1536);ATK.add(12288);ATK.add(98304); ATK.add(786432);
        DEF = new ArrayList(); DEF.add(0);DEF.add(1); DEF.add(9); DEF.add(81); DEF.add(729); DEF.add(6561);DEF.add(59849); DEF.add(538641);
        heuristic = new ArrayList<>();
        random = new ArrayList<>();
        final CustomTextView customTextView = view.findViewById(R.id.custom_text);
        customTextView.setBackgroundResource(R.drawable.oco);

        if (arr.get(i)==1) customTextView.setBackgroundResource(R.drawable.ocox);
        if (arr.get(i)==-1) customTextView.setBackgroundResource(R.drawable.ocoo);
        if (rep.size()>0)
            if (rep.get(rep.size()-1)==i) customTextView.setBackgroundResource(R.drawable.ocoxx);

        customTextView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (arr.get(i)==0 && rep.size()%2==1) {
                    red.clear();
                    kt = 0;
                    arr.set(i, -1);
                    rep.add(i);
                    kt = kthang(i, -1) + ktcot(i, -1) + ktcheo1(i, -1) + ktcheo2(i, -1);
                    if (kt<0) o_win();
                    else xpx_danh();
                }
            }

            private void xpx_danh() {
                kt = 0;
                int vt = vt_x_danh();
                arr.set(vt, 1);
                rep.add(vt);
                notifyDataSetChanged();
                kt = kthang(vt, 1) + ktcot(vt, 1) + ktcheo1(vt, 1) + ktcheo2(vt, 1);
                if (kt>0) x_win();
            }

            private boolean ktxq(int vt) {
                int hang=vt/19;
                int cot=vt%19;
                for (int x=hang-1; x<=hang+1; x++)
                    for (int y=cot-1; y<=cot+1; y++)
                        if (x>=0 && x<=18 && y>=0 && y<=18 && arr.get(x*19+y)!=0 && x*19+y!=vt) return true;
                return false;
            }

            private int minimaxab(int mini, int maxi, int depth, boolean isCom){
                if (depth==4) return 0;
                if (isCom) {
                    int v = -1000000000;
                    for (int vt=rep.get(rep.size()-1); vt<19*19; vt++)
                        if (arr.get(vt)==0 && ktxq(vt)){
                            arr.set(vt, 1);
                            rep.add(vt);
                            int kt0 = kthang(vt, 1) + ktcot(vt, 1) + ktcheo1(vt, 1) + ktcheo2(vt, 1);
                            if (kt0>0) {
                                rep.remove(rep.size()-1);
                                arr.set(vt, 0);
                                return 100000000-depth;
                            }
                            v=Math.max(v, minimaxab(mini, maxi, depth+1, false));
                            rep.remove(rep.size()-1);
                            arr.set(vt, 0);
                            if (v>=maxi) return v;
                            mini=Math.max(mini, v);
                        }
                    for (int vt=rep.get(rep.size()-1); vt>=0; vt--)
                        if (arr.get(vt)==0 && ktxq(vt)){
                            arr.set(vt, 1);
                            rep.add(vt);
                            int kt0 = kthang(vt, 1) + ktcot(vt, 1) + ktcheo1(vt, 1) + ktcheo2(vt, 1);
                            if (kt0>0) {
                                rep.remove(rep.size()-1);
                                arr.set(vt, 0);
                                return 100000000-depth;
                            }
                            v=Math.max(v, minimaxab(mini, maxi, depth+1, false));
                            rep.remove(rep.size()-1);
                            arr.set(vt, 0);
                            if (v>=maxi) return v;
                            mini=Math.max(mini, v);
                        }
                    return v;
                }
                else {
                    int v = 1000000000;
                    for (int vt=rep.get(rep.size()-1); vt<19*19; vt++)
                        if (arr.get(vt)==0 && ktxq(vt)) {
                            arr.set(vt, -1);
                            rep.add(vt);
                            int kt0 = kthang(vt, -1) + ktcot(vt, -1) + ktcheo1(vt, -1) + ktcheo2(vt, -1);
                            if (kt0<0) {
                                rep.remove(rep.size()-1);
                                arr.set(vt, 0);
                                return -100000000+depth;
                            }
                            v=Math.min(v, minimaxab(mini, maxi, depth+1, true));
                            rep.remove(rep.size()-1);
                            arr.set(vt, 0);
                            if (v<=mini) return v;
                            maxi=Math.min(maxi, v);
                        }
                    for (int vt=rep.get(rep.size()-1); vt>=0; vt--)
                        if (arr.get(vt)==0 && ktxq(vt)) {
                            arr.set(vt, -1);
                            rep.add(vt);
                            int kt0 = kthang(vt, -1) + ktcot(vt, -1) + ktcheo1(vt, -1) + ktcheo2(vt, -1);
                            if (kt0<0) {
                                rep.remove(rep.size()-1);
                                arr.set(vt, 0);
                                return -100000000+depth;
                            }
                            v=Math.min(v, minimaxab(mini, maxi, depth+1, true));
                            rep.remove(rep.size()-1);
                            arr.set(vt, 0);
                            if (v<=mini) return v;
                            maxi=Math.min(maxi, v);
                        }
                    return v;
                }
            }

            private int vt_x_danh() {
                heuristic.clear(); random.clear();
                for (int vt=0; vt<19*19; vt++)
                    if (arr.get(vt)!=0) heuristic.add(0);
                    else heuristic.add(
                            Math.max(xTC_cheo1(vt)+xTC_cheo2(vt)+xTC_doc(vt)+xTC_ngang(vt),
                                    xPT_cheo1(vt)+xPT_cheo2(vt)+xPT_doc(vt)+xPT_ngang(vt)));
                for (int vt=rep.get(rep.size()-1); vt<19*19; vt++)
                    if (arr.get(vt)==0 && ktxq(vt)) {
                        arr.set(vt, 1);
                        rep.add(vt);
                        int kt0 = kthang(vt, 1) + ktcot(vt, 1) + ktcheo1(vt, 1) + ktcheo2(vt, 1);
                        if (kt0>0) {
                            arr.set(vt, 0);
                            heuristic.set(vt, 100000000);
                            break;
                        }   //neu x win luon return heuristic=100cu;
                        int value = minimaxab(-1010101010, 1010101010, 2, false);
                        if (heuristic.get(vt)<value) heuristic.set(vt, value);
                        rep.remove(rep.size()-1);
                        arr.set(vt, 0);
                    }

                for (int vt=rep.get(rep.size()-1); vt>=0; vt--)
                    if (arr.get(vt)==0 && ktxq(vt)) {
                        arr.set(vt, 1);
                        rep.add(vt);
                        int kt0 = kthang(vt, 1) + ktcot(vt, 1) + ktcheo1(vt, 1) + ktcheo2(vt, 1);
                        if (kt0>0) {
                            arr.set(vt, 0);
                            heuristic.set(vt, 100000000);
                            break;
                        }   //neu x win luon return heuristic=100cu;
                        int value = minimaxab(-1010101010, 1010101010, 2, false);
                        if (heuristic.get(vt)<value) heuristic.set(vt, value);
                        rep.remove(rep.size()-1);
                        arr.set(vt, 0);
                    }

                int max=0;
                for (int vt=0; vt<19*19; vt++)
                    if (heuristic.get(vt)>heuristic.get(max)) max=vt;
                for (int vt=0; vt<19*19; vt++)
                    if (heuristic.get(vt)==heuristic.get(max)) random.add(vt);
                Random rd = new Random();
                return random.get(rd.nextInt(random.size()));
            }

            private int xTC_cheo2(int vt) {
                int sox=0, soo=0, sono=0, sonx=0;
                for (int dem=vt; dem<vt+6*18; dem=dem+18) {
                    if (vt/19==18 || vt%19==0) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==1) sox++;
                    else if (arr.get(dem)==-1) {
                        soo++;
                        break;
                    }
                    else {
                        if (dem/19!=18 && dem%19!=0 && arr.get(dem+18)==-1) sono++;
                        if (dem/19!=18 && dem%19!=0 && arr.get(dem+18)==1) sonx++;
                        break;
                    }
                    if (dem/19==18 || dem%19==0) break;
                }
                for (int dem=vt; dem>vt-6*18; dem=dem-18) {
                    if (vt/19==0 || vt%19==18) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)== 1) sox++;
                    else if (arr.get(dem)==-1) {
                        soo++;
                        break;
                    }
                    else {
                        if (dem/19!=0 && dem/19!=18 && arr.get(dem-18)==-1) sono++;
                        if (dem/19!=0 && dem/19!=18 && arr.get(dem-18)==1) sonx++;
                        break;
                    }
                    if (dem/19==0 || dem%19==18) break;
                }
                if (soo==2 && sox<5) return 0;
                if (sono==1 && soo==1 && sox<4) return 0;
                return ATK.get(sox) + ATK.get(sonx) - DEF.get(soo) - DEF.get(sono);

            }
            private int xTC_cheo1(int vt) {
                int sox=0, soo=0, sono=0, sonx=0;
                for (int dem=vt; dem<vt+6*20; dem=dem+20) {
                    if (vt/19==18 || vt%19==18) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==1) sox++;
                    else if (arr.get(dem)==-1) {
                        soo++;
                        break;
                    }
                    else {
                        if (dem/19!=18 && dem%19!=18 && arr.get(dem+20)==-1) sono++;
                        if (dem/19!=18 && dem%19!=18 && arr.get(dem+20)==1) sonx++;
                        break;
                    }
                    if (dem/19==18 || dem%19==18) break;
                }
                for (int dem=vt; dem>vt-6*20; dem=dem-20) {
                    if (vt/19==0 || vt%19==0) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==1) sox++;
                    else if (arr.get(dem)==-1) {
                        soo++;
                        break;
                    }
                    else {
                        if (dem/19!=0 && dem%19!=0 && arr.get(dem-20)==-1) sono++;
                        if (dem/19!=0 && dem%19!=0 && arr.get(dem-20)==1) sonx++;
                        break;
                    }
                    if (dem/19==0 || dem%19==0) break;
                }
                if (soo==2 && sox<5) return 0;
                if (sono==1 && soo==1 && sox<4) return 0;
                return ATK.get(sox) + ATK.get(sonx) - DEF.get(soo) - DEF.get(sono);

            }
            private int xTC_doc(int vt) {
                int sox=0, soo=0, sono=0, sonx=0;
                for (int dem=vt; dem<vt+6*19; dem=dem+19) {
                    if (vt/19==18) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==1) sox++;
                    else if (arr.get(dem)==-1) {
                        soo++;
                        break;
                    }
                    else {
                        if (dem/19!=18 && arr.get(dem+19)==-1) sono++;
                        if (dem/19!=18 && arr.get(dem+19)==1) sonx++;
                        break;
                    }
                    if (dem/19==18) break;
                }
                for (int dem=vt; dem>vt-6*19; dem=dem-19) {
                    if (vt/19==0) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==1) sox++;
                    else if (arr.get(dem)==-1) {
                        soo++;
                        break;
                    }
                    else {
                        if (dem/19!=0 && arr.get(dem-19)==-1) sono++;
                        if (dem/19!=0 && arr.get(dem-19)==1) sonx++;
                        break;
                    }
                    if (dem/19==0) break;
                }
                if (soo==2 && sox<5) return 0;
                if (sono==1 && soo==1 && sox<4) return 0;
                return ATK.get(sox) + ATK.get(sonx) - DEF.get(soo) - DEF.get(sono);

            }
            private int xTC_ngang(int vt) {
                int sox=0, soo=0, sono=0, sonx=0;
                for (int dem=vt; dem<vt+6; dem++) {
                    if (vt%19==18) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==1) sox++;
                    else if (arr.get(dem)==-1) {
                        soo++;
                        break;
                    } else {
                        if (dem%19!=18 && arr.get(dem+1)==-1) sono++;
                        if (dem%19!=18 && arr.get(dem+1)==1) sonx++;
                        break;
                    }
                    if (dem%19==18) break;
                }
                for (int dem=vt; dem>vt-6; dem--) {
                    if (vt%19==0) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==1) sox++;
                    else if (arr.get(dem)==-1) {
                        soo++;
                        break;
                    } else {
                        if (dem%19!=0 && arr.get(dem-1)==-1) sono++;
                        if (dem%19!=0 && arr.get(dem-1)==1) sonx++;
                        break;
                    }
                    if (dem%19==0) break;
                }
                if (soo==2 && sox<5) return 0;
                if (sono==1 && soo==1 && sox<4) return 0;
                return ATK.get(sox) + ATK.get(sonx) - DEF.get(soo) - DEF.get(sono);
            }

            private int xPT_cheo2(int vt) {
                int sox=0, soo=0, sonx=0, sono=0;
                for (int dem=vt; dem<vt+6*18; dem=dem+18) {
                    if (vt/19==18 || vt%19==0) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==-1) soo++;
                    else if (arr.get(dem)==1) {
                        sox++;
                        break;
                    }
                    else {
                        if (dem/19!=18 && dem%19!=0 && arr.get(dem+18)==1) sonx++;
                        if (dem/19!=18 && dem%19!=0 && arr.get(dem+18)==-1) sono++;
                        break;
                    }
                    if (dem/19==18 || dem%19==0) break;
                }
                for (int dem=vt; dem>vt-6*18; dem=dem-18) {
                    if (vt/19==0 || vt%19==18) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==-1) soo++;
                    else if (arr.get(dem)==1) {
                        sox++;
                        break;
                    }
                    else {
                        if (dem/19!=0 && dem/19!=18 && arr.get(dem-18)==1) sonx++;
                        if (dem/19!=0 && dem/19!=18 && arr.get(dem-18)==-1) sono++;
                        break;
                    }
                    if (dem/19==0 || dem%19==18) break;
                }
                if (sox==2 && soo<5) return 0;
                if (sonx==1 && sox==1 && soo<4) return 0;
                return DEF.get(soo) + DEF.get(sono) - ATK.get(sox) - ATK.get(sonx);

            }
            private int xPT_cheo1(int vt) {
                int sox=0, soo=0, sonx=0, sono=0;
                for (int dem=vt; dem<vt+6*20; dem=dem+20) {
                    if (vt/19==18 || vt%19==18) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==-1) soo++;
                    else if (arr.get(dem)==1) {
                        sox++;
                        break;
                    }
                    else {
                        if (dem/19!=18 && dem%19!=18 && arr.get(dem+20)==1) sonx++;
                        if (dem/19!=18 && dem%19!=18 && arr.get(dem+20)==-1) sono++;
                        break;
                    }
                    if (dem/19==18 || dem%19==18) break;
                }
                for (int dem=vt; dem>vt-6*20; dem=dem-20) {
                    if (vt/19==0 || vt%19==0) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==-1) soo++;
                    else if (arr.get(dem)==1) {
                        sox++;
                        break;
                    }
                    else {
                        if (dem/19!=0 && dem%19!=0 && arr.get(dem-20)==1) sonx++;
                        if (dem/19!=0 && dem%19!=0 && arr.get(dem-20)==-1) sono++;
                        break;
                    }
                    if (dem/19==0 || dem%19==0) break;
                }
                if (sox==2 && soo<5) return 0;
                if (sonx==1 && sox==1 && soo<4) return 0;
                return DEF.get(soo) + DEF.get(sono) - ATK.get(sox) - ATK.get(sonx);

            }
            private int xPT_doc(int vt) {
                int sox=0, soo=0, sonx=0, sono=0;
                for (int dem=vt; dem<vt+6*19; dem=dem+19) {
                    if (vt/19==18) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==-1) soo++;
                    else if (arr.get(dem)==1) {
                        sox++;
                        break;
                    }
                    else {
                        if (dem/19!=18 && arr.get(dem+19)==1) sonx++;
                        if (dem/19!=18 && arr.get(dem+19)==-1) sono++;
                        break;
                    }
                    if (dem/19==18) break;
                }
                for (int dem=vt; dem>vt-6*19; dem=dem-19) {
                    if (vt/19==0) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==-1) soo++;
                    else if (arr.get(dem)==1) {
                        sox++;
                        break;
                    }
                    else {
                        if (dem/19!=0 && arr.get(dem-19)==1) sonx++;
                        if (dem/19!=0 && arr.get(dem-19)==-1) sono++;
                        break;
                    }
                    if (dem/19==0) break;
                }
                if (sox==2 && soo<5) return 0;
                if (sonx==1 && sox==1 && soo<4) return 0;
                return DEF.get(soo) + DEF.get(sono) - ATK.get(sox) - ATK.get(sonx);

            }
            private int xPT_ngang(int vt) {
                int sox=0, soo=0, sonx=0, sono=0;
                for (int dem=vt; dem<vt+6; dem++) {
                    if (vt%19==18) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==-1) soo++;
                    else if (arr.get(dem)==1) {
                        sox++;
                        break;
                    } else {
                        if (dem%19!=18 && arr.get(dem+1)==1) sonx++;
                        if (dem%19!=18 && arr.get(dem+1)==-1) sono++;
                        break;
                    }
                    if (dem%19==18) break;
                }
                for (int dem=vt; dem>vt-6; dem--) {
                    if (vt%19==0) break;
                    if (dem==vt) continue;
                    if (arr.get(dem)==-1) soo++;
                    else if (arr.get(dem)==1) {
                        sox++;
                        break;
                    } else {
                        if (dem%19!=0 && arr.get(dem-1)==1) sonx++;
                        if (dem%19!=0 && arr.get(dem-1)==-1) sono++;
                        break;
                    }
                    if (dem%19==0) break;
                }
                if (sox==2 && soo<5) return 0;
                if (sonx==1 && sox==1 && soo<4) return 0;
                return DEF.get(soo) + DEF.get(sono) - ATK.get(sox) - ATK.get(sonx);
            }

            private void x_win() {
                rep.add(-2);
                Toast.makeText(myContext, "X win", 1).show();
                btNewGame.setEnabled(true);
                for (int j=0; j<19*19; j++)
                    if (arr.get(j)==0) arr.set(j, 7);
            }
            private void o_win(){
                rep.add(-1);
                Toast.makeText(myContext, "O win", 1).show();
                btNewGame.setEnabled(true);
                for (int j=0; j<19*19; j++)
                    if (arr.get(j)==0) arr.set(j, 7);
                notifyDataSetChanged();
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
}
