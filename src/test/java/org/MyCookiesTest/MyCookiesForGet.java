package org.MyCookiesTest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MyCookiesForGet {
	private String url;
	private ResourceBundle bundle;
	//用来存取cookie信息的变量
	private BasicCookieStore cookieStore;
	@BeforeTest
	public void BeforeTest() {
		//从Appliacation.properties中获取数据
		bundle = ResourceBundle.getBundle("resources/Application");
		String testurl = bundle.getString("test.url");
		String uri = bundle.getString("test.get.with.cookies.uri");
		//拼接
		this.url=testurl+uri;
		System.out.println(url);
	}
	@Test
	public void cookiesForGetTest() throws Exception {
		 cookieStore = new BasicCookieStore();
		 //创建HttpClient(用HttpClients)
		 CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		 //创建HttpGet请求，传入url
		 HttpGet httpGet = new HttpGet(url);
		 //HttpClient执行Get请求
		 CloseableHttpResponse response = httpClient.execute(httpGet);
		 String reString = EntityUtils.toString(response.getEntity());
		 System.out.println(reString);
		 List<Cookie> cookies = cookieStore.getCookies();
		 for (Cookie cookie : cookies) {
			String name = cookie.getName();
			String value = cookie.getValue();
			System.out.println("Cookie Name="+name+";Cookie Value="+value);
		}
		
	}
	/**
	 * 
	 * 设置依赖
	 * @throws Exception
	 */
	@Test(dependsOnMethods= {"cookiesForGetTest"})
	public void getWithCookies() throws Exception{
		//从配置文件获取数据
		String uri = bundle.getString("test.get.with.cookies");
		String testurl = bundle.getString("test.url");
		String gWCUrl=testurl+uri;
		System.out.println(gWCUrl);
		//创建BasicCookieStore
		BasicCookieStore basicCookieStore = new BasicCookieStore();
		//创建BasicClientCookie并设置cookie（第一种方法）
		BasicClientCookie cookie = new BasicClientCookie("login","true");
		//设置cookie作用域
		cookie.setDomain("localhost");
		//设置cookie路径
		cookie.setPath("/");
		//将BasicClientCookie加入basicCookieStore
		basicCookieStore.addCookie(cookie);
		CloseableHttpClient buildclient = HttpClients.custom().setDefaultCookieStore(basicCookieStore).build();
		HttpGet httpGet = new HttpGet(gWCUrl);
		//设置Cookie的第二种方法
		//httpGet.setHeader("Cookie","login=true;base=localhost");
		CloseableHttpResponse response = buildclient.execute(httpGet);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
	}
}
