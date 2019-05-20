package com.example.myapplication.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Clickable;
import com.example.myapplication.R;

//To do.

public class ObjectMenu extends Dialog implements View.OnClickListener, Clickable {

    private Button ok;
    private Context context;
    private String name;
    private TextView objectName;

    public ObjectMenu(Context context,String name){
        super(context);
        this.context = context;
        this.name = name;
    }

    @Override
    public void onCreate (Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.object_menu);
        //TODO WSKAZÓWKA 2
        //ok = (button) ...
        //
        //objectName = (TextView) ...
        //
        //TODO WSKAZÓWKA 2
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ok:
                dismiss();
                break;
                default:
                    break;
        }
    }

    @Override
    public void actionOnClick (){
        ObjectMenu objectMenu = new ObjectMenu(context,name);
        objectMenu.show();
    }



}
