package org.commoncrawl.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

public class CountWebComponentFromWARCFile {

	public static String extractURL(String url) {
		String result = "";
		if (url.length() == 0)
			return null;
		if (url.indexOf("http") != -1) {
			Pattern regex = Pattern.compile("^https?://[^/]+");
			Matcher regexMatcher = regex.matcher(url);
			if (regexMatcher.find()) {
				for (int i = 0; i <= regexMatcher.groupCount(); i++) {
					result += " " + regexMatcher.group(i);
				}
			}
			return result.replaceAll("https://", "");
		}
		return null;
	}

	public static String extractIPAddress(String sentence) {
		String result = "";
		if (sentence.length() == 0)
			return null;
		if (sentence.indexOf("WARC-IP-Address") != -1) {
			Pattern regex = Pattern.compile("\"WARC-IP-Address\":\"(.*?)\"");
			Matcher regexMatcher = regex.matcher(sentence);
			if (regexMatcher.find()) {
				result = result + regexMatcher.group(1);
			}
			return result;
		}
		return null;
	}
	
	public static String getCountryGivenIP(String ip) {
		String country = "";
		// build lookupService to look for country by given IP address
		File dbfile = new File("E:\\Brandeis_Study_file\\Indy sty"
				+ "\\210_workspace\\Common Crawl WARC Examples"
				+ "\\cc-warc-examples-master\\src\\resources\\GeoLiteCity.dat");
		try {
			LookupService lookupService = new LookupService(dbfile,
					LookupService.GEOIP_MEMORY_CACHE);
			Location location = lookupService.getLocation(ip);
			country = location.countryName;
			lookupService.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return country;
	}
	
	public static List<String> getImportCSS(String sentence2){
		List<String> result = new ArrayList<>();
		//String result = "";
		if(sentence2.length() == 0) return null;
		if(sentence2.indexOf("@import ") != -1){
			Pattern regex = Pattern.compile("@import\\s*\"([^\"]*)\";");
			Matcher regexMatcher = regex.matcher(sentence2);
			while(regexMatcher.find()){
				result.add(regexMatcher.group(1));
			}
			return result;
		}
		return null;
	}
	
	public static List<String> getJsFromScript(String sentence3){
		List<String> result = new ArrayList<>();
		//String result = "";
		if(sentence3.length() == 0) return null;
		if(sentence3.indexOf("script src=") != -1){
			Pattern regex = Pattern.compile("script\\s*src=\"([^\"]*)\"");
			Matcher regexMatcher = regex.matcher(sentence3);
			while(regexMatcher.find()){
				result.add(regexMatcher.group(1));
			}
			return result;
		}
		return null;
	}
	
	public static void main(String[] args) {
		HashMap<String, Set<String>> hm_web_techs = new HashMap<>();
		try {
			// find the path of file
			// WARC paths
			String srcDir = "E:/Brandeis_Study_file/Indy sty/210_workspace"
					+ "/Common Crawl WARC Examples/November2014/";
			File folder = new File(srcDir);
			File[] listOfFiles = folder.listFiles();
			BufferedReader br = new BufferedReader(new FileReader(listOfFiles[0].getPath()));
			String path;
			int index = 0;
			while ((path = br.readLine()) != null) {
				// do every 1100 url
				index++;
				if (index % 1100 != 0) {
					continue;
				}
				try {
					S3Service s3s = new RestS3Service(null);
					String fn = path;
					S3Object f = s3s.getObject("aws-publicdatasets", fn, null,null, null, null, null, null);
					// The file name identifies the
					// ArchiveReader and indicates if it should
					// be decompressed
					ArchiveReader ar = WARCReaderFactory.get(fn,f.getDataInputStream(), true);

					// Once we have an ArchiveReader, we can
					// work through each of the records it contains
					int q = 0;
					for (ArchiveRecord r : ar) {
						//do every 150,since each data, set has 150000 JSON files on average.
						q++;
						if (q % 150 != 0) {
							continue;
						}
						// The header file contains information
						// such as the type of record,
						// size, creation time, and URL

						// If we want to read the contents of the record, we can use the
						// ArchiveRecord as an InputStream Create a byte array that is as long
						// as the record's stated length
						byte[] rawData = IOUtils.toByteArray(r, r.available());

						// Why don't we convert it to a string
						// and print the start of it?
						// Let's hope it's text!
						String content = new String(rawData);
						String website = extractURL(r.getHeader().getUrl());
						
						Set<String> techName = new HashSet<String>();
						//count other technologies
						if(content.indexOf("imgur") != -1){
								techName.add("imgur");
						}
						if(content.indexOf("photobucket") != -1){
								techName.add("photobucket");
						}
						if(content.indexOf("tinypic") != -1){
								techName.add("tinypic");
						}
						if(content.indexOf("postimage") != -1){
								techName.add("postimage");
						}
						if(content.indexOf("imgsafe") != -1){
								techName.add("imgsafe");
						}
						if(content.indexOf("googletamanager") != -1){
								techName.add("googletamanager");
						}
						if(content.indexOf("adsbygoogle") != -1){
								techName.add("adsbygoogle");
						}
						if(content.indexOf("google-analytics") != -1){
								techName.add("google-analytics");
						}
						if(content.indexOf("google-site-verification") != -1){
								techName.add("google-site-verification");
						}
						if(content.indexOf("MongoDB") != -1 || content.indexOf("mongodb") != -1){
								techName.add("MongoDB");
						}
						if(content.indexOf("SQLite") != -1 || content.indexOf("sqlite") != -1){
								techName.add("SQLite");
						}
						if(content.indexOf("NoSQL") != -1 || content.indexOf("nosql") != -1){
								techName.add("NoSQL");
						}
						if(content.indexOf("Mongoose") != -1 || content.indexOf("mongoose") != -1){
								techName.add("Mongoose");
						}
						if(content.indexOf("Apache Derby") != -1 || content.indexOf("Apache DB") != -1){
								techName.add("Apache Derby");
						}
						if(content.indexOf("Oracle") != -1 || content.indexOf("oracle") != -1){
								techName.add("Oracle");
						}
						if(content.indexOf("PostgreSQL") != -1 || content.indexOf("postgresql") != -1){
								techName.add("PostgreSQL");
						}
						if(content.indexOf("Salesforce") != -1 || content.indexOf("salesforce") != -1){
								techName.add("Salesforce");
						}
						if((content.indexOf("microsoft") != -1 || content.indexOf("Microsoft") != -1) && (content.indexOf("crm") != -1 || content.indexOf("CRM") != -1)){
								techName.add("Microsoft Dynamics CRM");
						}
						if(content.indexOf("Pipedrive") != -1 || content.indexOf("pipedrive") != -1){
								techName.add("Pipedrive");
						}
						if((content.indexOf("microsoft") != -1 || content.indexOf("Microsoft") != -1) && (content.indexOf("azure") != -1 || content.indexOf("Azure") != -1)){
								techName.add("Microsoft Azure");
						}
						if(content.indexOf("AWS") != -1 || content.indexOf("aws") != -1){
								techName.add("AWS");
						}
						if(content.indexOf("Pipedrive") != -1 || content.indexOf("pipedrive") != -1){
								techName.add("Pipedrive");
						}
						if((content.indexOf("IBM") != -1 || content.indexOf("ibm") != -1) && (content.indexOf("bluemix") != -1 || content.indexOf("Bluemix") != -1)){
								techName.add("IBM Bluemix");
						}
						if(content.indexOf("OpenCms") != -1 || content.indexOf("opencms") != -1){
								techName.add("OpenCms");
						}
						if((content.indexOf("apache") != -1 || content.indexOf("Apache") != -1) && (content.indexOf("Roller") != -1 || content.indexOf("roller") != -1)){
								techName.add("Apache Roller");
						}
						if(content.indexOf("Drupal") != -1 || content.indexOf("drupal") != -1){
								techName.add("Drupal");
						}
						if(content.indexOf("Wordpress") != -1 || content.indexOf("wordpress") != -1){
								techName.add("Wordpress");
						}
						if(content.indexOf("Django") != -1 || content.indexOf("django") != -1){
								techName.add("Django");
						}
						if(content.indexOf(".css") != -1){
							techName.add("CSS");
						}
						if(content.indexOf(".js") != -1){
							techName.add("JavaScript");
						}
						if(content.indexOf("jquery") != -1 || content.indexOf("jQuery") != -1){
							techName.add("jQuery");
						}
						
						if(hm_web_techs.containsKey(website)){
							hm_web_techs.get(website).addAll(techName);
						}else{
							hm_web_techs.put(website, techName);
						}
						
						r.close();
					}
					Date date = new Date();
					System.out.println("done.."+date.toString());
					ar.close();
					f.closeDataInputStream();
				} catch (S3ServiceException | SocketTimeoutException
						| java.lang.RuntimeException
						| java.net.UnknownHostException VariableDeclaratorId) {
					continue;
				}
			}//while
			br.close();
			folder.exists();
			
			//save web address data into csv file
			PrintWriter pw1 = new PrintWriter(new File("webComponents.csv"));
			StringBuilder sb1 = new StringBuilder();
			sb1.append("web address");
			sb1.append(',');
			sb1.append("technologies");
			sb1.append('\n');
			for(String site : hm_web_techs.keySet()){
				sb1.append(site);
				sb1.append(',');
				sb1.append(hm_web_techs.get(site).toString());
				sb1.append('\n');
			}
			pw1.write(sb1.toString());
			pw1.close();
			
			
			System.out.println("this month is done.");
		} catch (IOException ex) {
		}
	}

}