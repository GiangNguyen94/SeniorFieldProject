package org.commoncrawl.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/*
 * this class is used to merge specific technologies into appropriate category
 */
public class MergeTechnologies {

	public static void main(String[] args) {
		MergeTechnologies obj = new MergeTechnologies();
		obj.run();
	}

	public void run(){
		String csvFilePath = "E:\\Brandeis_Study_file\\Indy sty\\210_workspace"
				+ "\\Common Crawl WARC Examples\\data2-technologies per month"
				+ "\\122014\\technologies.csv";
		BufferedReader br = null;
		String line = "";
		HashMap<String, Integer> hm = new HashMap<>();
		try{
			br = new BufferedReader(new FileReader(csvFilePath));
			while((line = br.readLine()) != null){
				System.out.println("asdasd");
				String[] elements = line.split(",");// tech, num
				if(elements[0].indexOf("jquery") != -1){
					if(hm.containsKey("jquery")){
						if(elements.length > 1 && Integer.valueOf(elements[1]) >= 0)
							hm.replace("jquery", hm.get("jquery"), hm.get("jquery")+Integer.valueOf(elements[1]));
					}else{
						hm.put("jquery", Integer.valueOf(elements[1]));
					}
				}else if(elements[0].indexOf("react") != -1){
					if(hm.containsKey("react")){
						if(elements.length > 1 && Integer.valueOf(elements[1]) >= 0)
							hm.replace("react", hm.get("react"), hm.get("react")+Integer.valueOf(elements[1]));
					}else{
						hm.put("react", Integer.valueOf(elements[1]));
					}
				}else if(elements[0].indexOf("ionic") != -1){
					if(hm.containsKey("ionic")){
						if(elements.length > 1 && Integer.valueOf(elements[1]) >= 0)
							hm.replace("ionic", hm.get("ionic"), hm.get("ionic")+Integer.valueOf(elements[1]));
					}else{
						hm.put("ionic", Integer.valueOf(elements[1]));
					}
				}else if(elements[0].indexOf("angular") != -1){
					if(hm.containsKey("angular")){
						if(elements.length > 1 && Integer.valueOf(elements[1]) >= 0)
							hm.replace("angular", hm.get("angular"), hm.get("angular")+Integer.valueOf(elements[1]));
					}else{
						hm.put("angular", Integer.valueOf(elements[1]));
					}
				}else if(elements[0].indexOf("hubspot") != -1){
					if(hm.containsKey("hubspot")){
						if(elements.length > 1 && Integer.valueOf(elements[1]) >= 0)
							hm.replace("hubspot", hm.get("hubspot"), hm.get("hubspot")+Integer.valueOf(elements[1]));
					}else{
						hm.put("hubspot", Integer.valueOf(elements[1]));
					}
				}
			}
			
			//save data into csv file
			PrintWriter pw1 = new PrintWriter(new File("mergedTechnologies.csv"));
			StringBuilder sb1 = new StringBuilder();
			sb1.append("technologies");
			sb1.append(',');
			sb1.append("number");
			sb1.append('\n');
			for(String tech : hm.keySet()){
				sb1.append(tech);
				sb1.append(',');
				sb1.append(hm.get(tech));
				sb1.append('\n');
			}
			pw1.write(sb1.toString());
			pw1.close();
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
