package api.KhanAcademySandbox;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.async.methods.SimpleBody;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public abstract class HttpSender {
	private static CloseableHttpClient httpclient;
	private static CloseableHttpAsyncClient asyncclient = HttpAsyncClients.custom().build();
	static {
		httpclient=HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
		asyncclient.start();
	}
	
	public static ResponseHandler<String> getDefaultResponseHandler() {
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			
		     @Override
		     public String handleResponse(
		             final HttpResponse response) throws ClientProtocolException, IOException {
		         int status = response.getStatusLine().getStatusCode();
		         if (status >= 200 && status < 300) {
		             HttpEntity entity = response.getEntity();
		             return entity != null ? EntityUtils.toString(entity) : null;
		         } else {
		             throw new ClientProtocolException("Unexpected response status: " + status);
		         }
		     }
		
		};
		return responseHandler;
	}
	
	public static String httpGet(String url) throws ClientProtocolException, IOException {
		HttpGet get=new HttpGet(url);
		String responseBody = httpclient.execute(get, getDefaultResponseHandler());
		return responseBody;
    }
	public static String httpPost(String url,HttpEntity h,Header[]...head) throws ClientProtocolException, IOException {
		HttpPost post=new HttpPost(url);
		post.setEntity(h);
		if(head.length>0)
		    post.setHeaders(head[0]);
		else
			post.setHeaders(getDefaultHeaders());
		CloseableHttpResponse httpResponse=httpclient.execute(post);
		System.out.println("done.");
		return inputStreamToString(httpResponse.getEntity().getContent());
	}
	
	public static Future<SimpleHttpResponse> asyncPost(String url,byte[] body,org.apache.hc.core5.http.Header[]...head) throws ClientProtocolException, IOException {
		SimpleHttpRequest post=SimpleRequestBuilder.post(url).build();
		post.setBody(body, ContentType.APPLICATION_JSON);
		if(head.length>0)
				post.setHeaders(head[0]);
		else
			post.setHeaders(getDefaultAsyncHeaders());
		Future<SimpleHttpResponse> httpResponse=asyncclient.execute(post,null);
		return httpResponse;
	}
	
	public static String inputStreamToString(InputStream i) {
		Scanner scanner = new Scanner(i, StandardCharsets.UTF_8.name());
		if(!scanner.hasNext()) {
			scanner.close();
			throw new IllegalArgumentException("no data returned");
		}
		String content=scanner.useDelimiter("\\A").next();
		scanner.close();
		return content;
	}
	
	public static Header[] getDefaultHeaders() {
		Header[] h=new Header[3];
		h[0]=new BasicHeader("Content-Type","application/json");
		h[1]=new BasicHeader("Cookie",User.EMPTY_COOKIE);
		h[2]=new BasicHeader("X-Ka-Fkey",User.FKEY);
		return h;
	}
	
	public static org.apache.hc.core5.http.Header[] getDefaultAsyncHeaders() {
		org.apache.hc.core5.http.Header[] h=new org.apache.hc.core5.http.Header[3];
		h[0]=new org.apache.hc.core5.http.message.BasicHeader("Content-Type","application/json");
		h[1]=new org.apache.hc.core5.http.message.BasicHeader("Cookie",User.EMPTY_COOKIE);
		h[2]=new org.apache.hc.core5.http.message.BasicHeader("X-Ka-Fkey",User.FKEY);
		return h;
	}
}
