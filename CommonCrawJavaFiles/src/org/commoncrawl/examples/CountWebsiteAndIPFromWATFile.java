package org.commoncrawl.examples;
import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
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

import com.esotericsoftware.jsonbeans.WrongJsonFormatException;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

/**
 * A raw example of how to process a WARC file using the org.archive.io package.
 * Common Crawl S3 bucket without credentials using JetS3t.
 *
 * @author Stephen Merity (Smerity)
 */
public class CountWebsiteAndIPFromWATFile {
	/**
	 * @param args
	 * @throws IOException
	 * @throws WrongJsonFormatException
	 */

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
	
	public static String getCountryGivenIP(String ip){
		String country = "";
		//build lookupService to look for country by given IP address
		File dbfile = new File("E:\\Brandeis_Study_file\\Indy sty"
				+ "\\210_workspace\\Common Crawl WARC Examples"
				+ "\\cc-warc-examples-master\\src\\resources\\GeoLiteCity.dat");
		try {
			LookupService lookupService = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);
			Location location = lookupService.getLocation(ip);
			country = location.countryName;
			lookupService.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return country;
	}


	public static void main(String[] args) {
		HashMap<String, Integer> hm_web = new HashMap<String, Integer>();
		HashMap<String, Integer> hm_ip = new HashMap<String, Integer>();
		try {
			// find the path of file
			String srcDir = "E:/Brandeis_Study_file/Indy sty/210_workspace"
					+ "/Common Crawl WARC Examples/December2014/";
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
						if(hm_web.containsKey(website)){
							hm_web.replace(website, hm_web.get(website), hm_web.get(website)+1);
						}else{
							hm_web.put(website, 1);
						}
						
						if(hm_ip.containsKey(ip)){
							hm_ip.replace(ip, hm_ip.get(ip), hm_ip.get(ip)+1);
						}else{
							hm_ip.put(ip, 1);
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
			PrintWriter pw1 = new PrintWriter(new File("website.csv"));
			StringBuilder sb1 = new StringBuilder();
			sb1.append("web address");
			sb1.append(',');
			sb1.append("number");
			sb1.append('\n');
			for(String ads : hm_web.keySet()){
				sb1.append(ads);
				sb1.append(',');
				sb1.append(hm_web.get(ads));
				sb1.append('\n');
			}
			pw1.write(sb1.toString());
			pw1.close();
			
			//save ip data into csv file
			PrintWriter pw2 = new PrintWriter(new File("ip.csv"));
			StringBuilder sb2 = new StringBuilder();
			sb2.append("ip");
			sb2.append(',');
			sb2.append("number");
			sb2.append('\n');
			for(String ip : hm_ip.keySet()){
				sb2.append(ip);
				sb2.append(',');
				sb2.append(hm_ip.get(ip));
				sb2.append('\n');
			}
			pw2.write(sb2.toString());
			pw2.close();
			
			System.out.println("this month is done.");
		} catch (IOException ex) {
		}
	}
	
}