package com.example.downloaddemookhttp3;

public class DownloadInfo {

    private final String fileName;
    private final String filePath;
    private final String url;

    public DownloadInfo(Builder builder) {
        this.fileName = builder.fileName;
        this.filePath = builder.filePath;
        this.url = builder.url;
    }

    public String getUlr() {
        return this.url;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public static class Builder {
        private String fileName;
        private String filePath;
        private String url;

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public DownloadInfo build() {
            return new DownloadInfo(this);
        }
    }
}
