package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.daasuu.gpuv.camerarecorder.CameraRecordListener;
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorder;
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import com.daasuu.gpuv.camerarecorder.LensFacing;
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R;
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.FileUtils;
import com.image.effect.timewarp.scan.filtertiktok.face.filter.widget.SampleCameraGLView;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

@SuppressWarnings("deprecation")
public class BaseCameraActivity extends BaseAppCompatActivity {

    public SampleCameraGLView sampleGLView;
    protected GPUCameraRecorder gpuCameraRecorder;

    protected String filepath;

    protected LensFacing lensFacing = LensFacing.FRONT;
    protected int cameraWidth = 1024;
    protected int cameraHeight = 576;
    protected int videoWidth = 576;
    protected int videoHeight = 1024;

    protected boolean toggleClick = false;

    protected void releaseCamera() {
        if (sampleGLView != null) {
            sampleGLView.onPause();
        }

        if (gpuCameraRecorder != null) {
            gpuCameraRecorder.stop();
            gpuCameraRecorder.release();
            gpuCameraRecorder = null;
        }

        if (sampleGLView != null) {
            ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
            sampleGLView = null;
        }
    }

    protected void setUpCameraView() {
        runOnUiThread(() -> {
            FrameLayout frameLayout = findViewById(R.id.wrap_view);
            frameLayout.removeAllViews();
            sampleGLView = null;
            sampleGLView = new SampleCameraGLView(getApplicationContext());
            /*sampleGLView.setTouchListener((event, width, height) -> {
                if (gpuCameraRecorder == null) return;
                gpuCameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
            });*/
            frameLayout.addView(sampleGLView);
        });
    }

    protected void setUpCamera() {
        Log.e("CAMERA", "setUpCamera: ");
        setUpCameraView();

        gpuCameraRecorder = new GPUCameraRecorderBuilder(this, sampleGLView).cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                    }

                    @Override
                    public void onRecordComplete() {
                        exportMp4ToGallery(getApplicationContext(), filepath);
                        onGPUCameraRecordCompleted();
                    }

                    @Override
                    public void onRecordStart() {
                        onGPUCameraRecordStarted();
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("GPUCameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(() -> setUpCamera());
                        }
                        toggleClick = false;
                    }

                    @Override
                    public void onVideoFileReady() {
                    }
                })
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build();
    }

    protected void onGPUCameraRecordStarted() {
    }

    protected void onGPUCameraRecordCompleted() {
    }

    protected interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    protected void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) throws RuntimeException {
        sampleGLView.queueEvent(() -> {
            EGL10 egl = (EGL10) EGLContext.getEGL();
            GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
            Bitmap snapshotBitmap = createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(), gl);

            runOnUiThread(() -> {
                bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
            });
        });
    }

    protected Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl) {
        Log.e("CreateBitmap", "w: " + w + " h: " + h);
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2, texturePixel, blue, red, pixel;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    texturePixel = bitmapBuffer[offset1 + j];
                    blue = (texturePixel >> 16) & 0xff;
                    red = (texturePixel << 16) & 0x00ff0000;
                    pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            Log.e("CreateBitmap", "createBitmapFromGLSurface: " + e.getMessage(), e);
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    public void saveAsPngImage(Bitmap bitmap, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
    }

    public static String getVideoFilePath() {
        return FileUtils.Companion.getExportDir().getAbsolutePath() + "/Timewarp-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".mp4";
    }

    protected static void exportPngToGallery(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static String getImageFilePath() {
        return FileUtils.Companion.getExportDir().getAbsolutePath() + "/Timewarp-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".png";
    }

}
