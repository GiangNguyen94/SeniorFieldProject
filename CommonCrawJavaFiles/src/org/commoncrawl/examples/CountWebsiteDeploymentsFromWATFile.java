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

public class CountWebsiteDeploymentsFromWATFile {

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
	
	public static void main(String[] args) {
		//this hash map contains: key: web site; value: list of IP addresses that visited this web site;
		HashMap<String, List<String>> hm_web_ips = new HashMap<>();
		HashMap<String, HashMap<String, Integer>> hm_web_ctrs = new HashMap<>();
		List<String> ctryName = new ArrayList<String>();
		try {
			// find the path of file
			String srcDir = "E:/Brandeis_Study_file/Indy sty/210_workspace"
					+ "/Common Crawl WARC Examples/February2016/";
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
						String ip = extractIPAddress(content);
						
						//count web site and IP number
						if(hm_web_ips.containsKey(website)){
							hm_web_ips.get(website).add(ip);
						}else{
							hm_web_ips.put(website, new ArrayList<String>());
							hm_web_ips.get(website).add(ip);
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
			
			//for each web site, count the amount of visitors of people from different countries.
			//(customer group for each web site)
			for(String site : hm_web_ips.keySet()){
				List<String> IPs = hm_web_ips.get(site);
				HashMap<String, Integer> ctry_number = new HashMap<>();
				for(int i = 0; i < IPs.size(); i ++){
					//add country name into country name list
					try{
						String country = getCountryGivenIP(IPs.get(i)).replaceAll(",", "");
						if(country == null){
							continue;
						}
						if(!ctryName.contains(country)){
							ctryName.add(country);
						}
						//build hash map of ctry_number for each web site
						if(ctry_number.containsKey(country)){
							ctry_number.replace(country, ctry_number.get(country), ctry_number.get(country)+1);
						}else{
							ctry_number.put(country, 1);
						}
					}catch(NullPointerException e){
						continue;
					}
				}
				//build hash map of hm_web_ctrs
				hm_web_ctrs.put(site, ctry_number);
			}
			
			//save web address data into csv file
			PrintWriter pw1 = new PrintWriter(new File("websiteDeployments.csv"));
			StringBuilder sb1 = new StringBuilder();
			sb1.append("web address");
			for(int i = 0; i < ctryName.size(); i ++){
				sb1.append(',');
				sb1.append(ctryName.get(i));
			}
			sb1.append('\n');
			for(String site : hm_web_ctrs.keySet()){
				sb1.append(site);
				HashMap<String, Integer> c_n = hm_web_ctrs.get(site);
				for(int i = 0; i < ctryName.size(); i ++){
					sb1.append(',');
					if(c_n.containsKey(ctryName.get(i))){
						sb1.append(c_n.get(ctryName.get(i)));
					}else{
						sb1.append(0);
					}
				}
				sb1.append('\n');
			}
			pw1.write(sb1.toString());
			pw1.close();
			
			
			System.out.println("this month is done.");
		} catch (IOException ex) {
		}
	}

}
