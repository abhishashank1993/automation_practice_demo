package Libraries;

import SeleniumCore.CommonClass;

public class ShoppingPage extends HomePage{

	public boolean shopping() throws InterruptedException
	{
		
		boolean flag =true;
		
		Thread.sleep(2000);
		flag = page("Pyrimdcore_HomePage").element("indodesk").smartClick();
		if(!flag) {
			ErrDescription="Address button element not found";
			return flag;
			
		}
		Thread.sleep(2000);
		flag = page("Pyrimdcore_HomePage").element("womanbtn").smartClick();
		if(!flag) {
			ErrDescription="Woman button element not found";
			return flag;
			
		}
		Thread.sleep(2000);
		flag = page("Pyrimdcore_HomePage").element("Topsbtn").smartClick();
		if(!flag) {
			ErrDescription="Tops button element not found";
			return flag;
			
		}
		Thread.sleep(2000);
		flag = page("ContentCatalog_Images_Page").element("Fadedshortsleevstshirt").smartClick();
		if(!flag) {
			ErrDescription="Fade short sleevs T-shirt image element not found";
			return flag;
			
			
		}
			 
		return flag;
		
	}
}
