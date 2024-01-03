package com.creator.common.bean;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.creator.common.utils.FileUtil;

import java.io.File;

public class FileBean {
    public FileBean(Context context, String contentUriStr) {
        //contentUri
        this.contentUriStr = contentUriStr;
        contentUri = Uri.parse(contentUriStr);
        //realUri
        realUriStr = FileUtil.getRealPathFromUri(context, contentUriStr);
        realUri = Uri.parse(realUriStr);
        //mimeType
        mimeType = FileUtil.getMimeType(context, contentUriStr);
    }

    public FileBean(){}
    public FileBean(Context context, Uri contentUri) {
        this(context, contentUri.toString());
    }

    private String realUriStr;//文件的真实URI
    private Uri realUri;//文件的真实URI
    private String contentUriStr;//通过选择文件获取到的URI
    private Uri contentUri;//文件的真实URI
    private String fileName;//文件名
    private String mimeType;//文件的mimeType
    private Long fileSize;//文件大小
    private File file;//文件类

    private String httpUri;//http文件地址

    public String getHttpUri() {
        return httpUri;
    }

    public void setHttpUri(String httpUri) {
        this.httpUri = httpUri;
    }

    public String getRealUriStr() {
        return realUriStr;
    }

    public void setRealUriStr(String realUriStr) {
        this.realUriStr = realUriStr;
    }

    public String getContentUriStr() {
        return contentUriStr;
    }

    public void setContentUriStr(String contentUriStr) {
        this.contentUriStr = contentUriStr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public File getFile() {
        if (file == null) {
            file = new File(realUriStr);
        }
        return file;
    }



}
