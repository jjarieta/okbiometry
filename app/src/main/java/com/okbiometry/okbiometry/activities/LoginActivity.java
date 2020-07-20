package com.okbiometry.okbiometry.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.okbiometry.okbiometry.R;
import com.okbiometry.okbiometry.clases.clsTiposNip;
import com.okbiometry.okbiometry.clases.clsUsuario;
import com.okbiometry.okbiometry.enums.DocumentType;
import com.okbiometry.okbiometry.interfaces.MyApiService;
import com.okbiometry.okbiometry.utilidades.clsFiles;
import com.okbiometry.okbiometry.utilidades.clsMensajes;
import com.okbiometry.okbiometry.utilidades.clsMsgbox;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {


    private JSONObject JsonObject;
    public static ArrayList<clsTiposNip> ListaTipoNip = new ArrayList<>();
    private Spinner cbxTipoNip;
    private TextInputEditText txtNip;
    private Button btnValidarUser;
    private int TipoDocumento;
    private ProgressBar ProgressBar;
    private ScrollView Principial;
    private View barraLateral;
    private ImageView ImgRegister;
    public static clsUsuario ObjUsuario = new clsUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

       // clsFiles.getPrivateAlbumStorageDir(this,"Voz");

        cbxTipoNip = findViewById(R.id.cbxTipoNip);//Tipo Nip
        txtNip = findViewById(R.id.txtNip); // Nip del Usuario
        btnValidarUser = findViewById(R.id.cirLoginButton);
        ProgressBar = findViewById(R.id.progressBarLogin);
        Principial =  findViewById(R.id.RlyPrincipial);
        barraLateral =  findViewById(R.id.barraLateral);
        ImgRegister =  findViewById(R.id.ImageRegister);

        txtNip.requestFocus();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};
                requestPermissions(permissions, 1);
            }
        }

        try{
            ObtenerTiposNip();

        }catch (Exception e){
            clsMsgbox.ShowAlert(this, clsMensajes.Msg_ErrorNip,getString(R.string.app_name));
        }

        btnValidarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (TextUtils.isEmpty(txtNip.getText()) ){
                        txtNip.requestFocus();
                        txtNip.setError(clsMensajes.Msg_CampoRequerido);

                    }else{

                        hideKeyboard(LoginActivity.this);
                        UserLogin(txtNip.getText().toString().trim(),TipoDocumento);
                    }

                }catch (Exception e){
                    clsMsgbox.ShowAlert(LoginActivity.this,e.getMessage(),getString(R.string.app_name));
                }

            }
        });

        cbxTipoNip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String Valor = parent.getItemAtPosition(pos).toString();
            //    Toast.makeText(LoginActivity.this, Valor, Toast.LENGTH_SHORT).show();
                TipoDocumento = DocumentType.BuscarTipoNipDescri(Valor,ListaTipoNip);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void ObtenerTiposNip() {
        ListaTipoNip.clear();
        for(int i = 0; i< DocumentType.values().length; i++) {
            clsTiposNip ObjTipoNip = new clsTiposNip(DocumentType.values()[i].getIntvalue(), DocumentType.values()[i].getStrDescripcion());
            ListaTipoNip.add(ObjTipoNip);
        }
        cbxTipoNip.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ListaTipoNip));
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

    public void onClickValidar(View View){
        try {

            if (TextUtils.isEmpty(txtNip.getText())){
                txtNip.requestFocus();
                txtNip.setError(clsMensajes.Msg_CampoRequerido);

            }else{
                hideKeyboard(this);
                UserLogin(txtNip.getText().toString().trim(),TipoDocumento);
            }

        }catch (Exception e){
            clsMsgbox.ShowAlert(LoginActivity.this,e.getMessage(),getString(R.string.app_name));
        }

    }

    private void UserLogin(final String strNip, final int strTipo){
        class UserLoginClass extends AsyncTask<String, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                hideView();

            }

            @Override
            protected void onPostExecute(Void Respuesta) {
                super.onPostExecute(Respuesta);



            }

            @Override
            protected Void doInBackground(String... params) {


              return   ValidarUsuario();

            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(strNip, String.valueOf(strTipo));
    }

    private Void ValidarUsuario( ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(clsMensajes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApiService apiInterface = retrofit.create(MyApiService.class);

        try {

            JsonObject data = new JsonObject();
            data.addProperty("dni",txtNip.getText().toString().trim());
            data.addProperty("documentTypeId",TipoDocumento);

            Call<Object> call=apiInterface.Login(data);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    showView();

                        if (response.isSuccessful()){
                            try {

                                JsonObject=new JSONObject(new Gson().toJson(response.body()));
                                Log.e("TAG", "onResponse: "+JsonObject );
                              if (JsonObject.getString("isError").equals("false")){
                                    ObjUsuario.setDni(txtNip.getText().toString());

                                    Toast.makeText(LoginActivity.this, "El Cliente Existe", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if (jObjError.getString("isError").equals("true")){
                                    ObjUsuario.setDni(txtNip.getText().toString());
                                    ObjUsuario.setTipoDni(String.valueOf(TipoDocumento));

                                    String error=jObjError.getJSONObject("responseException").getString("exceptionMessage");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                                    builder.setTitle("El usuario no existe");
                                    builder.setMessage("¿Desea Registrarte?");

                                    builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User clicked OK button
                                            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                                            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                                        }
                                    });
                                    builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User clicked OK button

                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();;

                                }


                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void showView(){
        Principial.setVisibility(View.VISIBLE);
        barraLateral.setVisibility(View.VISIBLE);
        ProgressBar.setVisibility(View.GONE);
    }

    private void hideView(){
        Principial.setVisibility(View.GONE);
        barraLateral.setVisibility(View.GONE);

        ProgressBar.setVisibility(View.VISIBLE);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
