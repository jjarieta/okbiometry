package com.okbiometry.okbiometry;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.okbiometry.okbiometry.clases.clsTiposNip;
import com.okbiometry.okbiometry.enums.DocumentType;
import com.okbiometry.okbiometry.interfaces.MyApiService;
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

    private static final String BASE_URL ="https://okbiometry20200520223931.azurewebsites.net/api/Account/";
    private JSONObject JsonObject;
    private ArrayList<clsTiposNip> ListaTipoNip = new ArrayList<>();
    private Spinner cbxTipoNip;
    private TextInputEditText txtNip;
    private Button btnValidarUser;
    private int TipoDocumento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        cbxTipoNip = findViewById(R.id.cbxTipoNip);//Tipo Nip
        txtNip = findViewById(R.id.txtNip); // Nip del Usuario
        btnValidarUser = findViewById(R.id.cirLoginButton);
        txtNip.requestFocus();

        try{
            ObtenerTiposNip();

        }catch (Exception e){
            clsMsgbox.ShowAlert(this, clsMensajes.Msg_ErrorNip,getString(R.string.app_name));
        }

        btnValidarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (TextUtils.isEmpty(txtNip.getText())){
                        txtNip.requestFocus();
                        txtNip.setError(clsMensajes.Msg_CampoRequerido);
                         TipoDocumento = Integer.parseInt(cbxTipoNip.getSelectedItem().toString());
                    }else{
                        UserLogin(txtNip.getText().toString().trim(),TipoDocumento);
                    }

                }catch (Exception e){
                    clsMsgbox.ShowAlert(LoginActivity.this,clsMensajes.Msg_ConsultarUsuario,getString(R.string.app_name));
                }

            }
        });

        cbxTipoNip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String Valor = parent.getItemAtPosition(pos).toString();
                Toast.makeText(LoginActivity.this, Valor, Toast.LENGTH_SHORT).show();
                TipoDocumento = DocumentType.BuscarTipoNipDescri(Valor,ListaTipoNip);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void ObtenerTiposNip() {

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

    private void UserLogin(final String strNip, final int strTipo){
        class UserLoginClass extends AsyncTask<String, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

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
                .baseUrl(BASE_URL)
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

                        if (response.isSuccessful()){
                            try {
                                JsonObject=new JSONObject(new Gson().toJson(response.body()));
                                Log.e("TAG", "onResponse: "+JsonObject );
                                Toast.makeText(LoginActivity.this, "El Cliente Existe", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if (jObjError.getString("isError").equals("true")){
                                    String error=jObjError.getJSONObject("responseException").getString("exceptionMessage");
                                    clsMsgbox.ShowAlert(LoginActivity.this,error,"Aviso");
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


}
