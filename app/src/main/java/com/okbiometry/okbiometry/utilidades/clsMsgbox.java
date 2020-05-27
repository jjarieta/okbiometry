package com.okbiometry.okbiometry.utilidades;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.okbiometry.okbiometry.R;
import com.okbiometry.okbiometry.RegisterActivity;


public class clsMsgbox {

      public static void ShowAlert(Context context, String Mensaje, String Title){
          AlertDialog.Builder builder = new AlertDialog.Builder(context);

          builder.setTitle(Title);
          builder.setMessage(Mensaje);

          builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  // User clicked OK button
              }
          });


          AlertDialog dialog = builder.create();
          dialog.show();
      }



}
