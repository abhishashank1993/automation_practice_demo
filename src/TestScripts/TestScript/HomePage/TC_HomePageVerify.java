package TestScript.HomePage;

import org.testng.Assert;
import org.testng.annotations.Test;

import Launcher.Launcher;
import Libraries.HomePage;
import Libraries.ShoppingPage;



public class TC_HomePageVerify extends ShoppingPage {
	String strStepDesc;
	String strActualResult;
	String ErrDescription;
    @Test
    public void verifyHomePageLaunch() throws InterruptedException {
        // Fetching URL from Common Values sheet via dicCommValues
        String url = Launcher.dicCommValues.get("Application URL");
        
        String name= dicTestData.get("Username");
        String password =dicTestData.get("Password");
        
        

        browser.launchApplication(url);
        strStepDesc = "open the browser and put the credenital and click the login button ";
				strActualResult = "open the browser successfully and login successfully";
				ErrDescription = "user name is not vailid";
        boolean flag= loginpage(name, password); 

        if (flag) {
			reporter.reportStep(strStepDesc, strActualResult, "Pass");
		} else {
			reporter.reportStep(strStepDesc, ErrDescription, "Fail");
			return;
		}
        flag = shopping(); 
        strStepDesc = "validate home page";
        strActualResult ="home page open successfully";
        if (flag) {
			reporter.reportStep(strStepDesc, strActualResult, "Pass");
		} else {
			reporter.reportStep(strStepDesc, ErrDescription, "Fail");
			return;
		} 		

    }
}
