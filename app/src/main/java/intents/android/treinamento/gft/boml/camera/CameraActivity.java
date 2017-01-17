package intents.android.treinamento.gft.boml.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {

    private Camera camera;
    private Button takePictureButton;
    private SurfaceView surfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        setObjects();
        setActions();

        camera = Camera.open(findFrontFacingCamera());
        camera.setDisplayOrientation(90);
        surfaceView = (SurfaceView) findViewById(R.id.preview);
        surfaceView.getHolder().addCallback(this);

    }

    private void setObjects() {
        takePictureButton = (Button) findViewById(R.id.takePictureButton);
    }

    private void setActions() {
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });
    }

    private void tirarFoto() {
        camera.takePicture(null, null, this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (surfaceHolder.getSurface() != null) {
            try {
                camera.stopPreview();
            } catch (Exception e) {
            }

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
        }
    }

    public static int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        savePictureToFileSystem(bytes);
    }

    private void savePictureToFileSystem(byte[] bytes) {
        Bitmap imageFullDecoded = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(imageFullDecoded , 0, 0, imageFullDecoded .getWidth(), imageFullDecoded .getHeight(), matrix, true);
        Boolean sucesso = saveToFile(rotatedBitmap);

        Intent returnIntent = new Intent();
        if(sucesso) {
            setResult(Activity.RESULT_OK,returnIntent);
        } else {
            setResult(Activity.RESULT_CANCELED,returnIntent);
        }

        finish();
    }

    private boolean saveToFile(Bitmap imagem){

        boolean saved = false;
        try {

            File file = getOutputMediaFile();
            FileOutputStream fos = new FileOutputStream(file);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            byte[] byteArray = stream.toByteArray();

            fos.write(byteArray);
            fos.close();
            saved = true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return saved;
    }

    private File getOutputMediaFile(){
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(mediaStorageDir, "selfie.jpg");
        return imageFile;
    }
}
