package com.okbiometry.okbiometry.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.okbiometry.okbiometry.R;
import com.okbiometry.okbiometry.clases.clsUsuario;
import com.okbiometry.okbiometry.interfaces.MyApiService;
import com.okbiometry.okbiometry.utilidades.clsMensajes;
import com.okbiometry.okbiometry.utilidades.clsMsgbox;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.okbiometry.okbiometry.activities.LoginActivity.ListaTipoNip;
import static com.okbiometry.okbiometry.utilidades.clsMensajes.Msg_Password;


public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNombres,ediTexApellidos,editTextpass,editTextpassConfirm,editTextDni;
    private Button cirRegisterButton;
    private CheckBox CheckConsentimiento;
    private Spinner Tiponip;
    private JSONObject JsonObject;
    private int TipoDocumento;
    private ScrollView scrollView;
    private LinearLayout lyprogreso;
    public static clsUsuario ObjUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        try {
            ObjUsuarios = LoginActivity.ObjUsuario;
            CrearControles();
        }catch (Exception e){
            clsMsgbox.ShowAlert(this,"Error creando controles",getString(R.string.app_name));
        }

        cirRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckConsentimiento.isChecked()==false) {

                    CheckConsentimiento.setError(clsMensajes.Msg_CampoRequerido);
                    CheckConsentimiento.requestFocus();
                    clsMsgbox.ShowAlert(RegisterActivity.this,"Para continuar debes marcar la casilla del consentiento",getString(R.string.app_name));
                    return;
                }



            }
        });

    }

    //valida la contraseña ingresada
    private boolean ValidatePassword(String Password, String PasswordConfirm) {

        if (Password.equals(PasswordConfirm)){
            return true;
        }else{
            return false;
        }
    }

    //Creacion de controles de la interfaz Gráfica
    private void CrearControles() {
        editTextNombres =  findViewById(R.id.editTextNombres);
        ediTexApellidos =  findViewById(R.id.ediTexApellidos);
        editTextpass =  findViewById(R.id.editTextpass);
        editTextpassConfirm =  findViewById(R.id.editTextpassConfirm);
        cirRegisterButton=  findViewById(R.id.cirRegisterButton);
        CheckConsentimiento=  findViewById(R.id.CheckConsentimiento);
        editTextDni =findViewById(R.id.editTextDni);
        scrollView =  findViewById(R.id.scroll);
        lyprogreso =  findViewById(R.id.lyprogreso);

        editTextDni.setText(ObjUsuarios.getDni());
      //  editTextDni.setEnabled(false);



        //////////////////////////////////////////
        //Tipo de Nip
        Tiponip=  findViewById(R.id.cbxTipoNip2);
        Tiponip.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ListaTipoNip));



        Tiponip.setSelection(Integer.parseInt(ObjUsuarios.getTipoDni()));
      //  Tiponip.setEnabled(false);
        CheckConsentimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (CheckConsentimiento.isChecked()==true) {


                    if(TextUtils.isEmpty(editTextDni.getText().toString())){
                        editTextDni.setError(clsMensajes.Msg_CampoRequerido);
                        editTextDni.requestFocus();
                        CheckConsentimiento.setChecked(false);
                        return;
                    }
                    if(TextUtils.isEmpty(editTextNombres.getText().toString())){
                        editTextNombres.setError(clsMensajes.Msg_CampoRequerido);
                        editTextNombres.requestFocus();
                        CheckConsentimiento.setChecked(false);
                        return;
                    }

                    if(TextUtils.isEmpty(ediTexApellidos.getText().toString())){
                        ediTexApellidos.setError(clsMensajes.Msg_CampoRequerido);
                        ediTexApellidos.requestFocus();
                        CheckConsentimiento.setChecked(false);
                        return;
                    }

                    if(TextUtils.isEmpty(editTextpass.getText().toString())){
                        editTextpass.setError(clsMensajes.Msg_CampoRequerido);
                        editTextpass.requestFocus();
                        CheckConsentimiento.setChecked(false);
                        return;
                    }

                    if(TextUtils.isEmpty(editTextpassConfirm.getText().toString())){
                        editTextpassConfirm.setError(clsMensajes.Msg_CampoRequerido);
                        editTextpassConfirm.requestFocus();
                        CheckConsentimiento.setChecked(false);
                        return;
                    }

                    if (ValidatePassword(editTextpass.getText().toString(),editTextpassConfirm.getText().toString())){

                        ObjUsuarios.setDni(editTextDni.getText().toString());
                        ObjUsuarios.setTipoDni(ObjUsuarios.getTipoDni());
                        ObjUsuarios.setNombres(editTextNombres.getText().toString());
                        ObjUsuarios.setApellidos(ediTexApellidos.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), visorpdf.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);


                    }else{

                        clsMsgbox.ShowAlert(RegisterActivity.this,Msg_Password,getString(R.string.app_name));
                        CheckConsentimiento.setChecked(false);
                    }



                }

            }
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }
    private void RegisterUser(){
        class UserRegisterClass extends AsyncTask<String, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //hideView();
                lyprogreso.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }

            @Override
            protected void onPostExecute(Void Respuesta) {
                super.onPostExecute(Respuesta);



            }

            @Override
            protected Void doInBackground(String... params) {


                return   SendRegisterUser();

            }
        }
        UserRegisterClass SendData = new UserRegisterClass();
        SendData.execute();
    }
    private Void SendRegisterUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(clsMensajes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApiService apiInterface = retrofit.create(MyApiService.class);

        try {

            JsonObject data = new JsonObject();
            data.addProperty("dni",editTextDni.getText().toString().trim());
            data.addProperty("password",editTextpass.getText().toString());
            data.addProperty("lastName",ediTexApellidos.getText().toString());
            data.addProperty("name",editTextNombres.getText().toString());
            data.addProperty("documentTypeId",TipoDocumento);

            Call<Object> call=apiInterface.User(data);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                   // showView();
                    lyprogreso.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    if (response.isSuccessful()){
                        try {

                            JsonObject=new JSONObject(new Gson().toJson(response.body()));
                            Log.e("TAG", "onResponse: "+JsonObject );
                            if (JsonObject.getString("isError").equals("false")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("AVISO");
                                builder.setMessage("Registro Exitoso");
                                builder.setCancelable(false);
                                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                       // ClearFields();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("isError").equals("true")){
                                String error=jObjError.getJSONObject("responseException").getString("exceptionMessage");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("Error");
                                builder.setMessage(error);
                                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        ClearFields();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();;

                            }


                        } catch (Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }


                }
                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void ClearFields() {
        editTextDni.setText("");
        editTextNombres.setText("");
        ediTexApellidos.setText("");
        editTextpass.setText("");
        editTextpassConfirm.setText("");
        CheckConsentimiento.setChecked(false);


    }


}
