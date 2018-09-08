package com.cse535;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mathworks.toolbox.javabuilder.*;



import NBC.Class1;
import KNN.Class2;
import SVM.Class3;
import RF.Class4;




public class FogServer extends HttpServlet {
    private final String UPLOAD_DIRECTORY = "C:\\Users\\LENOVO\\Documents\\BrainNet\\FogIncomingRequests";
    private final String REGISTRY_DIRECTORY = "C:\\Users\\LENOVO\\Documents\\BrainNet\\RegisteredUsers";
    private String username = null;


    Hashtable selection = new Hashtable();
	
//    private final String UPLOAD_DIRECTORY = "/home/abhinab22mohanty/IncomingFiles";
//    private final String REGISTRY_DIRECTORY = "/home/abhinab22mohanty/RegisteredUsers";
    private int check_value = 0;
    private String filepath;
    private String name = null;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
//		id = Integer.parseInt(request.getParameter("id"));
//		username = request.getParameter("name");
//		selection.put(username, id);
		response.setStatus(200);
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int id = 1;
		
	           try {
	                List<org.apache.commons.fileupload.FileItem> multiparts = new ServletFileUpload(
	                                         new DiskFileItemFactory()).parseRequest(request);
	              
	                for(org.apache.commons.fileupload.FileItem item : multiparts){
	                    if(!item.isFormField()){
	                        name = new File(item.getName()).getName();
	                        filepath = UPLOAD_DIRECTORY + File.separator + name;
	                        item.write( new File(filepath));
	                    }else {
	                        if(item.getFieldName().equals("id"))
	                        {   
	                             id=Integer.parseInt(item.getString());
	                        }
	                    }
	                }
	                
//	               int id = (Integer) selection.get(name);
//	             id = Integer.parseInt(request.getParameter("id"));
	       
	                
	             switch(id) {
	             case 1:
	            	 check_value = authenticate1(filepath,name);
	            	 break;
	             case 2:
	            	 check_value = authenticate2(filepath,name);
	            	 break;
	             case 3:
	            	 check_value = authenticate3(filepath,name);
	            	 break;
	             case 4:
	            	 check_value = authenticate4(filepath,name);
	            	 break;
	             }

	             
	             if(check_value == 1)
	            	 response.setStatus(200);
	                
	             else
	            	 response.setStatus(404);
	                
	                
	               //request.setAttribute("message", "File Uploaded Successfully");
	               
	            } catch (Exception ex) {
	            	response.setStatus(404);
	            }          
	         


		
	}


	private int authenticate4(String filepath2, String name2) throws MWException {
		// TODO Auto-generated method stub
		String pathToEdfFile = filepath2;
		String check_filePath = REGISTRY_DIRECTORY + File.separator + name2;

		File f = new File(check_filePath);
		if(f.exists()) { 
		    //return 0;
		
		
		Object[] resltsum = null;
		
		
		Class4 m = new Class4();
		
		
		resltsum = m.RF(1,check_filePath,pathToEdfFile);
		
		MWArray javaSum = (MWNumericArray)resltsum[0];
		
		double[][] total = (double[][])javaSum.toArray();
		
		int finalResult = (int) total[0][0];
		
		if(finalResult == 1)
			return 1;
		else
			return 0;
		}
		else {
				return 0;
		}
	}


	private int authenticate3(String filepath2, String name2) throws MWException {
		// TODO Auto-generated method stub
		String pathToEdfFile = filepath2;
		String check_filePath = REGISTRY_DIRECTORY + File.separator + name2;

		File f = new File(check_filePath);
		if(f.exists()) { 
		   
		
		
		Object[] resltsum = null;
		
		
		Class3 m = new Class3();
		
		
		resltsum = m.SVM(1,check_filePath,pathToEdfFile);
		
		MWArray javaSum = (MWNumericArray)resltsum[0];
		
		double[][] total = (double[][])javaSum.toArray();
		
		int finalResult = (int) total[0][0];
		
		if(finalResult == 1)
			return 1;
		else
			return 0;
		}
		else {
			return 0;
		}
	}


	private int authenticate2(String filepath2, String name2) throws MWException {
		// TODO Auto-generated method stub
		String pathToEdfFile = filepath2;
		String check_filePath = REGISTRY_DIRECTORY + File.separator + name2;

		File f = new File(check_filePath);
		if(f.exists()) { 
		    
		
		
		Object[] resltsum = null;
		
		
		Class2 m = new Class2();
		
		
		resltsum = m.KNN(1,check_filePath,pathToEdfFile);
		
		MWArray javaSum = (MWNumericArray)resltsum[0];
		
		double[][] total = (double[][])javaSum.toArray();
		
		int finalResult = (int) total[0][0];
		
		if(finalResult == 1)
			return 1;
		else
			return 0;
		}
		else {
			return 0;
		}
	}


	private int authenticate1(String pathToFile,String file_name) throws FileNotFoundException, RemoteException, MWException {
		// TODO Auto-generated method stub
		String pathToEdfFile = pathToFile;
		String check_filePath = REGISTRY_DIRECTORY + File.separator + file_name;

		File f = new File(check_filePath);
		if(f.exists()) { 
		    
		
		
		Object[] resltsum = null;
		
		
		Class1 m = new Class1();
		
		
		resltsum = m.NBC(1,check_filePath,pathToEdfFile);
		
		MWArray javaSum = (MWNumericArray)resltsum[0];
		
		double[][] total = (double[][])javaSum.toArray();
		
		int finalResult = (int) total[0][0];
		
		if(finalResult == 1)
			return 1;
		else
			return 0;
		}
		else
		{
			return 0;
		}
	}



	

}
