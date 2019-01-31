package com.example.pokewiki;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Dialogs {

    public static void mostrarDialogErro(int idMensagem, Activity tela){
        AlertDialog.Builder builder = new AlertDialog.Builder(tela);
        builder.setMessage(idMensagem).setTitle(R.string.msg_erro)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
