package com.sharmastech.skillhouettes.cache;

import android.content.Context;
import android.net.Uri;

import com.sharmastech.skillhouettes.common.Constants;
import com.sharmastech.skillhouettes.utils.TraceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

public class CacheManager {

    private final int CACHE_ITEMS_EXTN = 1;

    private final int CACHE_STREAM_EXTN = 2;

    private final int CACHE_ITEMS_INTRN = 3;

    private final int CACHE_STREAM_INTRN = 4;

    private final int CACHE_ITEMS_EXTN_P = 5;

    private final int CACHE_STREAM_INTRN_P = 6;

    private final int CACHE_ITEMS_INTRN_ONLY = 7;

    private static CacheManager mCacheManager = null;

    private Context mContext;

    private CacheManager(Context aContext) {
        mContext = aContext;
    }

    /**
     * getInstance -
     *
     * @return CacheManager
     */
    public static CacheManager getInstance(Context context) {
        if (mCacheManager == null)
            mCacheManager = new CacheManager(context);

        return mCacheManager;
    }

    private void cacheObjExtn(String fileName, Object object, String path) {

        fileName = String.valueOf(fileName.hashCode());

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(path + "/" + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            TraceUtils.logException(e);
        }
    }

    public void cacheObjIntrn(String fileName, Object object) {

        fileName = String.valueOf(fileName.hashCode());

        FileOutputStream fileOS = null;

        ObjectOutputStream objectOS = null;

        try {
            fileOS = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            objectOS = new ObjectOutputStream(fileOS);
            objectOS.writeObject(object);
            objectOS.flush();
        } catch (Exception e) {
            TraceUtils.logException(e);
        } finally {

            try {
                if (fileOS != null)
                    fileOS.close();
                fileOS = null;

                if (objectOS != null)
                    objectOS.close();
                objectOS = null;

            } catch (Exception e) {
                TraceUtils.logException(e);
            }
        }
    }

    public void cacheStreamExtn(String fileName, InputStream inputStream,
                                String path) {
        FileOutputStream fileOut = null;
        fileName = String.valueOf(fileName.hashCode());
        try {

            String line = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    inputStream));

            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(path + "/" + fileName);

            fileOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fileOut, "UTF-8");

            while ((line = rd.readLine()) != null) {
                osw.append(line);
            }

            osw.flush();
            osw.close();

            if (rd != null) {
                rd.close();
                rd = null;
            }

            file = null;

        } catch (Exception e) {
            TraceUtils.logException(e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                inputStream = null;

                if (fileOut != null)
                    fileOut.close();
                fileOut = null;

            } catch (Exception e) {
                TraceUtils.logException(e);
            }
        }

    }

    public void cacheStreamIntrn(String fileName, InputStream inputStream) {

        FileOutputStream fileOut = null;
        fileName = String.valueOf(fileName.hashCode());
        try {

            String line = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    inputStream));
            fileOut = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fileOut, "UTF-8");

            while ((line = rd.readLine()) != null) {
                osw.append(line);
            }

            osw.flush();
            osw.close();

            rd.close();
            rd = null;

        } catch (Exception e) {
            TraceUtils.logException(e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                inputStream = null;

                if (fileOut != null)
                    fileOut.close();
                fileOut = null;

            } catch (Exception e) {
                TraceUtils.logException(e);
            }
        }
    }

    // /////////////////////////-------------Get
    // Cache------------------//////////////////////////////////////////////

    public Object getIntrnObj(String fileName) throws Exception {

        FileInputStream fileIS = null;
        ObjectInputStream objectIS = null;
        Object object = null;

        try {
            fileName = String.valueOf(fileName.hashCode());
            File iFile = mContext.getFileStreamPath(fileName);
            if (iFile.exists()) {
                fileIS = mContext.openFileInput(fileName);
                objectIS = new ObjectInputStream(fileIS);
                object = objectIS.readObject();
                objectIS.close();
                return object;
            }

        } catch (Exception e) {
            TraceUtils.logException(e);
            throw e;
        } finally {

            try {

                if (fileIS != null)
                    fileIS.close();
                fileIS = null;

                if (objectIS != null)
                    objectIS.close();
                objectIS = null;

            } catch (Exception e) {
                TraceUtils.logException(e);
            }
        }

        return null;
    }

    public Object getExtnObject(String fileName, String path) throws Exception {

        Object object = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectIS = null;

        fileName = String.valueOf(fileName.hashCode());

        File file = new File(path + "/" + fileName);
        if (!file.exists()) {
            return null;
        }

        try {
            fileInputStream = new FileInputStream(path + "/" + fileName);
            objectIS = new ObjectInputStream(fileInputStream);
            object = objectIS.readObject();
            objectIS.close();

        } catch (Exception e) {
            TraceUtils.logException(e);
        } finally {

            try {

                if (fileInputStream != null)
                    fileInputStream.close();
                fileInputStream = null;

                if (objectIS != null)
                    objectIS.close();
                objectIS = null;

            } catch (Exception e) {
                TraceUtils.logException(e);
            }

        }

        return object;
    }

    public InputStream getExtnStream(String fileName, String path) {

        InputStream inputStream = null;
        FileInputStream fileInputStream = null;
        try {

            fileName = String.valueOf(fileName.hashCode());

            File file = new File(path + "/" + fileName);
            if (!file.exists()) {
                return null;
            }
            fileInputStream = new FileInputStream(file);
            inputStream = fileInputStream;

        } catch (Exception e) {
            TraceUtils.logException(e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                inputStream = null;

                if (fileInputStream != null)
                    fileInputStream.close();
                fileInputStream = null;

            } catch (Exception e) {
                TraceUtils.logException(e);
            }
        }
        return inputStream;
    }

    public InputStream getIntrnStream(String fileName) {

        InputStream inputStream = null;
        FileInputStream fileInputStream = null;
        try {

            fileName = String.valueOf(fileName.hashCode());

            File iFile = mContext.getFileStreamPath(fileName);
            if (iFile.exists()) {
                fileInputStream = mContext.openFileInput(fileName);
                inputStream = fileInputStream;
            }

        } catch (Exception e) {
            TraceUtils.logException(e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                inputStream = null;

                if (fileInputStream != null)
                    fileInputStream.close();
                fileInputStream = null;

            } catch (Exception e) {
                TraceUtils.logException(e);
            }
        }
        return inputStream;
    }

    public void deleteCacheFiles(int pageCount, String fileName) {

        try {

            Uri uri = Uri.parse(fileName);

            String value = uri.getQueryParameter("pno");

            while (pageCount > -1) {

                if (value != null) {
                    fileName = fileName.replace("pno=" + value, "pno=" + pageCount);
                }

                String fileNameEncode = fileName;

                fileNameEncode = String.valueOf(fileNameEncode.hashCode());

                File file = new File(Constants.getCacheTempPath(mContext) + "/" + fileNameEncode);
                if (file.exists()) {
                    file.delete();
                }
                pageCount--;
            }

        } catch (Exception e) {
            TraceUtils.logException(e);
        }
    }

    public void setCache(String requestUrl, int aCacheType, Object object) {
        switch (aCacheType) {

            case CACHE_ITEMS_EXTN:
                cacheObjExtn(requestUrl, object, Constants.getCacheTempPath(mContext)/*Constants.CACHETEMP*/);
                break;

            case CACHE_STREAM_EXTN:

                break;

            case CACHE_ITEMS_INTRN:
                cacheObjIntrn(requestUrl, object);
                break;

            case CACHE_STREAM_INTRN:

                break;

            case CACHE_ITEMS_EXTN_P:
                cacheObjExtn(requestUrl, object, Constants.getCacheDataPath(mContext)/*Constants.CACHEDATA*/);
                break;

            case CACHE_STREAM_INTRN_P:

                break;

            case CACHE_ITEMS_INTRN_ONLY:
                cacheObjIntrn(requestUrl, object);
                break;

            default:
                break;
        }
    }

    public Object getCache(String requestUrl, int aCacheType) {
        Object object = null;
        try {

            switch (aCacheType) {

                case CACHE_ITEMS_EXTN:
                    object = getExtnObject(requestUrl, Constants.getCacheTempPath(mContext));
                    break;

                case CACHE_STREAM_EXTN:
                    break;

                case CACHE_ITEMS_INTRN:
                    object = getIntrnObj(requestUrl);
                    break;

                case CACHE_STREAM_INTRN:
                    break;

                case CACHE_ITEMS_EXTN_P:
                    object = getExtnObject(requestUrl, Constants.getCacheDataPath(mContext));
                    break;

                case CACHE_STREAM_INTRN_P:
                    break;

                case CACHE_ITEMS_INTRN_ONLY:
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return object;
    }

}