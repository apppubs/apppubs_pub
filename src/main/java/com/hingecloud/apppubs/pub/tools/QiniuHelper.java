package com.hingecloud.apppubs.pub.tools;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiniuHelper {

    private String mAccessKey;
    private String mSecretKey;
    private String mBucket;

    public QiniuHelper(String accessKey, String secretKey, String bucket) {
        mAccessKey = accessKey;
        mSecretKey = secretKey;
        mBucket = bucket;
    }

    public void uploadFile(String localFilePath, String key) throws QiniuException {
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String upToken = getAccessToken();
        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        System.out.println(putRet.key);
        System.out.println(putRet.hash);
    }

    private String getAccessToken() {
        Auth auth = Auth.create(mAccessKey, mSecretKey);
        String upToken = auth.uploadToken(mBucket);
        return upToken;
    }

}
