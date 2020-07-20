package com.okbiometry.okbiometry.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.okbiometry.okbiometry.R;
import com.okbiometry.okbiometry.clases.clsUsuario;
import com.okbiometry.okbiometry.enums.DocumentType;

import java.io.IOException;

import static com.okbiometry.okbiometry.activities.LoginActivity.ListaTipoNip;
import static com.okbiometry.okbiometry.activities.RegisterActivity.ObjUsuarios;

public class RecordVoice extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

    private PlayButton   playButton = null;
    private MediaPlayer   player = null;
    public clsUsuario ObjUsuarios;
    private TextView TextoGrabar,txtAviso;

    private LinearLayout linearLayout;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @SuppressLint("AppCompatCustomView")
    class RecordButton extends ImageView {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                   txtAviso.setText("Detener Grabación");
                    setImageDrawable(getResources().getDrawable(R.drawable.ic_back_img));
                } else {
                    txtAviso.setText("Iniciar Grabación");
                    setImageDrawable(getResources().getDrawable(R.drawable.btngrabar));
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
           txtAviso.setText("Iniciar grabacion");
            setOnClickListener(clicker);
        }
    }

    @SuppressLint("AppCompatCustomView")
    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_voice);
        ObjUsuarios=LoginActivity.ObjUsuario;

        TextoGrabar =  findViewById(R.id.Textograbacion);
        TextoGrabar.setText("YO, " +ObjUsuarios.getNombres().toUpperCase()+" "+ObjUsuarios.getApellidos().toUpperCase()+" IDENTIFICADO CON "+ DocumentType.BuscarTipoNipCodigo(Integer.parseInt(ObjUsuarios.getTipoDni()),ListaTipoNip).toUpperCase()+ " NÚMERO "+ObjUsuarios.getDni() + " ACEPTO TERMINOS Y CONDICIONES");
        linearLayout = findViewById(R.id.linearLayout);
        txtAviso =findViewById(R.id.txtAviso);
        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        recordButton = new RecordButton(this);
        recordButton.setImageDrawable(getResources().getDrawable(R.drawable.btngrabar));
        linearLayout.addView(recordButton,
                new LinearLayout.LayoutParams(
                        500,
                        500,
                        0));
        linearLayout.setGravity(Gravity.CENTER);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}
    