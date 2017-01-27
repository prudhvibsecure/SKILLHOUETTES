package com.sharmastech.skillhouettes.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.callbacks.IRequestCallback;
import com.sharmastech.skillhouettes.common.Constants;
import com.sharmastech.skillhouettes.common.Item;
import com.sharmastech.skillhouettes.common.ProjectHeaders;
import com.sharmastech.skillhouettes.utils.TraceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

public class HTTPGetTask extends AsyncTask<String, Long, Integer> {

    String requestUrl = null;

    HttpURLConnection conn = null;
    InputStream inputStream = null;
    IRequestCallback callback = null;
    String mimeType = null;
    Context context = null;
    private String downloadFileName = "";

    boolean saveToFile = false;
    static int RESCODE = 0;
    private long length = 0;

    long mDownloadStartTime = 0;

    private double timeTaken = 0;
    
    public HTTPGetTask(Context context, IRequestCallback callback,
                       boolean savetofile, String downloadname) {
        this.context = context;
        this.callback = callback;
        this.saveToFile = savetofile;
        this.downloadFileName = downloadname;
    }

    @Override
    protected void onCancelled() {

        try {
            if (conn != null)
                conn.disconnect();

            conn = null;
            callback.onRequestCancelled(context.getString(R.string.yhcdr));

            File file = new File(Constants.DWLPATH + "/" + downloadFileName);
            if (file.exists()) {
                file.delete();
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }

        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onRequestInitiated();
    }

    @Override
    protected Integer doInBackground(String... params) {

        try {

            if (!isNetworkAvailable()) {
                return 5;
            }

            requestUrl = params[0];
            requestUrl = urlEncode(requestUrl);

           Item item = ProjectHeaders.getInstance(context).getHeaders();

            long startTime = System.currentTimeMillis();

            conn = getHTTPConnection(requestUrl, item);

            if (conn == null) {
                return 1;
            }

            long endTime = System.currentTimeMillis();

            timeTaken = endTime - startTime;

            timeTaken = timeTaken / 1000.0;

            mDownloadStartTime = System.currentTimeMillis();

            mimeType = conn.getContentType();
            if (mimeType == null) {
                return 9;
            }

            String type = mimeType;

            TraceUtils.logE("mimeType::", mimeType + "");

            inputStream = conn.getInputStream();

            length = conn.getContentLength();

			/*Log.e("-=-=-content lenght-=-=-=-", length+"");
			Log.e("-=-=-file size=-=-=-=-", conn.getHeaderField("content-filesize")+"");
			Log.e("-=-=-file size Content-Length =-=-=-=-", conn.getHeaderField("Content-Length")+"");*/

            if (length == -1) {
                String leng = conn.getHeaderField("content-filesize");//content-filesize
                if (leng != null)
                    length = Long.parseLong(leng);
            }

            if (type != null && saveToFile)
                if (type.contains("video/")
                        || type.contains("image/")
                        || type.contains("audio/")
                        || type.contains("application/vnd.android.package-archive")
                        || type.contains("videotone/"))
                    saveFile(mimeType, length);
                else {

                    inputStream = conn.getInputStream();

                    byte[] bytebuf = new byte[0x1000];

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    for (; ; ) {
                        int len = inputStream.read(bytebuf);
                        if (len < 0)
                            break;
                        baos.write(bytebuf, 0, len);
                    }

                    bytebuf = baos.toByteArray();

                    //String response = new String(bytebuf, "UTF-8");

                    return 10;
                }

            return 0;

        } catch (MalformedURLException me) {
            //e.printStackTrace();
            return 4;
        } catch (ConnectException e) {
            //e.printStackTrace();
            return 3;
        } catch (SocketException se) {
            //e.printStackTrace();
            return 6;
        } catch (SocketTimeoutException stex) {
            stex.printStackTrace();
            return 2;
        } catch (Exception ex) {
            ex.printStackTrace();
            String val = ex.getMessage();

            if (val != null && val.toLowerCase().contains("permission denied")) {
                return 11;
            }

            return 7;
        } finally {

            if (inputStream != null)
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                }

            if (conn != null)
                conn.disconnect();
            conn = null;

        }

    }

    protected void onPostExecute(Integer result) {

        try {

            if (result != 0) {
                if (result == 20)

                    if (result == 3)
                        callback.onRequestFailed(context.getString(R.string.snr1), result);
                if (result == 2)
                    callback.onRequestFailed(context.getString(R.string.sct), result);
                if (result == 6)
                    callback.onRequestFailed(context.getString(R.string.snr2), result);
                if (result == 4)
                    callback.onRequestFailed(context.getString(R.string.iurl), result);
                if (result == 1)
                    callback.onRequestFailed(context.getString(R.string.isres) + RESCODE, result);
                if (result == 5)
                    callback.onRequestFailed(context.getString(R.string.nipcyns), result);
                if (result == 7)
                    callback.onRequestFailed(context.getString(R.string.snr3), result);
                if (result == 8)
                    callback.onRequestFailed(context.getString(R.string.edcpda), result);
                if (result == 9)
                    callback.onRequestFailed(context.getString(R.string.isr), result);
                if (result == 10)
                    callback.onRequestFailed(context.getString(R.string.dfptl), result);
                if (result == 11)
                    callback.onRequestFailed(context.getString(R.string.pid), result);

                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }

                File file = new File(Constants.DWLPATH + "/" + downloadFileName);
                if (file.exists()) {
                    file.delete();
                }

                return;
            }

            if (!saveToFile && inputStream != null) {
                //callback.onRequestComplete(inputStream, mimeType);
                return;

            } else if (saveToFile && mimeType != null) {
				
				/*if(downloadFileName.contains("."))
				   downloadFileName = downloadFileName.substring(0, downloadFileName.indexOf("."));*/

                callback.onRequestComplete(requestUrl + "###" + downloadFileName, mimeType);
                return;
            }

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        callback.onRequestProgress(values);
    }

    /**
     * @param url - URL to establish connection
     * @return HttpURLConnection
     * @throws Exception if URL Malformed or SocketTimeout or Any other Exception.
     */
    @SuppressWarnings("rawtypes")
    static public HttpURLConnection getHTTPConnection(String url, Item headers)
            throws Exception {
        HttpURLConnection _conn = null;
        URL serverAddress = null;
        URL hostAddress = null;
        int socketExepCt = 0;
        int ExepCt = 0;
        int numRedirect = 0;

        TraceUtils.logE("Request API:", url);

        hostAddress = new URL(url);
        String host = "http://" + hostAddress.getHost() + "/";

        for (int i = 0; i <= 5; i++) {

            try {

                serverAddress = new URL(url);

                _conn = (HttpURLConnection) serverAddress.openConnection();
                if (_conn != null) {
                    _conn.setRequestMethod("GET");
                    _conn.setDoOutput(false);
                    _conn.setReadTimeout(30000);
                    _conn.setConnectTimeout(10000);
                    _conn.setInstanceFollowRedirects(false);

                    if (headers != null) {
                        Hashtable requestProperty = headers.getAllAttributes();
                        Enumeration keys = requestProperty.keys();
                        while (keys.hasMoreElements()) {
                            String key = keys.nextElement().toString();
                            String value = requestProperty.get(key).toString();
                            _conn.setRequestProperty(key, value);
                        }
                    }

                    RESCODE = 0;
                    _conn.connect();

                    RESCODE = _conn.getResponseCode();
                    if (RESCODE == HttpURLConnection.HTTP_OK) {
                        return _conn;
                    } else if (RESCODE == HttpURLConnection.HTTP_MOVED_TEMP
                            || RESCODE == HttpURLConnection.HTTP_MOVED_PERM) {
                        if (numRedirect > 15) {
                            _conn.disconnect();
                            _conn = null;
                            break;
                        }

                        numRedirect++;
                        i = 0;
                        url = _conn.getHeaderField("Location");
                        if (!url.startsWith("http")) {
                            url = host + url;
                        }

                        _conn.disconnect();
                        _conn = null;
                        continue;

                    } else {
                        _conn.disconnect();
                        _conn = null;
                    }
                }
            } catch (MalformedURLException me) {
                throw me;
            } catch (SocketTimeoutException se) {
                _conn = null;
                if (i >= 3)
                    throw se;
            } catch (SocketException s) {
                if (socketExepCt > 3) {
                    _conn = null;
                    throw s;
                }
                socketExepCt++;
                i = 0;
                continue;
            } catch (Exception e) {

                if (ExepCt > 3) {
                    _conn = null;
                    throw e;
                }
                ExepCt++;
                i = 0;
                continue;

            }
        }
        return null;
    }

    /**
     * checkConnectivity - Checks Whether Internet connection is available or
     * not.
     */
    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo net = manager.getActiveNetworkInfo();
        if (net != null) {
            if (net.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * urlEncode -
     *
     * @return String
     */
    public static String urlEncode(String sUrl) {
        int i = 0;
        String urlOK = "";
        while (i < sUrl.length()) {
            if (sUrl.charAt(i) == ' ') {
                urlOK = urlOK + "%20";
            } else {
                urlOK = urlOK + sUrl.charAt(i);
            }
            i++;
        }
        return (urlOK);
    }

    private void saveFile(String aMineType, long length) throws Exception {

        int bufferSize = 4096;

        //byte[] buffer = new byte[1024];
        byte[] buffer = new byte[bufferSize];
        int bytesRead = 0;
        long totalRead = 0;

        FileOutputStream outStream = null;

        File f = new File(Constants.DWLPATH);
        if (!f.exists()) {
            f.mkdirs();
        }

        try {

            downloadFileName = downloadFileName.replaceAll("[^a-zA-Z0-9_]+", "");

            downloadFileName = downloadFileName + "." + getFileExtension(aMineType);

            outStream = new FileOutputStream(Constants.DWLPATH + "/" + downloadFileName);

            while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) >= 0) {

                outStream.write(buffer, 0, bytesRead);

                totalRead += bytesRead;

                if (this.length > 0) {
                    Long[] progress = new Long[5];
                    progress[0] = (long) ((double) totalRead / (double) this.length * 100.0);
                    progress[1] = totalRead;
                    progress[2] = this.length;

                    double elapsedTimeSeconds = (System.currentTimeMillis() - mDownloadStartTime) / 1000.0;

                    // Compute the avg speed up to now
                    double bytesPerSecond = totalRead / elapsedTimeSeconds;

                    // How many bytes left?
                    long bytesRemaining = this.length - totalRead;

                    double timeRemainingSeconds;

                    if (bytesPerSecond > 0) {
                        timeRemainingSeconds = bytesRemaining / bytesPerSecond;
                    } else {
                        // Infinity so set to -1
                        timeRemainingSeconds = -1.0;
                    }

                    progress[3] = (long) (elapsedTimeSeconds * 1000);

                    progress[4] = (long) (timeRemainingSeconds * 1000);

                    //Log.e("progress[4]", progress[4]+"");

                    publishProgress(progress);
                }

                if (this.isCancelled()) {
                    if (conn != null)
                        conn.disconnect();
                    conn = null;
                    break;
                }

                //int totalReadInKB = totalRead / 1024;

                //publishProgress(totalReadInKB);
            }

            //byte[] bytes = bos.toByteArray();

            //downloadItem.setAttributeValue("data", bytes);

            //serializeData(downloadItem);
			
			/*if ((length - totalRead) > 50) {
				throw new SizeNotMatched("Size not matchedt");
			}*/

			/*if ((length - (totalRead / 1024)) > 50) {
				throw new SizeNotMatched("Size not matchedt");
			}*/

            outStream.close();
            buffer = null;
        } catch (Exception e) {

            throw e;

        } finally {
            if (outStream != null)
                outStream.close();
            outStream = null;
        }
    }

    /**
     * getFileExtension - This method return file extenion based on mime type.
     *
     * @return String
     */
    private String getFileExtension(String aType) {
        String type = "";
        type = aType;// image/gif;charset=ISO-8859-1
        if (type.contains("videotone/"))
            return type.substring(10, type.length());
        if (type.contains("video/x-ms-wmv"))
            return "wmv";
        if (type.contains("video/") || type.contains("audio/")
                || type.contains("image/")) {
            type = type.substring(6, type.length());
            if (type.contains(";"))
                type = type.substring(0, type.indexOf(";"));
            return type;
        } else if (type.contains("application/vnd.android.package-archive"))
            return "apk";

        return type;
    }

}