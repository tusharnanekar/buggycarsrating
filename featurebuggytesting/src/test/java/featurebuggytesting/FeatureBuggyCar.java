package featurebuggytesting;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class FeatureBuggyCar {
	
	WebDriver driver;
	String userName = "Tushar6";
	String psswrd = "Testing@123";
	String modelCar= "Diablo";
	String colName = "Rank";
	@BeforeSuite(alwaysRun=true)
	public void driverSetup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		
	}
	
	@AfterSuite(alwaysRun=false)
	public void driverClose() {
		driver.quit();
	}
	
	@BeforeTest(alwaysRun=true)
	public void LaunchURL() {
		driver.get("https://buggy.justtestit.org/");
		driver.manage().window().maximize();
	}
	
	@Test(priority=0, groups="feature")
	public void BCR_Register() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.findElement(By.linkText("Register")).click();
		String actualURL = driver.getCurrentUrl();
		String expectedURL= "https://buggy.justtestit.org/register";
		Assert.assertEquals(actualURL,expectedURL);
		driver.findElement(By.xpath("//input[@id='username']")).sendKeys(userName); 
		driver.findElement(By.xpath("//input[@id='firstName']")).sendKeys("Tushar"); 
		driver.findElement(By.xpath("//input[@id='lastName']")).sendKeys("Nanekar"); 
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys(psswrd); 
		driver.findElement(By.xpath("//input[@id='confirmPassword']")).sendKeys(psswrd);
			if(driver.findElement(By.xpath("//button[text()='Register']")).isEnabled()) {
				driver.findElement(By.xpath("//button[text()='Register']")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'result alert alert')]")));
				String regMsg = driver.findElement(By.xpath("//*[contains(@class,'result alert alert')]")).getText();
				Assert.assertEquals(regMsg,"Registration is successful");
				System.out.println("Registration message" + regMsg);
			}else {
				System.out.println("Please check the fields entered");
				Assert.fail();
			}
	}
	
	@Test(priority=1, groups="feature", enabled=true)
	public void BCR_Login() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.findElement(By.name("login")).sendKeys(userName);
		driver.findElement(By.name("password")).sendKeys(psswrd);
		driver.findElement(By.xpath("//button[text()='Login']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Logout']")));
		if (driver.findElements(By.xpath("//a[text()='Logout']")).size() > 0) {
			System.out.println("Login Successful");
			driver.findElement(By.xpath("//a[normalize-space()='Buggy Rating']")).click();
		}else{
			Assert.fail("Login failed");
		};
	}
		
	@Test(priority=4, groups="feature", enabled=true)
	public void BCR_Logout() {
		
		if (driver.findElements(By.xpath("//a[text()='Logout']")).size() > 0) {
			driver.findElement(By.xpath("//a[text()='Logout']")).click();
			System.out.println("Logout Successful");
		}else{
			System.out.println("Logout unSuccessful : Pleae check if correct page is opened");
		};
	}
		
	@Test(priority=2, groups="feature", enabled=true)
	public void BCR_VotingPopularModel() {
		//BCR_Login();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Popular Model']/following-sibling::a")));
			WebElement modelVotingLink = driver.findElement(By.xpath("//h2[text()='Popular Model']/following-sibling::a"));
			
			if (modelVotingLink.isDisplayed()) {
				modelVotingLink.click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='card-block']//h4//strong")));
				if(driver.findElements(By.xpath("//textarea[@id='comment']")).size()>0) {
					int existingVote = Integer.parseInt(driver.findElement(By.xpath("//div[@class='card-block']//h4//strong")).getText());
					driver.findElement(By.xpath("//textarea[@id='comment']")).sendKeys("Testing Vote Tushar");
					driver.findElement(By.xpath("//button[text()='Vote!']")).click();
					//wait
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='card-block']//p[@class='card-text']")));
					WebElement votingMessage = driver.findElement(By.xpath("//div[@class='card-block']//p[@class='card-text']"));
					if (votingMessage.isDisplayed()) {
					String votemsg = votingMessage.getText();
					int currentVote = Integer.parseInt(driver.findElement(By.xpath("//div[@class='card-block']//h4//strong")).getText());
					int voteInc = currentVote-existingVote;
					Assert.assertEquals(currentVote,existingVote+voteInc);
						System.out.println(votemsg);
					}
						
				}else {
					WebElement votingMessage = driver.findElement(By.xpath("//div[@class='card-block']//p[@class='card-text']"));
					if(votingMessage.isDisplayed()) {
						String votemsg = votingMessage.getText();
						System.out.println("Voting not available :" +votemsg);
					}
				}
			}else{
				System.out.println("Popular Model Crad is not visible to vote");
				Assert.fail("Voting Not Done");
			};
		
		
	}
	
	@Test(priority=3, groups="feature", enabled=true)
	public void ModelDetails() throws InterruptedException {
		//BCR_Login();
		driver.findElement(By.xpath("//a[normalize-space()='Buggy Rating']")).click();
		String getValue = getColForModel(modelCar,colName);
		System.out.println("Value found:" + getValue);
	}
	
	public String getColForModel(String modelName,String ColName) throws InterruptedException {
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		String ColValue = "Not Found";
		String headName = "";
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Overall Rating']/following-sibling::a")));
		WebElement allCars = driver.findElement(By.xpath("//h2[text()='Overall Rating']/following-sibling::a"));
		if (allCars.isDisplayed()) {
			allCars.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='cars table table-hover']")));
		String pageNbr = driver.findElement(By.xpath("//div[@class='row']//div[@class='pull-xs-right']")).getText();
		String[] totPage = pageNbr.split("of");
		int totpageN = Integer.parseInt((totPage[1]).trim());
		boolean strFlag = false;
		boolean strFlagModel = false;
		int i=1;
		for(int pages=1;pages<=totpageN;pages++) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='cars table table-hover']")));
		WebElement table = driver.findElement(By.xpath("//table[@class='cars table table-hover']"));
		List<WebElement> tableHeaders = table.findElements(By.tagName("th"));
			
		if(!strFlag) {
		for(i =1; i<tableHeaders.size()-1;i++) {	
			headName = tableHeaders.get(i).getText();
			if(headName.equals(ColName)) {
				System.out.println("Column Found");
				strFlag = true;
				break;
			}
		}
		System.out.println(i);
		}
			
		List<WebElement> tablecontents = table.findElements(By.tagName("tr"));
		for(int j =1; j<tablecontents.size();j++) {
			
			List<WebElement> tableCols = tablecontents.get(j).findElements(By.tagName("td"));
			String modelMatch = tableCols.get(2).getText();
			if(modelMatch.equals(modelName)) {
				System.out.println("Model Found");
				ColValue = tableCols.get(i).getText();
				System.out.println(ColValue);
				strFlagModel = true;
				break;
			}
		}
		if(strFlagModel) {
			break;
		}
		driver.findElement(By.xpath("//div[@class='pull-xs-right']/a/following::a[@class='btn']")).click();
		Thread.sleep(3000);
		}
		
		}
		return ColValue;
	}
}
