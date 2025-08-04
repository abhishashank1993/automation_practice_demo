
package Launcher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.TestNG;
import org.testng.collections.Lists;

import Comm.Email;
import Comm.MsTeams;
import Comm.Slack;
import DataLibs.Excel;
import Reporter.Report;
import Utils.CreateTestNGFile;
import Utils.ReadProperty;


public class Launcher extends LauncherBase
{
	static Logger logger = Logger.getLogger(Launcher.class);
	public static void main(String[] args) throws Exception
	{
		SimpleDateFormat  dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		SimpleDateFormat  dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
		Date date = new Date();

		boolean blnflag=false;

		Excel excelRead = new Excel();

		CreateTestNGFile testng = new CreateTestNGFile();

		Report report = new Report(null,null);

		//@ Initialize log4j properties
		PropertyConfigurator.configure("src/reportingEngine/Reporter/log4j.properties");

		logger.info("Automation execution begins");

		//@ Read Property File
		ReadProperty.propertyFile("src/dataEngine/DataFiles/framework-config.properties");
		if (ReadProperty.dictProjectVar.size() == 0)
		{
			logger.error("Unable to read data from framework config property file");
			return ;
		}
		// Creating Hashmap of config file
		dicConfig = excelRead.exlDictionary(ReadProperty.dictProjectVar.get("config") + "Config.xlsx","Config", 1, 2);
		if (dicConfig.size() == 0)
		{
			logger.error("Unable to read data from Config.xlsx file");
			return ;
		}
		//@ create project execution date and time
		LauncherBase.dicConfig.put("ProjectStartTime", dateFormat.format(date));
		LauncherBase.dicConfig.put("ProjectStartDateTime", dateTimeFormat.format(date));

		//@ Report Initialization
		report.intializeProject();

		logger.info("Fetching data from Test Suite file");
		// Reading Suite(s) file and creating Hashmap
		dicTestSuite = excelRead.exlReadTestSuite(dicConfig.get("Test Suite File Name"));
		if (dicTestSuite.size() == 0)
		{
			logger.error("Unable to read data from Test Suite file, or none of the test case is marked as yes for execution");
			return;
		}
		logger.info("Preparing TestNG XML file");
		// Creating TestNG xml
		blnflag = testng.createTestNGFile(dicConfig,dicTestSuite,false);

		excelRead.updateExcel("testsuitereset","","");
		logger.info("Fetching data from Test Data file common values sheet");

		// Creating Hashmap of Common values sheet
		dicCommValues = excelRead.exlDictionary(ReadProperty.dictProjectVar.get("testdata")+ dicConfig.get("Test Data File"),"Common Values", 1, 2);
		//Get the Raw data for Report

		///@ New Report//////////
		Report.initializeNewReport();
		try
		{
			if(blnflag)
			{
				TestNG testngsuite = new TestNG();
				File file=new File(ReadProperty.dictProjectVar.get("TestNG"));
				List<String> suites = Lists.newArrayList();
				String files[] = file.list();
				// Adding all xmls to Suite
				for(String suiteFile : files)
				{
					suites.add(ReadProperty.dictProjectVar.get("TestNG")+suiteFile);
				}
				if(suites.size()>=1)
				{
					// Start execution
					//@
					try
					{
						testngsuite.setTestSuites(suites);
						testngsuite.run();
						date = new Date();
						LauncherBase.dicConfig.put("ProjectEndTime", dateFormat.format(date));
						LauncherBase.dicConfig.put("ProjectEndDateTime", dateTimeFormat.format(date));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					logger.error(String.format("No testng's xml file found"));
					blnflag=false;
				}

				Report.newReport();
				Report.reportDicProcess();
				report.createSummaryReportData();
			}
			else
			{
				logger.error(String.format("Issue while creating project default configuration"));
			}
			///@ End New Report/////////
			Report.EndNewReport();

			//@ Send report to mail address
			if(dicConfig.get("Send Summary Email").equalsIgnoreCase("Yes"))
			{
				Email sendmail= new Email();
				sendmail.sendEmail();
			}

			//@ Send report to communication channel
			if(dicConfig.get("Communication Channel").equalsIgnoreCase("Slack"))
			{
				Slack sendMessage= new Slack();
				sendMessage.sendSlackMessage();
			}
			else if(dicConfig.get("Communication Channel").equalsIgnoreCase("MS Teams"))
			{
				MsTeams sendMessage = new MsTeams();
				sendMessage.sendTeamMessage();
			}

		}
		catch(Exception e)
		{
			Report.newReport();
			Report.reportDicProcess();
			report.createSummaryReportData();
			logger.error(String.format("Issue while executing testng.\n Error is :%s",e.getMessage()));
			e.printStackTrace();
		}
		System.exit(0);
	}
}