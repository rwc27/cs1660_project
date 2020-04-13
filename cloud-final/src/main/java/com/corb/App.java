package com.corb;

import java.awt.*;
import java.io.*;
import java.util.Collections;
import java.io.FileInputStream;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Blob;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.google.api.services.dataproc.model.SubmitJobRequest;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.dataproc.*;
import com.google.api.services.dataproc.model.HadoopJob;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.cloud.storage.Storage.*;
import com.google.api.services.storage.model.StorageObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

class ActionEventDemo {
    JFrame frame=new JFrame();
	JFrame loaded=new JFrame();
	
	String input_file = "Invalid File";
	
	boolean fileEx = false;
	
	JLabel title_field = new JLabel("Load My Engine");
	JLabel list_field = new JLabel(" ");
    JButton file_button = new JButton("Choose Files");
	JButton load_button = new JButton("Load Engine");
	
	File selected_file;

 
    ActionEventDemo(){
        prepareGUI();
        buttonProperties();
    }
 
    public void prepareGUI(){
        frame.setTitle("Corbett - rwc27 - CS1660 - Final Project");
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        frame.setBounds(200,200,400,400);
		
		title_field.setBounds(140,20,140,40);
		frame.add(title_field);
		
		list_field.setBounds(140,100,140,40);
		frame.add(list_field);
		
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
	public void prepareLoaded() throws FileNotFoundException, IOException{
		loaded.setTitle("Corbett - rwc27 - CS1660 - Final Project");
		loaded.getContentPane().setLayout(null);
		loaded.setVisible(true);
		loaded.setBounds(200,200,400,400);
		loaded.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		frame.setVisible(false);
				
		loaded.add(title_field);
		title_field.setText("Engine Loading");	
		
		try
		{
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("./final-project-273902-34d59a10b0b6.json"))
                        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		HttpRequestInitializer ri = new HttpCredentialsAdapter(credentials);
 
        Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(),new JacksonFactory(), ri).build();
        HadoopJob job = new HadoopJob();
        job.setMainJarFileUri("gs://dataproc-staging-us-west1-1089798135765-6b3sxm6f/JAR/InvertedIndex.jar");
        job.setArgs(ImmutableList.of("InvertedIndex",input_file,"gs://dataproc-staging-us-west1-1089798135765-6b3sxm6f/Output"));
        dataproc.projects().regions().jobs().submit("final" , "us-west1", new SubmitJobRequest()
			.setJob(new Job()
            .setPlacement(new JobPlacement()
            .setClusterName("final"))
            .setHadoopJob(job)))
            .execute();
		}
		catch (FileNotFoundException ex)
		{
			System.exit(-1);
		}
		catch (IOException ez)
		{
			System.exit(-1);
		}
	}
	
    public void buttonProperties(){
		final JFileChooser chooser = new JFileChooser();					// Create a file chooser to select files
		chooser.setAcceptAllFileFilterUsed(false);
		File workingDirectory = new File(System.getProperty("user.dir"));	// Get the current directory which will be the WORKDIR that docker transfered all of the resources into
		chooser.setCurrentDirectory(workingDirectory);						// Set that directory as the current directory for the chooser
		FileNameExtensionFilter filter = new FileNameExtensionFilter(		// Filter out all files except those that end with .gz
			"GZ File", "gz");
		chooser.setFileFilter(filter);										// Set filter to the chooser
		
        file_button.setBounds(120,60,140,40);								//Setting location and size of button
        frame.add(file_button);												//adding button to the frame
		file_button.addActionListener(new ActionListener() {
		@Override
        public void actionPerformed(ActionEvent e) {
			int selectedFile = chooser.showOpenDialog(null);
			if (selectedFile == chooser.APPROVE_OPTION) {
				selected_file = chooser.getSelectedFile();
				String v = selected_file.getName();
				list_field.setText(v);
				if (v.compareTo("Hugo.tar.gz") == 0){
					input_file = "Hugo";
				} else if (v.compareTo("Shakespeare.tar.gz") == 0) {
					input_file = "Shakespeare";
				} else if (v.compareTo("Tolstoy.tar.gz") == 0) {
					input_file = "Tolstoy";
				} else if (v.compareTo("all.tar.gz") == 0) {
					input_file = "Data";
				} else {
					input_file = "Invalid File";
				}
				fileEx = true;
				System.out.println(input_file);
			} 
        }
		});
		
		load_button.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent f) {
			try{
				prepareLoaded();
			}catch(IOException er){
				System.out.println("Invalid File");
			}
		} 
		
		});
		
		load_button.setBounds(100,200,180,80);
		frame.add(load_button);
    }
 
}

public class App 
{	
    public static void main( String[] args )
    {
        new ActionEventDemo();
    }
}
