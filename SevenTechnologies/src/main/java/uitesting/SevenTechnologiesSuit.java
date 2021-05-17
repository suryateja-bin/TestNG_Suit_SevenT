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
	//The before method will be executed before every test case
	@BeforeMethod(enabled=true)
	public void Launch() {
		System.setProperty("webdriver.chrome.driver", "C:/Users/Surya/eclipse-workspace/SevenTechnologies/ChromeDriver/chromedriver.exe");//chrome driver location which is stored in the maven project
		driver= new ChromeDriver();//object for chrome driver
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);//page time out
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);//implicit wait
		driver.manage().deleteAllCookies();//to delete the cookies 
		driver.manage().window().maximize();//to maximize the window


	}
	@Test(priority = 1)
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
				httpURLConnection.setConnectTimeout(5000);//to avoid the delay between http connection
				httpURLConnection.connect();
				//to get the response code
				if(httpURLConnection.getResponseCode() == 200) 
				{
					//To print the image with response code 200
					System.out.println( imageSrc + " >> " + httpURLConnection.getResponseCode() + " >> " +httpURLConnection.getResponseMessage()+"\r\n");
				}
				else
				{
					//To print the image with response code 400
					System.err.println(imageSrc + " >> " + httpURLConnection.getResponseCode() + " >> " +httpURLConnection.getResponseMessage()+"\r\n");
				}

				httpURLConnection.disconnect();
			} catch (Exception e)
			{
				
				System.err.println(imageSrc);

			}
		}
	}
	@Test(priority = 2)
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
	@Test(priority = 3)
	public void AssertDummyPassword()
	{

		driver.navigate().to("http://the-internet.herokuapp.com/login");
		driver.findElement(By.id("username")).sendKeys("tomsmith");//the dummy username provided in the UI
		driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");//the dummy passcode provided in the UI
		driver.findElement(By.xpath("//button[@type='submit']")).click();//to submit the form

		// This will capture the message after successful login
		String actual_msg=driver.findElement(By.xpath("//h4")).getText();
		Reporter.log(actual_msg);
		String expect="Welcome to the Secure Area. When you are done click logout below.";//actual message on UI

		// Verify the message
		Assert.assertEquals(actual_msg, expect);

	}
	@Test(priority = 4)
	public void AssertAlphabetEntry() {
		driver.navigate().to("http://the-internet.herokuapp.com/inputs");
		String expect="Test";
		driver.findElement(By.xpath("//input[@type='number']")).sendKeys(expect);//to enter the alphabet message
		String actual_msg=driver.findElement(By.xpath("//input[@type='number']")).getText();//to get the message entered 
		Assert.assertEquals(actual_msg, expect);//failed after assertion 
		
		
	}
	@Test(priority = 5)
	public void SortUITable() {
		driver.navigate().to("http://the-internet.herokuapp.com/tables");
		driver.findElement(By.xpath("//*[@id='table1']/thead/tr/th[4]/span")).click();//to sort the table1
		driver.findElement(By.xpath("//*[@id='table2']/thead/tr/th[4]/span")).click();//to sort the table2
	}
	@Test(priority = 6)
	public void Notification() {
		driver.navigate().to("http://the-internet.herokuapp.com/notification_message_rendered");
		driver.findElement(By.xpath("//a[text()='Click here']")).click();//click on clickhere button
		String initial=driver.findElement(By.xpath("//*[@id='flash']")).getText();//get the flash message after clicking the clickhere
		//The loop will be activated only if the flash message is unsuccessful
		while(initial.contains("unsuccesful")) {
			driver.findElement(By.xpath("//a[text()='Click here']")).click();
			initial=driver.findElement(By.xpath("//*[@id='flash']")).getText();
			if (initial.contains("Action succesful")) {
                //will be terminated once the message is Action succefull
				break;
            }
			
		}
		
		Assert.assertTrue(initial.contains("Action successful"));//assertion will be passed only if message is successfull
	}
	//The after method will be executed after every test case
	@AfterMethod(enabled=true)
	public void close() {
		driver.quit();
	}
}
