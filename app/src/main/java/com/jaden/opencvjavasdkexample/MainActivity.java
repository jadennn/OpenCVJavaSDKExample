package com.jaden.opencvjavasdkexample;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OpenCV_Test";

    private ImageView mSrcIV;
    private ImageView mDstIV;
    private Button mSwitchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOpenCV();
    }

    private void initUI(){
        mSrcIV = findViewById(R.id.src_img);
        mDstIV = findViewById(R.id.dst_img);
        mSwitchBtn = findViewById(R.id.switch_btn);
        mSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSrcIV.setDrawingCacheEnabled(true);
                Bitmap src = mSrcIV.getDrawingCache();
                mDstIV.setImageBitmap(rgb2Gray(src));
                mSrcIV.setDrawingCacheEnabled(false);
            }
        });
    }


    //load OpenCV
    private void loadOpenCV(){
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }

        }
    };

    //使用opencv将rgb图转成灰度图
    public Bitmap rgb2Gray(Bitmap src){
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(src, rgbMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, dst); //convert mat to bitmap
        return dst;
    }

}
