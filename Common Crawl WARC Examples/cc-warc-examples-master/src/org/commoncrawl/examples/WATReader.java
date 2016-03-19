package org.commoncrawl.examples;
import java.io.IOException;

import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;

/**
 * This is a raw example of how you can retrieve a file from the
 * Common Crawl S3 bucket without credentials using JetS3t.
 *
 * @author Stephen Merity (Smerity)
 */
public class WATReader {
	public static void main(String[] args) throws IOException, S3ServiceException {
		// We're accessing a publicly available bucket so don't need to fill in our credentials
		S3Service s3s = new RestS3Service(null);
		
		// Let's grab a file out of the CommonCrawl S3 bucket
		//String fn = "common-crawl/crawl-data/CC-MAIN-2015-40/segments/1443736672328.14/warc/CC-MAIN-20151001215752-00000-ip-10-137-6-227.ec2.internal.warc.gz";//warc.paths
		String fn = "common-crawl/crawl-data/CC-MAIN-2015-40/segments/1443736672328.14/wat/CC-MAIN-20151001215752-00000-ip-10-137-6-227.ec2.internal.warc.wat.gz";
		S3Object f = s3s.getObject("aws-publicdatasets", fn, null, null, null, null, null, null);
		
		// The file name identifies the ArchiveReader and indicates if it should be decompressed
		ArchiveReader ar = WARCReaderFactory.get(fn, f.getDataInputStream(), true);
		
		// Once we have an ArchiveReader, we can work through each of the records it contains
		int i = 0;
		for(ArchiveRecord r : ar) {/*
			System.out.println("f content disposition: " + f.getContentDisposition());
			System.out.println("f content encoding: " + f.getContentEncoding());
			System.out.println("f content language: " + f.getContentLanguage());
			System.out.println("f content type: " + f.getContentType());
			System.out.println("f E tag: " + f.getETag());
			System.out.println("f key: " + f.getKey());
			System.out.println("f Md5HashAsBase64: " + f.getMd5HashAsBase64());
			System.out.println("f bucket name: " + f.getBucketName());
			System.out.println("f last modified date: " + f.getLastModifiedDate());
			System.out.println("f meta data map: " + f.getMetadataMap());
			System.out.println("f modifiable metadata : " + f.getModifiableMetadata());
			System.out.println("f owner: " + f.getOwner());
			// The header file contains information such as the type of record, size, creation time, and URL
			System.out.println("Header: " + r.getHeader());
			System.out.println("URL: " + r.getHeader().getUrl());
			System.out.println("Content begin: " + r.getHeader().getContentBegin());
			System.out.println("Date: " + r.getHeader().getDate());
			System.out.println("Digest: " + r.getHeader().getDigest());
			System.out.println("Length: " + r.getHeader().getLength());
			System.out.println("Mimetype: " + r.getHeader().getMimetype());
			System.out.println("Offset: " + r.getHeader().getOffset());
			System.out.println("Reader Identifier: " + r.getHeader().getReaderIdentifier());
			System.out.println("RecordIdentifier: " + r.getHeader().getRecordIdentifier());
			System.out.println("Version: " + r.getHeader().getVersion());
			System.out.println("Header Field Keys: " + r.getHeader().getHeaderFieldKeys());
			System.out.println("HeaderFields: " + r.getHeader().getHeaderFields());
			System.out.println("r position: " + r.getPosition());
			System.out.println("r digest str: " + r.getDigestStr());
			System.out.println("r read: " + r.read());
			System.out.println("r content headers: " + r.hasContentHeaders());
			System.out.println("r available: " + r.available());
			System.out.println();
			*/
			// If we want to read the contents of the record, we can use the ArchiveRecord as an InputStream
			// Create a byte array that is as long as all the record's stated length
			byte[] rawData = new byte[r.available()];
			r.read(rawData);
			// Note: potential optimization would be to have a large buffer only allocated once
			
			// Why don't we convert it to a string and print the start of it? Let's hope it's text!
			String content = new String(rawData);
			System.out.println(content);
			
			// Pretty printing to make the output more readable 
			System.out.println("=-=-=-=-=-=-=-=-=");
			if (i++ > 20) break; 
		}
	}
}