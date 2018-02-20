package com.example.ozeki.fishing5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;

public class FindFishingGroundActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener {

    // カメラビューのインスタンス
    // CameraBridgeViewBase は JavaCameraView/NativeCameraView のスーパークラス
    private CameraBridgeViewBase mCameraView;
    private final int MAX_GROUND_NUM = 3;
    private ImageButton location_button;
    private FrameLayout.LayoutParams buttonLayoutParams;
    // dp単位を取得
    private float scale;

    private Point location_pt;
    private Mat location_frame;
    private double radius;

    private int buttonWidth;
    private int buttonHeight;

    int displayHeightPixel;
    int displayWidthPixel;

    boolean run_camera;

    SharedPreferences pref;

    private Handler handler = new Handler();

    // ライブラリ初期化完了後に呼ばれるコールバック (onManagerConnected)
    // public abstract class BaseLoaderCallback implements LoaderCallbackInterface
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                // 読み込みが成功したらカメラプレビューを開始
                case LoaderCallbackInterface.SUCCESS:
                    mCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_fishing_ground);

        displayHeightPixel = getResources().getDisplayMetrics().heightPixels;
        displayWidthPixel = getResources().getDisplayMetrics().widthPixels;

        // ボタンの設定
        location_button = findViewById(R.id.locationButton);

        // dp単位を取得
        scale = getResources().getDisplayMetrics().density;
        // ボタンサイズ
        buttonWidth = location_button.getLayoutParams().width;
        buttonHeight = location_button.getLayoutParams().height;

        //ボタンのRelativeLayoutの子要素指定
        buttonLayoutParams = new FrameLayout.LayoutParams(buttonWidth, buttonHeight);

        location_button.setLayoutParams(buttonLayoutParams);

        //カメラストップ用
        run_camera = true;

        // カメラビューのインスタンスを変数にバインド
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        // リスナーの設定 (後述)
        mCameraView.setCvCameraViewListener(this);

        location_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                run_camera = false;
                check_location(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 非同期でライブラリの読み込み/初期化を行う
        // static boolean initAsync(String Version, Context AppContext, LoaderCallbackInterface Callback)
        if (!OpenCVLoader.initDebug()) {
            Log.d("onResume", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("onResume", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        // カメラプレビュー開始時に呼ばれる
        Log.d("camera", "viewstart");
    }

    @Override
    public void onCameraViewStopped() {
        // カメラプレビュー終了時に呼ばれる
    }

    // CvCameraViewListener の場合
    // フレームをキャプチャする毎(30fpsなら毎秒30回)に呼ばれる
    @Override
    public Mat onCameraFrame(Mat inputFrame) {

        if(!run_camera){ return location_frame; }

        // 円を検出する部分
        Mat gray = new Mat(inputFrame.rows(), inputFrame.cols(), CvType.CV_8SC1);
        Imgproc.cvtColor(inputFrame, gray, Imgproc.COLOR_RGB2GRAY);

        Mat circles = new Mat();// 検出した円の情報格納する変数

        //画面サイズの取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        android.graphics.Point realSize = new android.graphics.Point();
        disp.getRealSize(realSize);

        int screen_width = realSize.x;
        int screen_height = realSize.y;

        //円の取得
        Imgproc.HoughCircles(
                gray,
                circles,
                Imgproc.CV_HOUGH_GRADIENT,
                2,
                (screen_height * 2) / 3, //円の中心同士の最小距離
                160,
                50,
                screen_height / 6,
                screen_height / 3
        );

        Log.d("長さ","" + screen_height );
        location_pt = new Point();
        location_frame = inputFrame;
        // 検出した直線上を緑線で塗る
        if(circles.cols() > 0) {
            double data[] = circles.get(0, 0);
            location_pt.x = data[0];
            location_pt.y = data[1];
            radius = data[2];
            Imgproc.circle(location_frame, location_pt, (int) radius, new Scalar(0,153,255), -1);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    // ここに処理
                    buttonLayoutParams.setMargins(
                            /* 参考
                            location_pt.y=141.0
                            location_pt.x=1149.0
                            location_frame.height()=960
                            location_frame.width()=1280
                            cale=3.0*/
                            (int)(mCameraView.getPaddingLeft() + location_pt.x + (buttonWidth)),
                            (int)(mCameraView.getPaddingTop() + location_pt.y - buttonHeight),
                            buttonLayoutParams.rightMargin,
                            buttonLayoutParams.bottomMargin
                    );
                    location_button.setLayoutParams(buttonLayoutParams);
                    location_button.setVisibility(View.VISIBLE);
                }
            });
/*
            Log.d("デバッグ", "width:"+ mCameraView.getWidth());
            Log.d("デバッグ", "height:"+ mCameraView.getHeight() );
            Log.d("デバッグ", "ボタンwidth:"+ buttonWidth);
            Log.d("デバッグ", "ボタンheight:"+ buttonHeight);
            */
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    location_button.setVisibility(View.INVISIBLE);
                }
            });
        }

        return location_frame;
    }

    public interface CvCameraViewFrame {
        // 1チャンネルグレースケールのMatインスタンスを返す
        public Mat gray();
    }

    private void check_location(View view){
        //matからbitmapの変換
        Bitmap target_location_frame = Bitmap.createBitmap(
                location_frame.width(),
                location_frame.height(),
                Bitmap.Config.ARGB_8888
        );
        Utils.matToBitmap(location_frame, target_location_frame);

        //bitmapのjpg変換して保存
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        target_location_frame.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        pref = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fishing_ground", bitmapStr);
        editor.apply();

        Point target_location_pt = location_pt;

        double target_radius = radius;
        //FishingGround target_fishing_ground = new FishingGround(target_location_frame, target_location_pt, target_radius);
        Intent intent = new Intent(FindFishingGroundActivity.this, FishingActivity.class);
        intent.putExtra("target_radius", target_radius);
        intent.putExtra("target_location_pt_x", target_location_pt.x);
        intent.putExtra("target_location_pt_y", target_location_pt.y);
        startActivity(intent);
    }
}