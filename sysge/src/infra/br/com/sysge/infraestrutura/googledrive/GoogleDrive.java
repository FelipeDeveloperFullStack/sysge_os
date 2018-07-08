package br.com.sysge.infraestrutura.googledrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Felipe M. Santos
 * Using API Google Drive
 *
 */

public class GoogleDrive {
	
	 private static final String APPLICATION_NAME = "Google Drive API Java Quickstart/1.0";
	    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	    private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.
	    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	    private static final String CLIENT_SECRET_DIR = "/client_secret.json";
	    private static final String MIME_TYPE_FOLDER = "application/vnd.google-apps.folder";

	    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
	    	
	        InputStream in = GoogleDrive.class.getResourceAsStream(CLIENT_SECRET_DIR);
	        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER));
	        
	        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	                .setDataStoreFactory(fileDataStoreFactory)
	                .setAccessType("online")
	                .setApprovalPrompt("auto")
	                .build();
	        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	    }

	    public static void addFileCloud(String fileName, String folderName, String description, String path) throws IOException, GeneralSecurityException {
	        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	        
	        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	                .setApplicationName(APPLICATION_NAME)
	                .build();
	        
	        addFileCloud("backup", "Backup Sysge 2018", "Pasta de backup do sistema Sysge", service, "C:\\backup\\client_secret.json");
	       
	    }
	    
	    
	    private static void addFileCloud(String fileName, String folderName, String description, Drive service, String path) throws IOException{
	    	String id = createFolder(folderName, description, service);
	    	
	    	File sqlFile = new File();
	    	sqlFile.setName(fileName);
	    	if(id != null){
	    		sqlFile.setParents(Collections.singletonList(id));
	    	}
	    	
	    	java.io.File filePath = new java.io.File(path);
	    	FileContent fileContent = new FileContent("text/plain", filePath);
	    	
	    	service.files().create(sqlFile, fileContent).execute();
	    	
	    }
	    
	    
	    private static String createFolder(String folderName, String description, Drive service) throws IOException{
	    	
	    	    File fileMetadata = new File();
		        fileMetadata.setMimeType(MIME_TYPE_FOLDER);
		        fileMetadata.setName("Backup Sysge 2018");
		        fileMetadata.setDescription(description);
		        
		        String id = verifyNameFolderIsExist(folderName, service);
		        
		        if(id == null){
		        	service.files().create(fileMetadata).execute();
		        	return id;
		        }else{
		        	return id;
		        }
	    }
	    
	    private static String verifyNameFolderIsExist(String folderName, Drive service) throws IOException{
	    	 FileList result = service.files().list()
		                .setFields("nextPageToken, files(id, name)")
		                .execute();
		        
		        List<File> files = result.getFiles();
		        for (File file : files) {
		               if(file.getName().equalsIgnoreCase(folderName)){
		            	   return file.getId();
		               }	
		            }
	    	return null;
	    }

}
