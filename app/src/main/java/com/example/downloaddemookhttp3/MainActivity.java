package com.example.downloaddemookhttp3;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    ProgressBar processBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadButton = findViewById(R.id.btn);
        processBar = findViewById(R.id.processBar);
        String dir = MainActivity.this.getFilesDir() + "/download";
        Log.d(TAG, "===> dir = " + dir);
        List<DownloadInfo> downloadInfoList = init(MainActivity.this);
        downloadButton.setOnClickListener(v -> executor.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ; i < downloadInfoList.size(); i++) {
                    String url = downloadInfoList.get(i).getUrl();
                    String fileName = downloadInfoList.get(i).getFileName();
                    String filePath = downloadInfoList.get(i).getFilePath();
                    DownloadUtil.download(url, filePath, fileName, new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess() {
                            //成功
                            Looper.prepare();
                            Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            processBar.setProgress(100);
                            Log.i("注意", "下载成功");
                        }

                        @Override
                        public void onDownloading(long progress, long totalSize) {
                            processBar.setProgress((int) progress);
                            //进度
                            Log.i("注意", "url = " + url + " " + "fileName " + fileName + " " + "filePath = " + filePath  + "===> "+  progress + "%");
                        }

                        @Override
                        public void onDownloadFailed() {
                            //失败
                            Log.i("注意", "下载失败");
                        }
                    });
                }

            }
        }));
    }

    public static List<DownloadInfo> init(Context context) {
        String urlQ = "https://a1cdc0ff84d68fe256c0fea0c0b101fe.rdt.tfogc.com:49156/downv6.qq.com/qqweb/QQ_1/android_apk/Android_8.9.50.10650_537155547_64.apk";
        String urlWeiXin = "https://dldir1.qq.com/weixin/android/weixin8035android2360_arm64_1.apk";
        String urlTim = "https://downv6.qq.com/qqweb/QQ_1/android_apk/tim_3.5.1.3168_537150355_64.apk";
        List<String> urlList = new ArrayList<>();
        urlList.add(urlQ);
        urlList.add(urlWeiXin);
        urlList.add(urlTim);
        String dir = context.getFilesDir() + "/download";
        List<DownloadInfo> downloadInfoList = new ArrayList<>();
        for (int i = 0 ; i < urlList.size(); i++) {
            if (i == 0) {
                DownloadInfo downloadInfo0 = new DownloadInfo.Builder()
                        .setFileName("QQ.apk")
                        .setUrl(urlList.get(i))
                        .setFilePath(dir)
                        .build();
                downloadInfoList.add(downloadInfo0);
            } else if (i == 1) {
                DownloadInfo downloadInfo1 = new DownloadInfo.Builder()
                        .setFileName("QQ.apk")
                        .setUrl(urlList.get(i))
                        .setFilePath(dir)
                        .build();
                downloadInfoList.add(downloadInfo1);
            } else {
                DownloadInfo downloadInfo2 = new DownloadInfo.Builder()
                        .setFileName("QQ.apk")
                        .setUrl(urlList.get(i))
                        .setFilePath(dir)
                        .build();
                downloadInfoList.add(downloadInfo2);
            }
        }
        return downloadInfoList;
    }
}