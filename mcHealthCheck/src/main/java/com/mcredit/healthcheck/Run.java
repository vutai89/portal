package com.mcredit.healthcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.gson.Gson;
import com.mcredit.model.App;
import com.mcredit.model.BuildResult;
import com.mcredit.model.Config;

public class Run {
	final static Logger _logger = Logger.getLogger(Run.class);
	private static String _CONFIG_FILE = "config.txt";
	private static String _STATUS_FILE = "running.txt";
	private static String _USER = "admin"; // username
	private static String _PASS = "570eff71e70e79f69622e1cdf3829511"; // password or API

	public static void main(String[] args) throws Throwable {
		run();
	}
	
	private static void run() throws Exception{
		try {
			if(isRunning()) {
				_logger.info(String.format("--------------- START CHECK IS RUNNING ---------------"));
				return;
			}
			
			start();
			App item = getConfig();
			PropertyConfigurator.configure(item.getLog4jPath());
			_logger.info("********************* START HEALTH CHECK ************************");
			
			if(item == null || item.getApps() == null || item.getApps().length == 0  )
				throw new Exception("CAN NOT GET CONFIG");
			
			BuildResult result = null;
			int index =0;
			for (int i = 0; i < item.getApps().length; i++) {
				Config sys = item.getApps()[i];
				_logger.info(String.format("--------------- START CHECK %s ---------------",sys.getName()));
				
				try {
					for (int j = 0; j < sys.getHeathCheckEndPoint().length; j++) {
						index =0;
						boolean isDown = check(sys.getHeathCheckEndPoint()[j]);
						if(isDown){
							/*result.getResult can be one of those value ["","SUCCESS","ABORTED"]
							 * if value is "" it means this job is building not yet to done.
							 * */
							result = getBuildStatus(sys.getBuildStatusEndPoint());
							_logger.info("Build Status: " + new Gson().toJson(result));
							
							/*Retry 100 times before breaking*/
							while(index <= 100 && result != null && ( result.getResult() == null || result.getResult() == "" || "".equals(result.getResult().trim()))){
								index++;
								Thread.sleep(5000);/*Sleep within 5s*/
								_logger.info("SLEPP WITHIN 5s TO WAIT FOR BUILDING DONE");
								result = getBuildStatus(sys.getBuildStatusEndPoint());
							}
								
							result = getBuildStatus(sys.getBuildStatusEndPoint()); 
							restart(sys.getRestartEndPoint());
							break;
						}
					}
				} catch (Exception e) {
					_logger.error(e);
				}
			}
		} catch (Throwable th) {
			_logger.error(th);
		}finally{
			stop();
		}
	}

	private static App getConfig() throws Exception {
		App item = null;
		String configContent = readFileContent(getCurrentPath() + File.separator + _CONFIG_FILE);
		
		item = new Gson().fromJson(configContent, App.class);
		if (item == null)
			throw new Exception("Config file is required.");

		return item;
	}

	private static String readFileContent(String path2File) throws IOException {

		BufferedReader bufferedReader = new BufferedReader(new FileReader(path2File));
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;

		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);

		return stringBuffer.toString();
	}

	private static String getCurrentPath() throws URISyntaxException {

		CodeSource codeSource = Run.class.getProtectionDomain().getCodeSource();
		File jarFile = new File(codeSource.getLocation().toURI().getPath());
		String jarDir = jarFile.getParentFile().getPath();

		return jarDir;
	}

	private static void restart(String endPoint) throws Throwable {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			_logger.info("RESTART ENPOINT: " + endPoint);
			URL url = new URL(endPoint);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String authStr = _USER + ":" + _PASS;
			String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
			conn.setRequestProperty("Authorization", "Basic " + encoding);

			if (conn.getResponseCode() == 201)
				br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
			if (conn.getResponseCode() != 201)
				br = new BufferedReader(new InputStreamReader(
						(conn.getErrorStream())));

			_logger.info("RESTART HTTP STATUS CODE:" + conn.getResponseCode());
			
			String output;
			while ((output = br.readLine()) != null) {
			}

			if (conn.getResponseCode() != 201) {
				_logger.error("RESTART ERROR");
				_logger.error(output);
			} else {
				_logger.info("RESTART SUCCESS");
			}

		} catch (Throwable e) {
			_logger.error(e);
		} finally {
			if (conn != null)
				conn.disconnect();

			if (br != null)
				br.close();
		}

	}

	private static boolean check(String endPoint) throws Throwable {
		HttpURLConnection conn = null;
		boolean isDown = false;
		try {

			URL url = new URL(endPoint);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			_logger.info("Http Status Code:" + conn.getResponseCode());

			if (conn.getResponseCode() != 200) {
				isDown = true;
				_logger.error(endPoint + " IS DOWN.");
			} else {
				_logger.info(endPoint + " IS UP.");
			}

		} catch (Throwable e) {
			_logger.error(endPoint + " IS DOWN.");
			_logger.error(e);
			isDown = true;
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return isDown;
	}
	
	private static BuildResult getBuildStatus(String endPoint) throws Throwable {
		HttpURLConnection conn = null;
		BuildResult result = null;
		_logger.info("Get Build Status: " + endPoint);
		try {

			URL url = new URL(endPoint);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String authStr = _USER + ":" + _PASS;
			String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
			conn.setRequestProperty("Authorization", "Basic " + encoding);

			_logger.info("Http Status Code:" + conn.getResponseCode());

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			result = new Gson().fromJson(response.toString(), BuildResult.class);

		} catch (Throwable e) {
			_logger.error(e.getMessage());
			_logger.error(e);
			
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return result;
	}
	
	private static void start() throws Exception{
		PrintWriter writer = new PrintWriter(getCurrentPath() + File.separator + _STATUS_FILE, "UTF-8");
		writer.println("Running");
		writer.close();
	}
	
	private static boolean isRunning() throws Exception{
		File f = new File(getCurrentPath() + File.separator + _STATUS_FILE);
		return f.exists();
	}
	
	private static void stop() throws Exception{
		File f = new File(getCurrentPath() + File.separator + _STATUS_FILE);
		f.deleteOnExit();
	}

}
