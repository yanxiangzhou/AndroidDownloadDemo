package com.example.downloaddemookhttp3;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangsan
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 5, 30,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(60)
    );
    Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadButton = findViewById(R.id.btn);
        String url = "https://a1cdc0ff84d68fe256c0fea0c0b101fe.rdt.tfogc.com:49156/downv6.qq.com/qqweb/QQ_1/android_apk/Android_8.9.50.10650_537155547_64.apk?mkey=6436946eaaab5691141c0cfc8be4c986&arrive_key=151749479430&cip=111.175.29.17&proto=https&access_type=";
        String dir = MainActivity.this.getFilesDir() + "/download";
        Log.d(TAG, "===> dir = " + dir);
        String fileName = "QQ.apk";
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        DownloadUtil.download(url, dir, fileName, new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess() {
                                //成功
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_LONG).show();
                                Looper.loop();
                                Log.i("注意", "下载成功");
                            }

                            @Override
                            public void onDownloading(int progress) {
                                //进度
                                Log.i("注意", progress + "%");
                            }

                            @Override
                            public void onDownloadFailed() {
                                //失败
                                Log.i("注意", "下载失败");
                            }
                        });
                    }

                    ;
                });
            }
        });
    }
}