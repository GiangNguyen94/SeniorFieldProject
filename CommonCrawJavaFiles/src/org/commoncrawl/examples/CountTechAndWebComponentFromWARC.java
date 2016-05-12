package org.commoncrawl.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
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

public class CountTechAndWebComponentFromWARC{

	public static String getImportCSS(String sentence2){
		//List<String> result = new ArrayList<>();
		String result = "";
		if(sentence2.length() == 0) return null;
		if(sentence2.indexOf("@import ") != -1){
			Pattern regex = Pattern.compile("@import\\s*\"([^\"]*)\";");
			Matcher regexMatcher = regex.matcher(sentence2);
			while(regexMatcher.find()){
				result += "\t" + regexMatcher.group(1);
			}
			return result;
		}
		return null;
	}
	
	public static String getJsFromScript(String sentence3){
		String result = "";
		if(sentence3.length() == 0) return null;
		if(sentence3.indexOf("script src=") != -1){
			Pattern regex = Pattern.compile("script\\s*src=\"([^\"]*)\"");
			Matcher regexMatcher = regex.matcher(sentence3);
			while(regexMatcher.find()){
				result += "\t" + regexMatcher.group(1);
			}
			return result;
		}
		return null;
	}
	
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
	
	public static String getAngularJS(String s){
		String result = "";
		boolean found = false;
		if(s.length() == 0) return null;
		if(s.indexOf("angular") != -1){
			Pattern regex = Pattern.compile("angular([^\"'<>]*?)\\.js");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				found = true;
				result += "\t\t\t" + regexMatcher.group(0);
			}
			return result;
		}
		return null;
	}
	
	public static String getIonicJS(String s){
		String result = "";
		if(s.length() == 0) return null;
		if(s.indexOf("ionic") != -1){
			Pattern regex = Pattern.compile("ionic([^\"<>\\s]*?)\\.js");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				result += "\t\t\t" + regexMatcher.group(0);
			}
			return result;
		}
		return null;
	}
	
	public static String getIonicCSS(String s){
		String result = "";
		if(s.length() == 0) return null;
		if(s.indexOf("ionic") != -1){
			Pattern regex = Pattern.compile("ionic([^\"'<>]*?)\\.css");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				result += "\t\t\t" + regexMatcher.group(0);
			}
			return result;
		}
		return null;
	}
	
	public static String getIonicClass(String s){
		String result = "";
		if(s.length() == 0) return null;
		if(s.indexOf("ionic") != -1){
			Pattern regex = Pattern.compile("class=\"([^\"'<>]*?)\\s*ionic\"");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				result += "\t\t\t" + regexMatcher.group(0);
			}
			return result;
		}
		return null;
	}
	
	public static String getReactJS(String s){
		String result = "";
		if(s.length() == 0) return null;
		if(s.indexOf("react") != -1){
			Pattern regex = Pattern.compile("react([^\"'<>]*?)\\.js");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				result += "\t\t\t" + regexMatcher.group(0);
			}
			return result;
		}
		return null;
	}
	
	public static String getReactCSS(String s){
		String result = "";
		if(s.length() == 0) return null;
		if(s.indexOf("react") != -1){
			Pattern regex = Pattern.compile("react([^\"'<>]*?)\\.css");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				result += "\t\t\t" + regexMatcher.group(0);
			}
			return result;
		}
		return null;
	}
	
	public static String getHubspotCSS(String s){
		String result = "";
		boolean found = false;
		if(s.length() == 0) return null;
		if(s.indexOf("hubspot") != -1){
			Pattern regex = Pattern.compile("hubspot([^\"'<>]*?)\\.css");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				found = true;
				result += "\t\t\t" + regexMatcher.group(0);
			}
			if(found){
				return result;
			}else{
				return "\t\t\t" + "hubspot";
			}
			
		}
		return null;
	}
	
	public static String getHubspotJS(String s){
		String result = "";
		boolean found = false;
		if(s.length() == 0) return null;
		if(s.indexOf("hubspot") != -1){
			Pattern regex = Pattern.compile("hubspot([^\"'<>]*?)\\.js");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				found = true;
				result += "\t\t\t" + regexMatcher.group(0);
			}
			if(found){
				return result;
			}else{
				return "\t\t\t" + "hubspot";
			}
		}
		return null;
	}
	
	public static String gethsappstaticJS(String s){
		String result = "";
		boolean found = false;
		if(s.length() == 0) return null;
		if(s.indexOf("hsappstatic") != -1){
			Pattern regex = Pattern.compile("hsappstatic([[^\"'<>]*?)\\.js");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				found = true;
				result += "\t\t\t" + regexMatcher.group(0);
			}
			if(found){
				return result;
			}else{
				return "\t\t\t" + "hubspot";
			}
		}
		return null;
	}
	
	public static String gethsappstaticCSS(String s){
		String result = "";
		boolean found = false;
		if(s.length() == 0) return null;
		if(s.indexOf("hsappstatic") != -1){
			Pattern regex = Pattern.compile("hsappstatic([[^\"'<>]*?)\\.css");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				found = true;
				result += "\t\t\t" + regexMatcher.group(0);
			}
			if(found){
				return result;
			}else{
				return "\t\t\t" + "hubspot";
			}
			
		}
		return null;
	}
	
	public static String getJqueryJS(String s){
		String result = "";
		boolean found = false;
		if(s.length() == 0) return null;
		if(s.indexOf("jquery") != -1 || s.indexOf("jQuery") != -1){
			Pattern regex = Pattern.compile("jquery([^\"'<>\\s()]*?)\\.js");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				found = true;
				result += "\t\t\t" + regexMatcher.group(0);
			}
			if(found){
				return result;
			}else{
				return "\t\t\t" + "jquery";
			}
			
		}
		return null;
	}
	
	public static String getJqueryCSS(String s){
		String result = "";
		boolean found = false;
		if(s.length() == 0) return null;
		if(s.indexOf("jquery") != -1 || s.indexOf("jQuery") != -1){
			Pattern regex = Pattern.compile("jquery([^\"'<>\\s()]*?)\\.css");
			Matcher regexMatcher = regex.matcher(s);
			while(regexMatcher.find()){
				found = true;
				result += "\t\t\t" + regexMatcher.group(0);
			}
			if(found){
				return result;
			}else{
				return "\t\t\t" + "jquery";
			}
			
		}
		return null;
	}
	
	public static void main(String[] args) {
		HashMap<String, Set<String>> hm_web_techs = new HashMap<>();
		HashMap<String, Integer> hm_techs_number = new HashMap<>();
		String allTechsInOneString = "";
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
					System.out.println(path);
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
							r.close();
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
						//System.out.println(r.getHeader().getHeaderValue("WARC-Type"));//response
						
						//count web technologies
						String website = extractURL(r.getHeader().getUrl());
						//Set<String> techName = new HashSet<String>();	
						String techName = "";	
						String angularJS = getAngularJS(content);
						String ionicJS = getIonicJS(content);
						String ionicCSS = getIonicCSS(content);
						String ionicClass = getIonicClass(content);
						String reactJS = getReactJS(content);
						String reactCSS = getReactCSS(content);
						String hubspotJS = getHubspotJS(content);
						String hubspotCSS = getHubspotCSS(content);
						String hsappstaticJS = gethsappstaticJS(content);
						String hsappstaticCSS = gethsappstaticCSS(content);
						String jqueryJS = getJqueryJS(content);
						String jqueryCSS = getJqueryCSS(content);
						
						if(angularJS != null){techName += angularJS;}
						if(ionicJS != null) {techName += ionicJS;}
						if(ionicCSS != null) {techName += ionicCSS;}
						if(ionicClass != null){techName += ionicClass;}
						if(reactJS != null){techName += reactJS;}
						if(reactCSS != null){techName += reactCSS;}
						if(hubspotJS != null) {techName += hubspotJS;}
						if(hubspotCSS != null){techName += hubspotCSS;}
						if(hsappstaticJS != null) {techName += hsappstaticJS;}
						if(hsappstaticCSS != null){techName += hsappstaticCSS;}
						if(jqueryJS != null){techName += jqueryJS;}
						if(jqueryCSS != null){techName += jqueryCSS;}
						
						String[] techs = techName.split("\t\t\t");
						for(int i = 0 ; i < techs.length; i ++){
							if(!techs[i].equals("") && techs[i] != null){
								if(hm_web_techs.containsKey(website)){
									hm_web_techs.get(website).add(techs[i]);
								}else{
									hm_web_techs.put(website, new HashSet<String>());
									hm_web_techs.get(website).add(techs[i]);
								}
							}
						}
						
						//count technologies
						allTechsInOneString += techName;
						
						r.close();
					}
					ar.close();
					f.closeDataInputStream();
					s3s.deleteObject("aws-publicdatasets", fn);
					System.out.println("done....");
				} catch (S3ServiceException | SocketTimeoutException
						| java.lang.RuntimeException
						| java.net.UnknownHostException VariableDeclaratorId) {
					continue;
				}
			}//while
			br.close();
			folder.exists();
			
			String[] allTechs = allTechsInOneString.split("\t\t\t");
			for(int i = 0; i < allTechs.length; i ++){
				if(!allTechs[i].equals("") && allTechs[i] != null){
					if(hm_techs_number.containsKey(allTechs[i])){
						hm_techs_number.replace(allTechs[i], hm_techs_number.get(allTechs[i]), hm_techs_number.get(allTechs[i])+1);
					}else{
						hm_techs_number.put(allTechs[i], 1);
					}
				}
			}
			
			//save technologies data into csv file
			PrintWriter pw1 = new PrintWriter(new File("technologies.csv"));
			StringBuilder sb1 = new StringBuilder();
			sb1.append("technologies");
			sb1.append(',');
			sb1.append("number");
			sb1.append('\n');
			for(String tech : hm_techs_number.keySet()){
				sb1.append(tech.replaceAll(",", "").replaceAll("\n", ""));
				sb1.append(',');
				sb1.append(hm_techs_number.get(tech));
				sb1.append('\n');
			}
			pw1.write(sb1.toString());
			pw1.close();
			
			//save web address data into csv file
			PrintWriter pw2 = new PrintWriter(new File("webComponents.csv"));
			StringBuilder sb2 = new StringBuilder();
			sb2.append("web address");
			sb2.append(',');
			sb2.append("technologies");
			sb2.append('\n');
			for(String site : hm_web_techs.keySet()){
				sb2.append(site);
				sb2.append(',');
				sb2.append(hm_web_techs.get(site).toString());
				sb2.append('\n');
			}
			pw2.write(sb2.toString());
			pw2.close();
			
			System.out.println("this month is done.");
		} catch (IOException ex) {
		}
	}

}
