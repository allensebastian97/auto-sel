
package com.ventuno.playertest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

//import com.google.common.collect.Multiset.Entry;
import com.ventuno.result.CallDuration;
import com.ventuno.result.PlayerTestResult;
//import com.ventuno.result.TestResult;
import com.ventuno.util.VEN_CONSTANTS;

public class VenPlayerTester {

	// String to contain the video key
	static String videoKey;

	// to print the passed calls
	String keyPassed[] = new String[12];

	// to print the failed calls
	String keyFailed[] = new String[12];

	// to print the failed calls
	String keyDesc[] = new String[12];

	String kr[] = new String[12];

	static WebDriver driver;
	//	ProxyServer server;
	//Proxy proxy;
	
	public BrowserMobProxy proxy1;

	int iterations;
	int noOfKeys;

	
	private HashParser hParser;
	private PlayerTestResult tResult;
	
	public VenPlayerTester() {

		// Initialize parser & resulting structure
		iterations = 1;
		noOfKeys = 1;
		
		ArrayList<String> keys = new ArrayList<String>();
		keys.add(VideoKeys.videoKeys[0]);
		
		hParser = new HashParser();			
		tResult = new PlayerTestResult(iterations, keys);
		hParser.setTestResult(tResult);

	}
	
	public void test() {
		try {

			setProxy();

			for (int i = 0; i < noOfKeys; i++) {

				String vidkey = VideoKeys.videoKeys[i];
				performTest(i, vidkey);
				
			}
			
			tResult.aggregateResults();
			
		} catch (Exception ex) {
			System.out.println("failed to perform the test");
			ex.printStackTrace();
		}
	}

	
	public void validate() {
		try {

			VideoKeys keys = new VideoKeys();

			for (int i = 0; i < noOfKeys; i++) {

				String vidkey = VideoKeys.videoKeys[i];
				HashMap<String, TestFixture> videokeyFixtures = keys.getTestExpectation(vidkey);
				
				HashMap<String, CallDuration> videokeyResult = tResult.callDurations.get(i);
						
				mapsAreEqual(videokeyFixtures, videokeyResult);
				
				

			}

		} catch (Exception ex) {
			System.out.println("failed to perform the test");
			ex.printStackTrace();
		}
	}

	
	public boolean mapsAreEqual(HashMap<String, TestFixture> videokeyFixtures , HashMap<String, CallDuration> videokeyResult) {
		
		
		for (Map.Entry<String, TestFixture> entry : videokeyFixtures.entrySet()) {
			
			String key = entry.getKey();
			if (videokeyResult.containsKey(key)) {
				
				CallDuration duration =  videokeyResult.get(key);
				TestFixture fixture = videokeyFixtures.get(key);
				System.out.println("PASSED");
				
				if (duration.totalCalls != fixture.calls) {
					// Not working - 
					
					return false;
				}
			}
		}
	    return true;
	}

	
	public void performTest(int videoKeyPos, String vidkey) {
					try {
			
						for (int i = 0; i < iterations; i++) {
							createHTMLFile(vidkey);
							Thread.sleep(3000);
							setBrowserCapabilities();
							generateHARFile(vidkey, i);
			
							//parser.parseData(vidkey, i);
							hParser.parseData(vidkey,videoKeyPos, i);
			
							driver.close();
			
						}
					}
					catch (Exception e) {
						System.out.println("failed to create html file");
						e.printStackTrace();
					}
				}
			
	public void setProxy() throws Exception {
		
		// start the proxy
//		server = new ProxyServer(4435);
//		server.start();
//		server.setCaptureHeaders(true);
//		server.setCaptureContent(true);
//
//		proxy = server.seleniumProxy();
		
		// start the proxy
	    proxy1 = new BrowserMobProxyServer();
	    proxy1.start(0);

	    //get the Selenium proxy object - org.openqa.selenium.Proxy;
	    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy1);

	    // configure it as a desired capability
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
	    driver = new ChromeDriver(capabilities);
	    
	    proxy1.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

	    // create a new HAR with the label "seleniumeasy.com"
	    proxy1.newHar("test");
		
	}

	// method to set the browser capabilities for testing
	public void setBrowserCapabilities() {
		// set the desired capabilities of the browser in which the testing has to be done

//		DesiredCapabilities dc = DesiredCapabilities.chrome();
//		dc.setCapability(CapabilityType.PROXY, proxy1);
//		driver = new ChromeDriver(dc);
	}

	
	public void generateHARFile(String videokey, int n) {
		try {
			// create HAR feature for the sever
//			server.newHar("test");
			driver.get(VEN_CONSTANTS.webpage);

			Thread.sleep(VEN_CONSTANTS.PLAYER_PLAY_TIMEOUT);
			
			// set the HAR feature to the server
			net.lightbody.bmp.core.har.Har har = proxy1.getHar();

			// create a file stream object to write the HAR file in the specified path
			FileOutputStream fos = new FileOutputStream(VEN_CONSTANTS.harFilePath + videokey + "_" + n + ".txt");
			har.writeTo(fos);
			fos.close();
		}

		catch (Exception e) {
			System.out.println("Failed to create HAR File");
		}
	}

	public void createHTMLFile(String vk) throws IOException {
		File f = new File(VEN_CONSTANTS.htmlFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write("<!DOCTYPE html>" + "<html>" + "<head>" + "	<title></title>" + "</head>" + "<body>"
				+ "<div id=\"vt-video-player\"></div>"
				+ "<div id=\"testresult\" align=\"right\" style=\"position: absolute; top: 0; right: 0;\"></div>"
				+ "<script type=\"text/javascript\">" + "   var player;" + "   function getPlayerAPI( _player ){"
				+ "   	console.log(_player);" + "      player = _player;" + "   };" + "</script>"
				+ "<script type=\"text/javascript\">" + "window.__ventunoplayer = window.__ventunoplayer||[];"
				+ "window.__ventunoplayer.push({video_key: '" + vk
				+ "', holder_id: 'vt-video-player', player_type: 'vp', width:'405', height:'325', 'get_player_api' : getPlayerAPI });"
				+ "</script>"
				+ "<script type=\"text/javascript\" src=\"http://pl.ventunotech.com/plugins/cntplayer/ventunoSmartPlayer.js\"></script>"
				+ "</body>" + "</html>");
		bw.close();
	}

	public static void main(String[] args) throws IOException {
		
		System.setProperty("webdriver.chrome.driver", VEN_CONSTANTS.chromeDriverFilePath);
		VenPlayerTester playerTest = new VenPlayerTester();
		playerTest.test();
		
		playerTest.validate();
		
	}

}
