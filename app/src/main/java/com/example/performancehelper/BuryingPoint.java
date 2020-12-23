package com.example.performancehelper;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BuryingPoint {

    public static void testBuryingPoint1(){
        Log.e("test","testBuryingPoint1");
    }

    public static void testBuryingPoint2(String content){
        Log.e("test","testBuryingPoint2->"+content);
    }

    public static void testBuryingPoint3(){
        Log.e("test","testBuryingPoint3");
    }

    public static void testBuryingPoint4(){
        Log.e("test","testBuryingPoint4");
    }

    public static void testBuryingPoint5(){
        Log.e("test","testBuryingPoint5");
    }

    public static void testBuryingPoint6(){
        Log.e("test","testBuryingPoint6");
    }

    public static void testBuryingPoint7(){
        Toast.makeText(AppGlobals.getApplication(),"click",Toast.LENGTH_LONG).show();
    }

    public static void testBuryingPoint8(){
        Log.e("test","BurialPointManager");
    }

    public static void testBuryingPoint9(){
        Toast.makeText(AppGlobals.getApplication(),"99999999",Toast.LENGTH_LONG).show();
    }

    public static void testBuryingPoint10(){
        Toast.makeText(AppGlobals.getApplication(),"testBuryingPoint10",Toast.LENGTH_LONG).show();
    }

    public static void testBuryingPoint11(){
        Toast.makeText(AppGlobals.getApplication(),"testBuryingPoint11",Toast.LENGTH_LONG).show();
    }

    public static int count = 0;

    public static void testBuryingPoint12(){
        Toast.makeText(AppGlobals.getApplication(),"调用了deviceId次数"+count++,Toast.LENGTH_LONG).show();
    }

    public static void testBuryingPoint13(){
        Toast.makeText(AppGlobals.getApplication(),"onClick()",Toast.LENGTH_LONG).show();
    }

    public static void testBuryingPoint14(){
        Log.e("test","埋点");
    }

    public static void onClickListener(View view){
        Log.e("test","onClickListener view.id -> "+view.getId());
    }

    public static void onLongClickListener(View view){
        Log.e("test","onLongClickListener view.id -> "+view.getId());
    }
}
