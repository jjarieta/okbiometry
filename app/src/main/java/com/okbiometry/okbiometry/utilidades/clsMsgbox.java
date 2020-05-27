package com.okbiometry.okbiometry.utilidades;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;



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
