package com.example.downloaddemookhttp3;

public class DownloadInfo {

    private final String mFileName;
    private final String mmFilePath;
    private final String mUrl;

    public DownloadInfo(Builder builder) {
        this.mFileName = builder.mFileName;
        this.mmFilePath = builder.mFilePath;
        this.mUrl = builder.mUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFileName() {
        return mFileName;
    }

    public String getFilePath() {
        return mmFilePath;
    }

    public static class Builder {
        private String mFileName;
        private String mFilePath;
        private String mUrl;

        public Builder setFileName(String fileName) {
            this.mFileName = fileName;
            return this;
        }

        public Builder setUrl(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder setFilePath(String filePath) {
            this.mFilePath = filePath;
            return this;
        }

        public DownloadInfo build() {
            return new DownloadInfo(this);
        }
    }
}
