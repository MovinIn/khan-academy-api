package api.KhanAcademySandbox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	static {
		httpclient=HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
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
		System.out.println(responseBody);
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
		Scanner scanner = new Scanner(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8.name());
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
}
