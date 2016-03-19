package org.commoncrawl.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
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

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.JsonException;
import com.esotericsoftware.jsonbeans.JsonReader;
import com.esotericsoftware.jsonbeans.JsonValue;
import com.esotericsoftware.jsonbeans.OutputType;
import com.esotericsoftware.jsonbeans.WrongJsonFormatException;

/**
 * A raw example of how to process a WARC file using the org.archive.io package.
 * Common Crawl S3 bucket without credentials using JetS3t.
 *
 * @author Stephen Merity (Smerity)
 */
public class WARCReaderTest {
	/**
	 * @param args
	 * @throws IOException
	 * @throws WrongJsonFormatException
	 */

	public static String extractURL(String url) {
		String result = "";
		if(url.length() == 0) return null;
		if (url.indexOf("http") != -1) {
			Pattern regex = Pattern.compile("^https?://[^/]+");
			Matcher regexMatcher = regex.matcher(url);
			if (regexMatcher.find ()) {
				for (int i = 0; i <= regexMatcher.groupCount(); i++) {
					result += " " + regexMatcher.group(i);
				}
			}
			return result;
		}
		return null;
	}

	public static void printIntoFile(String content){
		try{
			PrintStream myconsole = new PrintStream(new File("./output.txt"));
			System.setOut(myconsole);
			myconsole.print(content);
		}catch(FileNotFoundException fx){
			System.out.println(fx);
			
		}
	}
	
	public static void main(String[] args) throws IOException, S3ServiceException {
		// Set up a local compressed WARC file for reading
		//String fn = "./CC-MAIN-20151001215752-00000-ip-10-137-6-227.ec2.internal.warc.wat.gz";
		//FileInputStream is = new FileInputStream(fn);
		// The file name identifies the ArchiveReader and indicates if it should
		// be decompressed
		//ArchiveReader ar = WARCReaderFactory.get(fn, is, true);
		Date date = new Date();
		System.out.println(date.toString());
		try(BufferedReader br = new BufferedReader(new FileReader("./wat.paths"))){
			String path;
			while((path = br.readLine()) != null){
				// We're accessing a publicly available bucket so don't need to fill in our credentials
				S3Service s3s = new RestS3Service(null);
				String fn = path;
				S3Object f = s3s.getObject("aws-publicdatasets", fn, null, null, null, null, null, null);
				// The file name identifies the ArchiveReader and indicates if it should be decompressed
				ArchiveReader ar = WARCReaderFactory.get(fn, f.getDataInputStream(), true);
				
				// json
				//Json json = new Json();
				//json.setOutputType(OutputType.json);

				// Once we have an ArchiveReader, we can work through each of the
				// records it contains
				int i = 0;
				for (ArchiveRecord r : ar) {
					// The header file contains information such as the type of record,
					// size, creation time, and URL
					
					//System.out.println("site: " +extractURL(String.valueOf(r.getHeader().getUrl())));

					// If we want to read the contents of the record, we can use the
					// ArchiveRecord as an InputStream
					// Create a byte array that is as long as the record's stated length
					byte[] rawData = IOUtils.toByteArray(r, r.available());

					// Why don't we convert it to a string and print the start of it?
					// Let's hope it's text!
					String content = new String(rawData);
					try {
					} catch (JsonException je) {
					}	

					//System.out.println(content.toString());
					// Pretty printing to make the output more readable
				}
				date = new Date();
				System.out.println("done..."+date.toString());
			}
		}
		
		
	}
}