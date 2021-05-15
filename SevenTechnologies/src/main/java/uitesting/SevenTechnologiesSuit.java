package uitesting;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;


public class SevenTechnologiesSuit {
	WebDriver driver;

	@BeforeMethod
	public void Launch() {
		System.setProperty("webdriver.chrome.driver", "C:/Users/Surya/eclipse-workspace/SevenTechnologies/ChromeDriver/chromedriver.exe");
		driver= new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();


	}
	@Test(enabled=false)
	public void imageStatusIdentifier() throws Exception{

		driver.navigate().to("http://the-internet.herokuapp.com/broken_images");
		Thread.sleep(5000);
		List<WebElement> images=driver.findElements(By.tagName("img"));	
		for(WebElement image:images) {
			String imageSrc=image.getAttribute("src");//To get the Base link/url for each image
			try {
				URL url=new URL(imageSrc);
				URLConnection urlConnection=url.openConnection();//To read the resource of referenced url object
				HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
				httpURLConnection.setConnectTimeout(5000);
				httpURLConnection.connect();
				if(httpURLConnection.getResponseCode() == 200) 
				{
					System.out.println( imageSrc + " >> " + httpURLConnection.getResponseCode() + " >> " +httpURLConnection.getResponseMessage()+"\r\n");
				}
				else
				{
					System.err.println(imageSrc + " >> " + httpURLConnection.getResponseCode() + " >> " +httpURLConnection.getResponseMessage()+"\r\n");
				}

				httpURLConnection.disconnect();
			} catch (Exception e) {
				
				System.err.println(imageSrc);

			}
		}
	}
	@Test(enabled=false)
	public void forgotPassword()
	{

		driver.navigate().to("http://the-internet.herokuapp.com/forgot_password");
		driver.findElement(By.id("form_submit")).click();

		// This will capture error message
		String actual_msg=driver.findElement(By.xpath("//h1")).getText();

		// Store message in variable
		String expect="Internal Server Error";

		// Verify error message
		Assert.assertEquals(actual_msg, expect);

	}
	@Test(enabled=false)
	public void AssertDummyPassword()
	{

		driver.navigate().to("http://the-internet.herokuapp.com/login");
		driver.findElement(By.id("username")).sendKeys("tomsmith");
		driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();

		// This will capture error message
		String actual_msg=driver.findElement(By.xpath("//h4")).getText();
		Reporter.log(actual_msg);
		String expect="Welcome to the Secure Area. When you are done click logout below.";

		// Verify error message
		Assert.assertEquals(actual_msg, expect);

	}
	@Test(enabled=false)
	public void AssertAlphabetEntry() {
		driver.navigate().to("http://the-internet.herokuapp.com/inputs");
		String expect="Test";
		driver.findElement(By.xpath("//input[@type='number']")).sendKeys(expect);
		String actual_msg=driver.findElement(By.xpath("//input[@type='number']")).getText();
		Assert.assertEquals(actual_msg, expect);
		
		
	}
	@Test(enabled=false)
	public void SortUITable() {
		driver.navigate().to("http://the-internet.herokuapp.com/tables");
		driver.findElement(By.xpath("//*[@id='table1']/thead/tr/th[4]/span")).click();
		driver.findElement(By.xpath("//*[@id='table2']/thead/tr/th[4]/span")).click();
	}
	@Test(enabled=true)
	public void Notification() {
		driver.navigate().to("http://the-internet.herokuapp.com/notification_message_rendered");
		driver.findElement(By.xpath("//a[text()='Click here']")).click();
		String initial=driver.findElement(By.xpath("//*[@id='flash']")).getText();
		while(initial.contains("unsuccesful")) {
			driver.findElement(By.xpath("//a[text()='Click here']")).click();
			initial=driver.findElement(By.xpath("//*[@id='flash']")).getText();
			if (initial.contains("Action succesful")) {
                break;
            }
			
		}
		
		Assert.assertTrue(initial.contains("Action successful"));
	}

	@AfterMethod(enabled=false)
	public void close() {
		driver.quit();
	}
}
