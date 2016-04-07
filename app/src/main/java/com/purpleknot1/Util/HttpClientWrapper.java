//package com.purpleknot1.Util;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//
//
//import com.purpleknot1.DTO.RequestType;
//import com.purpleknot1.DTO.ServiceListenerType;
//import com.purpleknot1.DTO.ServiceListenerType;
//
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URLEncodedUtils;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.HttpContext;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.SocketException;
//import java.net.SocketTimeoutException;
//import java.net.URI;
//import java.util.List;
//
//public class HttpClientWrapper {
//
//
//    /*Used to send http request to FPS server and return the data back*/
//    public void sendRequest(final String requestData, final Bundle extra,
//                            final ServiceListenerType what, final Handler messageHandler,
//                            final RequestType method, final List<NameValuePair> params , final Context context) {
//
//
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                BufferedReader in = null;
//                Message msg = Message.obtain();
//                msg.obj = what;
//                try {
//                   // String url = context.getString(R.string.application_url) + requestData;
//                    String url =  ApplicationConstants.url_server + requestData;
//
//                    Log.e("url 1 --",url);
//
//                    URI website = new URI(url);
//                    Log.e("Url", url);
//                    HttpResponse response = requestType(website, method, params);
//                    in = new BufferedReader(new InputStreamReader(response
//                            .getEntity().getContent()));
//                    StringBuffer sb = new StringBuffer("");
//                    String l;
//                    String nl = System.getProperty("line.separator");
//                    while ((l = in.readLine()) != null) {
//                        sb.append(l + nl);
//                    }
//                    in.close();
//                    String responseData = sb.toString();
//                    Log.e("Response", responseData);
//                    Bundle b = new Bundle();
//                    if (extra != null)
//                        b.putAll(extra);
//                    if (responseData.trim().length() != 0) {
//                        b.putString(FPSDBConstants.RESPONSE_DATA, responseData);
//                    } else {
//                        msg.obj = ServiceListenerType.ERROR_MSG;
//                        b.putString(FPSDBConstants.RESPONSE_DATA, "Empty Data");
//                    }
//                    msg.setData(b);
//                } catch (SocketTimeoutException e) {
//                    Log.e("SendRequest", "SocketTimeoutException", e);
//                    msg.obj = ServiceListenerType.ERROR_MSG;
//                    Bundle b = new Bundle();
//                    if (extra != null)
//                        b.putAll(extra);
//                    b.putString(FPSDBConstants.RESPONSE_DATA,
//                            "Cannot establish connection to server. Please try again later.");
//                    msg.setData(b);
//                } catch (SocketException e) {
//                    Log.e("SendRequest", "SocketException", e);
//                    msg.obj = ServiceListenerType.ERROR_MSG;
//                    Bundle b = new Bundle();
//                    if (extra != null)
//                        b.putAll(extra);
//                    b.putString(FPSDBConstants.RESPONSE_DATA,
//                            context.getString(R.string.connectionError));
//                    msg.setData(b);
//                } catch (IOException e) {
//                    Log.e("SendRequest", "IOException", e);
//                    msg.obj = ServiceListenerType.ERROR_MSG;
//                    Bundle b = new Bundle();
//                    if (extra != null)
//                        b.putAll(extra);
//                    b.putString(FPSDBConstants.RESPONSE_DATA, ""
//                            + "Internal Error.Please Try Again");
//                    msg.setData(b);
//                } catch (Exception e) {
//                    Log.e("SendRequest", "Exception", e);
//                    msg.obj = ServiceListenerType.ERROR_MSG;
//                    Bundle b = new Bundle();
//                    if (extra != null)
//                        b.putAll(extra);
//                    b.putString(FPSDBConstants.RESPONSE_DATA, context.getString(R.string.connectionRefused));
//                    msg.setData(b);
//                } finally {
//                    try {
//                        if (in != null) {
//                            in.close();
//                        }
//                    } catch (Exception e) {
//                        Log.e("HTTP", "Error", e);
//                    }
//                    messageHandler.sendMessage(msg);
//                }
//
//            }
//        }.start();
//
//    }
//
//    /*return http GET,POST and PUT method using parameters*/
//    private HttpResponse requestType(URI website, RequestType method,
//                                     List<NameValuePair> params) {
//        try {
//            HttpParams httpParameters = new BasicHttpParams();
//            int timeoutConnection = 50000;
//            HttpConnectionParams.setConnectionTimeout(httpParameters,
//                    timeoutConnection);
//            int timeoutSocket = 50000;
//            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//            HttpClient client = new DefaultHttpClient(httpParameters);
//
//           // Log.e("Cookie", "JSESSIONID=" + SessionId.getInstance().getSessionId());
//            switch (method) {
//                case POST:
//
//                    DefaultHttpClient httpClient = new DefaultHttpClient();
//                    HttpPost httpPost = new HttpPost(website);
//                    httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//                    HttpResponse httpResponse = httpClient.execute(httpPost);
//                   // HttpEntity httpEntity = httpResponse.getEntity();
//
//                    //-----------------
//
//
//                    return httpResponse;
//                case PUT:
//                    DefaultHttpClient puthttpClient = new DefaultHttpClient();
//                    String paramString = URLEncodedUtils.format(params, "utf-8");
//                    String website1=website.toString();
//                    website1 += "?" + paramString;
//                    HttpGet httpGet = new HttpGet(website);
//
//                    HttpResponse puthttpResponse = puthttpClient.execute(httpGet);
//
//                    return puthttpResponse;
//                case GET:
//                    HttpGet getRequest = new HttpGet();
//                    // Create local HTTP context
//                    HttpContext localContext = new BasicHttpContext();
//                    //HttpParams httpParams=new BasicHttpParams();
//                    // httpParams.setParameter("Content-Type","application/json");
//                    // getRequest.setParams(httpParams);
//                    getRequest.addHeader("Content-Type", "application/json");
//            //        getRequest.addHeader("Cookie", "JSESSIONID=" + SessionId.getInstance().getSessionId());
//
//                    // Bind custom cookie store to the local context
//                    //getRequest.setHeader("Content-Type", "application/json");
//                   // getRequest.setHeader("Cookie", "JSESSIONID:" + SessionId.getInstance().getSessionId());
//                    Log.e("GET", "GET Method");
//             //       Log.e("SessionId", "" + SessionId.getInstance().getSessionId());
//                    getRequest.setURI(website);
//                    return client.execute(getRequest);
//
//            }
//
//        } catch (Exception e) {
//            Log.e("Error", e.toString(), e);
//        }
//        return null;
//    }
//
//}