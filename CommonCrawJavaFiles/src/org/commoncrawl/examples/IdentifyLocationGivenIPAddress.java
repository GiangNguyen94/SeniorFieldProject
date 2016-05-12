package org.commoncrawl.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

public class IdentifyLocationGivenIPAddress {

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
		IdentifyLocationGivenIPAddress obj = new IdentifyLocationGivenIPAddress();
		obj.run();
	}

	public void run() {
		String csvFile = "E:\\Brandeis_Study_file\\Indy sty\\210_workspace"
				+ "\\Common Crawl WARC Examples\\ip.csv";
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		HashMap<String, Integer> hm_country = new HashMap<>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			int lineIndex = -1;
			while ((line = br.readLine()) != null) {
				if (++lineIndex == 0) {
					continue;
				}
				String[] words = line.split(csvSplitBy);
				try {
					String country = getCountryGivenIP(words[0]);
					// save into hash map
					if (hm_country.containsKey(country)) {
						hm_country.replace(
								country,
								hm_country.get(country),
								hm_country.get(country)
										+ Integer.valueOf(words[1]));
					} else {
						hm_country.put(country, Integer.valueOf(words[1]));
					}
				} catch (NullPointerException e) {
					continue;
				}
			}
			// write hash map of country into csv file
			PrintWriter pw = new PrintWriter(new File("country.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("country");
			sb.append(',');
			sb.append("number");
			sb.append('\n');
			for (String cty : hm_country.keySet()) {
				sb.append(cty);
				sb.append(',');
				sb.append(hm_country.get(cty));
				sb.append('\n');
			}
			pw.write(sb.toString());
			pw.close();
		} catch (IOException e) {
			System.out.println("IO Exception raised.");
		}
	}

}
