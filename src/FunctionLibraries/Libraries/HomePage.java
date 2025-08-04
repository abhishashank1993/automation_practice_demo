package Libraries;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import SeleniumCore.CommonClass;


public class HomePage extends CommonClass 
{
	

	public boolean loginpage(String username, String password) throws InterruptedException
	{
		
		boolean flag =true;
		
		flag = page("Pyrimdcore_HomePage").element("TextBox_username").type(username);
		if(!flag) {
			ErrDescription="user name is not vailid";
			return flag;
			
		}
		Thread.sleep(2000);
		flag = page("Pyrimdcore_HomePage").element("Textpass_Password").type(password);
		if(!flag) {
			ErrDescription="Password name is not vailid";
			return flag;
			
		}
		Thread.sleep(2000);
		flag = page("Pyrimdcore_HomePage").element("Button_Login").smartClick();
		if(!flag) {
			ErrDescription="login button element not found";
			return flag;
			
		}

			 
		return flag;
		
	}
}
