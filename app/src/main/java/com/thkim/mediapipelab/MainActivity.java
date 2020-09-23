package com.thkim.mediapipelab;

import android.Manifest;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.mediapipe.components.CameraHelper;
import com.google.mediapipe.components.CameraXPreviewHelper;
import com.google.mediapipe.components.ExternalTextureConverter;
import com.google.mediapipe.components.FrameProcessor;
import com.google.mediapipe.components.PermissionHelper;
import com.google.mediapipe.formats.proto.DetectionProto;
import com.google.mediapipe.formats.proto.LocationDataProto;
import com.google.mediapipe.framework.AndroidAssetUtil;
import com.google.mediapipe.framework.PacketGetter;
import com.google.mediapipe.glutil.EglManager;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Thkim_" + MainActivity.class.getSimpleName();

    private static final String BINARY_GRAPH_NAME = "box_tracking_graph.binarypb";
    private static final String INPUT_VIDEO_STREAM_NAME = "input_video";
    private static final String OUTPUT_VIDEO_STREAM_NAME = "output_video";
    private static final CameraHelper.CameraFacing CAMERA_FACING = CameraHelper.CameraFacing.FRONT;

    private static final CameraHelper.CameraFacing CAMERA_BACK = CameraHelper.CameraFacing.BACK;

    // Flips the camera-preview frames vertically before sending them into FrameProcessor to be
    // processed in a MediaPipe graph, and flips the processed frames back when they are displayed.
    // This is needed because OpenGL represents images assuming the image origin is at the bottom-left
    // corner, whereas MediaPipe in general assumes the image origin is at top-left.
    private static final boolean FLIP_FRAMES_VERTICALLY = true;

    static {
        // Load all native libraries needed by the app.
//        System.loadLibrary("mediapipe_jni");
//        System.loadLibrary("opencv_java3");
    }

    // {@link SurfaceTexture} where the camera-preview frames can be accessed.
    private SurfaceTexture previewFrameTexture;
    // {@link SurfaceView} that displays the camera-preview frames processed by a MediaPipe graph.
    private SurfaceView previewDisplayView;

    // Creates and manages an {@link EGLContext}.
    private EglManager eglManager;
    // Sends camera-preview frames into a MediaPipe graph for processing, and displays the processed
    // frames onto a {@link Surface}.
//    private FrameProcessor processor;
    private FrameProcessor processor;
    // Converts the GL_TEXTURE_EXTERNAL_OES texture from Android camera into a regular texture to be
    // consumed by {@link FrameProcessor} and the underlying MediaPipe graph.
    private ExternalTextureConverter converter;
//    private CustomExternalTextureConverter converter;

    // Handles camera access via the {@link CameraX} Jetpack support library.
    private CameraXPreviewHelper cameraHelper;

    private Button removeBt;
    private Button setBt;

    private Button startButton;
    private Button downloadButton;

    private String filePath;
    private String arm64_v8a = "arm64-v8a";
    private String armeabi_v7a = "armeabi-v7a";
    private String mpJni = "libmediapipe_jni.so";
    private String openCVJni = "libopencv_java3.so";

    private static final String MODEL_NAME = "ssdlite_object_detection.tflite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewDisplayView = new SurfaceView(this);
        setupPreviewDisplayView();

        DownloadService downloadService = new DownloadService(this);

        downloadService.downloadJniFor64();

        filePath = new File(getExternalCacheDir() + File.separator + arm64_v8a).toString();

        // Load all native libraries needed by the app.
        try {
            System.load(filePath + "/" + mpJni);
            System.load(filePath + "/" + openCVJni);
        } catch (Exception e ) {
            e.printStackTrace();
        }

        AndroidAssetUtil.initializeNativeAssetManager(this);

        Log.d(TAG, Environment.getExternalStorageDirectory().getPath());
        Log.d(TAG, this.getCacheDir().getAbsolutePath());

        Log.d(TAG, this.getFilesDir().getPath());

        eglManager = new EglManager(null);

        processor = new FrameProcessor(this, BINARY_GRAPH_NAME);

        PermissionHelper.checkAndRequestCameraPermissions(this);



        startButton = findViewById(R.id.start_btn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
                processor.addPacketCallback(
                        "tracked_detections",
                        (packet) -> {
                            Log.d(TAG, "Received .");
                            List<DetectionProto.Detection> detections = PacketGetter.getProtoVector(packet, DetectionProto.Detection.parser());
                            Log.d(TAG, "[TS:"
                                    + packet.getTimestamp()
                                    + "] "
                                    + getBoxDetectionProtoDebugString(detections));
                        });
            }
        });
        downloadButton = findViewById(R.id.model_btn);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Exam DownloadService
//                startFileDownload();
//                downloadFile();

                downloadService.downloadModel();

            }
        });

        Button mkdirBtn = findViewById(R.id.mkdir_btn);
        mkdirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                makeDirectory("mediapipe_asset_cache");
                downloadService.downloadJniFor64();

            }
        });

        Button loadLibs = findViewById(R.id.jni64_btn);
        loadLibs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        Log.d(TAG, getExternalCacheDir() + File.separator);

        requestPermission();
        // Exam DownloadService
//        registerReceiver();
    }
//    /* ---------------------------------------------------------------------------------- */
//    /* ---------------------------- Exam DownloadService Start ---------------------------- */
//    /* ---------------------------------------------------------------------------------- */
//
//    private void downloadFile() {
//        DownloadApi downloadApi = DownloadServiceTemp.createService(DownloadApi.class);
//
//        Call<ResponseBody> call = downloadApi.downloadTest();
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
//
//                    Log.d(TAG, "writtenToDisk : " + writtenToDisk);
//                    Log.d(TAG, "File download is successful.");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "File download is failed.");
//            }
//        });
//    }
//
//    private boolean makeDirectory(String folderName) {
//        Log.d(TAG, "makeDirectory()");
//        File dir = new File(getExternalCacheDir() + File.separator + folderName);
//        if (!dir.exists()) {
//            Log.d(TAG, "return true");
//            return dir.mkdirs();
//        }
//        Log.d(TAG, "return false");
//        return false;
//    }
//
//    private boolean writeResponseBodyToDisk(ResponseBody body) {
//        try {
//            makeDirectory("mediapipe_asset_cache");
//            File modelFile = new File(
//                    getExternalCacheDir()
//                            + File.separator
//                            + "mediapipe_asset_cache/"
//                            + "ssdlite_object_detection.tflite");
//
//            if (modelFile.exists()) {
//                Toast.makeText(MainActivity.this, "파일이 있음.", Toast.LENGTH_SHORT).show();
//                return true;
//            } else {
//                Toast.makeText(MainActivity.this, "파일이 없음.", Toast.LENGTH_SHORT).show();
//
//                InputStream inputStream = null;
//                OutputStream outputStream = null;
//
//                try {
//                    byte[] fileReader = new byte[4096];
//                    long fileSize = body.contentLength();
//                    long fileSizeDownloaded = 0;
//
//                    inputStream = body.byteStream();
//                    outputStream = new FileOutputStream(modelFile);
//
//                    while (true) {
//                        int read = inputStream.read(fileReader);
//
//                        if (read == -1) {
//                            break;
//                        }
//
//                        outputStream.write(fileReader, 0, read);
//
//                        fileSizeDownloaded += read;
//
//                        Log.d(TAG, "File Download : " + fileSizeDownloaded + " of " + fileSize);
//
//                        outputStream.flush();
//
//                    }
//                    return true;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return false;
//                } finally {
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//
//                    if (outputStream != null) {
//                        outputStream.close();
//                    }
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /* ---------------------------------------------------------------------------------- */
//    /* ---------------------------- Exam DownloadService End ---------------------------- */
//    /* ---------------------------------------------------------------------------------- */

    /* Detected List 값을 보여줌. */
    private static String getBoxDetectionProtoDebugString(List<DetectionProto.Detection> detections) {
        if (detections.isEmpty()) {
            return "No face landmarks";
        }
        String detectionListLog = "Number of Detected object: " + detections.size() + "\n";


        for (DetectionProto.Detection detectionList : detections) {
            LocationDataProto.LocationData locationData = detectionList.getLocationData();

            /* RelativeBoundingBox */
            detectionListLog +=
                    "\t\tRelativeBoundingBox width : "
                            + locationData.getRelativeBoundingBox().getWidth()
                            + "\t";
            detectionListLog +=
                    "\t\tRelativeBoundingBox height : "
                            + locationData.getRelativeBoundingBox().getHeight()
                            + "\n";

            detectionListLog +=
                    "\t\tRelativeBoundingBox getXmin : "
                            + locationData.getRelativeBoundingBox().getXmin()
                            + "\t";
            detectionListLog +=
                    "\t\tRelativeBoundingBox getYmin : "
                            + locationData.getRelativeBoundingBox().getYmin()
                            + "\n";

            /* Label Name */
            for (String name : detectionList.getLabelList()) {
                detectionListLog +=
                        "\t\tgetLabelList : "
                                + name
                                + "\t";
            }

            /* Label Score */
            for (Float name : detectionList.getScoreList()) {
                detectionListLog +=
                        "\tgetScoreList : "
                                + name
                                + "\t";
            }

            /* Label Id */
            detectionListLog +=
                    "\tgetDetectionId : "
                            + detectionList.getDetectionId()
                            + "\n\n";
        }
        return detectionListLog;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        converter = new ExternalTextureConverter(eglManager.getContext());
//        converter = new CustomExternalTextureConverter(eglManager.getContext(), 2, 270);
        converter.setFlipY(FLIP_FRAMES_VERTICALLY);
//        converter.setConsumer(processor);

//        startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        converter.close();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private SurfaceTexture texture;

    private void setupPreviewDisplayView() {
        Log.d(TAG, "setupPreviewDisplayView()");
        previewDisplayView.setVisibility(View.GONE);
        ViewGroup viewGroup = findViewById(R.id.preview_display_layout);
        viewGroup.addView(previewDisplayView);

        previewDisplayView
                .getHolder()
                .addCallback(
                        new SurfaceHolder.Callback() {
                            @Override
                            public void surfaceCreated(SurfaceHolder holder) {
                                Log.d(TAG, "surfaceCreated()");

                                converter.setConsumer(processor);
                                processor.addVideoStreams(eglManager.getNativeContext(), INPUT_VIDEO_STREAM_NAME, OUTPUT_VIDEO_STREAM_NAME);
                                processor.getVideoSurfaceOutput().setFlipY(FLIP_FRAMES_VERTICALLY);
                                processor.getVideoSurfaceOutput().setSurface(holder.getSurface());

                            }

                            @Override
                            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                Log.d(TAG, "surfaceChanged()");

                                // (Re-)Compute the ideal size of the camera-preview display (the area that the
                                // camera-preview frames get rendered onto, potentially with scaling and rotation)
                                // based on the size of the SurfaceView that contains the display.
                                Size viewSize = new Size(width, height);
                                Log.d(TAG, String.format("surface Width : %d, Height : %d", width, height));
                                Size displaySize = cameraHelper.computeDisplaySizeFromViewSize(viewSize);

                                // Connect the converter to the camera-preview frames as its input (via
                                // previewFrameTexture), and configure the output width and height as the computed
                                // display size.
                                Log.d(TAG, "Width : " + displaySize.getWidth() + " Height : " + displaySize.getHeight());

                                // Display 비율 및 해상도
                                converter.setSurfaceTextureAndAttachToGLContext(
                                        previewFrameTexture,
                                        displaySize.getWidth(),
                                        displaySize.getHeight());

                            }

                            @Override
                            public void surfaceDestroyed(SurfaceHolder holder) {
                                Log.d(TAG, "surfaceDestroyed()");

                                processor.getVideoSurfaceOutput().setSurface(null);
                            }
                        });
    }

    private void startCamera() {
        Log.d(TAG, "startCamera()");

        cameraHelper = new CameraXPreviewHelper();

        cameraHelper.setOnCameraStartedListener(
                surfaceTexture -> {
                    previewFrameTexture = surfaceTexture;
                    // Make the display view visible to start showing the preview. This triggers the
                    // SurfaceHolder.Callback added to (the holder of) previewDisplayView.
                    previewDisplayView.setVisibility(View.VISIBLE);
                });

        cameraHelper.startCamera(this, CAMERA_BACK, /*surfaceTexture=*/ null);
    }
}
