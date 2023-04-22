package com.example.downloaddemookhttp3;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author zhangsan
 */
public class DownloadUtil {
    private static final String TAG = DownloadUtil.class.getSimpleName();
    private volatile static OkHttpClient okHttpClient;

    private static final class DownloadUtilHolder {
        static final DownloadUtil downloadUtil = new DownloadUtil();
    }

    public static DownloadUtil get() {
        return DownloadUtilHolder.downloadUtil;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (DownloadUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public static void download(final String url, final String saveDir, final String fileName, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        DownloadUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                isExistDir(saveDir);
                try {
                    ResponseBody body = response.body();
                    is = body.byteStream();
                    long total = body.contentLength();
                    File file = new File(saveDir, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    Log.d(TAG, "===> total size = " + total + " dir = " + saveDir);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress, total);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (IOException e) {
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException ignored) {
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }

    /**
     * @param saveDir 保存的路径
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private static void isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.isDirectory()) {
            downloadFile.mkdirs();
        }
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(long progress, long totalSize);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
