package maventest;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import okhttp3.internal.http2.Http2Connection;

public class BrokenLinks {

	public static void main(String[] args) {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("https://www.amazon.in/");

		List<WebElement> links = driver.findElements(By.tagName("a"));
		System.out.println(links.size());

		List<String> urlList = new ArrayList<String>();
		for (WebElement e : links) {
			String url = e.getAttribute("href");
			urlList.add(url);
			// checkBrokenLinks(url);
		}
		long starttime = System.currentTimeMillis();
		urlList.parallelStream().forEach(e -> checkBrokenLinks(e));
		long endtime = System.currentTimeMillis();

		System.out.println("total time taken:   " + (endtime - starttime));

		driver.quit();
	}

	public static void checkBrokenLinks(String linkUrl) {

		try {
			URL url = new URL(linkUrl);
			HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setConnectTimeout(5000);
			httpurlconnection.connect();

			if (httpurlconnection.getResponseCode() >= 400) {
				System.out.println(linkUrl + "----->" + httpurlconnection.getResponseMessage() + "is a broken link");
			} else {
				System.out.println(linkUrl + "--->" + httpurlconnection.getResponseMessage());
			}
		}

		catch (Exception e) {

		}

	}

}
