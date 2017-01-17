package intents.android.treinamento.gft.boml.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_CAMERA_APLICACAO = 1;
    Button botaoCameraSimples;
    Button botaoCameraAplicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setObjects();
        setActions();
    }

    private void setObjects() {
        botaoCameraSimples = (Button) findViewById(R.id.botaoCameraSimples);
        botaoCameraAplicacao = (Button) findViewById(R.id.botaoCamera);
    }

    private void setActions() {
        botaoCameraSimples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File imageFile = new File(picsDir, "foto.jpg");
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivity(i);
            }
        });

        botaoCameraAplicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), CameraActivity.class);
                startActivityForResult(i, ACTIVITY_CAMERA_APLICACAO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_CAMERA_APLICACAO) {
            if (resultCode == RESULT_OK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Imagem capturada com sucesso!!")
                        .setTitle("Retorno Camera Aplicacao");

                AlertDialog alert = builder.create();
                alert.show();
            } else if(resultCode == RESULT_CANCELED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Houve algum problema ao capturar a imagem!!")
                        .setTitle("Retorno Camera Aplicacao");

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}
