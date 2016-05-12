package org.commoncrawl.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
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
public class MainWATReaderFile {
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

	public static String extractServer(String sentence) {
		String result = "";
		if (sentence.length() == 0)
			return null;
		if (sentence.indexOf("Server") != -1) {
			Pattern regex = Pattern.compile("\"Server\":\"(.*?)\"");
			Matcher regexMatcher = regex.matcher(sentence);
			if (regexMatcher.find()) {
				result = result + regexMatcher.group(1);
			}
			return result;
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

	public static String extractTitle(String sentence) {
		String result = "";
		if (sentence.length() == 0)
			return null;
		if (sentence.indexOf("Title") != -1) {
			Pattern regex = Pattern.compile("\"Title\":\"(.*?)\"");
			Matcher regexMatcher = regex.matcher(sentence);
			if (regexMatcher.find()) {
				result = result + regexMatcher.group(1);
			}
			return result;
		}
		return null;
	}

	public static String extractVersion(String sentence) {
		String result = "";
		if (sentence.length() == 0)
			return null;
		if (sentence.indexOf("Version") != -1) {
			Pattern regex = Pattern.compile("\"Version\":\"(.*?)\"");
			Matcher regexMatcher = regex.matcher(sentence);
			if (regexMatcher.find()) {
				result = result + regexMatcher.group(1);
			}
			return result;
		}
		return null;
	}

	public static void printIntoFile(String content) {
		try {
			PrintStream myconsole = new PrintStream(new File("./output.txt"));
			System.setOut(myconsole);
			myconsole.print(content);
		} catch (FileNotFoundException fx) {
			System.out.println(fx);

		}
	}

	public static void createTable(String name) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "CREATE TABLE " + name + " " + "(address     TEXT, "
					+ "date		TEXT, " + "ip	TEXT, " + "server	TEXT, "
					+ "title	TEXT, " + "HTTPResponseMessageVersion	TEXT)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println(name + "Table created successfully");
	}

	public static void insertIntoTable(String tableName, String insertText,
			String monthYear, String ip, String server, String title,
			String version) {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + tableName + ".db");
			c.setAutoCommit(false);
			// System.out.println("Inserting...Opened database successfully");

			stmt = c.prepareStatement("insert into " + tableName
					+ " values(?,?,?,?,?,?)");
			stmt.setString(1, insertText);
			stmt.setString(2, monthYear);
			stmt.setString(3, ip);
			stmt.setString(4, server);
			stmt.setString(5, title);
			stmt.setString(6, version);
			stmt.executeUpdate();

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		// System.out.println("Inserting...Records created successfully");
	}

	public static void main(String[] args) {
		Date date = new Date();
		System.out.println("start:" + date.toString());

		try {
			// process all the WAT files one by one under the file of "paths"
			String srcDir = "E:/Brandeis_Study_file/Indy sty/210_workspace/Common Crawl WARC Examples/paths/";
			File folder = new File(srcDir);
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles.length > 0) {
				for (int i = 0; i < listOfFiles.length; i++) {
					String monthYear = listOfFiles[i].getName();// get the month
																// and year that
																// data belongs
																// to

					createTable(monthYear);// create a table on SQLite

					// Read the wat file from a certain month of a year
					File folder2 = new File(listOfFiles[i].getPath());
					File[] listOfFiles2 = folder2.listFiles();
					if (listOfFiles2.length > 0) {
						for (int j = 0; j < listOfFiles2.length; j++) {
							BufferedReader br = new BufferedReader(
									new FileReader(listOfFiles2[j].getPath()));
							String path;
							int index = 0;
							while ((path = br.readLine()) != null) {
								// do every 1000 url
								index++;
								if (index % 1000 != 0) {
									continue;
								}
								System.out.println("Processing new set..."+ monthYear);

								try {
									// We're accessing a publicly available
									// bucket so don't need to fill in our
									// credentials
									S3Service s3s = new RestS3Service(null);
									String fn = path;
									S3Object f = s3s.getObject(
											"aws-publicdatasets", fn, null,
											null, null, null, null, null);
									// The file name identifies the
									// ArchiveReader and indicates if it should
									// be decompressed
									ArchiveReader ar = WARCReaderFactory.get(
											fn, f.getDataInputStream(), true);

									// Once we have an ArchiveReader, we can
									// work through each of the
									// records it contains
									int q = 0;
									for (ArchiveRecord r : ar) {
										q++;
										if (q % 150 != 0) {// do every 150,
															// since each data
															// set has 150000
															// JSON files on
															// average.
											continue;
										}
										// The header file contains information
										// such as the type of record,
										// size, creation time, and URL

										// If we want to read the contents of
										// the record, we can use the
										// ArchiveRecord as an InputStream
										// Create a byte array that is as long
										// as the record's stated length
										byte[] rawData = IOUtils.toByteArray(r,
												r.available());

										// Why don't we convert it to a string
										// and print the start of it?
										// Let's hope it's text!
										String content = new String(rawData);
										// System.out.println(content.toString());
										// Pretty printing to make the output
										// more readable
										
										insertIntoTable(
												monthYear,
												extractURL(String.valueOf(r
														.getHeader().getUrl())),
												monthYear,
												extractIPAddress(content),
												extractServer(content),
												extractTitle(content),
												extractVersion(content));
										
									}
									date = new Date();
									System.out.println("done..."
											+ date.toString());
									System.out.println("");
									f.closeDataInputStream();
								} catch (S3ServiceException
										| SocketTimeoutException
										| java.lang.RuntimeException 
										| java.net.UnknownHostException VariableDeclaratorId) {
									continue;
								}
							}
						}
					}
				}
			}
		} catch (IOException ex) {
		}

	}
}