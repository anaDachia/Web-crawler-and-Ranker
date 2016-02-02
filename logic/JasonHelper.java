package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
*/

public class JasonHelper {
	private String path = "/run/media/ana/New Volume/sharif/term7/JavaCodes/data/";
	ObjectMapper mapper = new ObjectMapper();
	private Data data;
	
	public JasonHelper(){}
	
	public JasonHelper(Data data) {
		this.data = data;
	}
	public void setData(Data data){
		this.data = data;
	}

	public void write_eigen(List<Double> seens){
		try {
			File f = new File (String.format("%s/seen_files/eigen2",this.path));
			f.createNewFile();
			mapper.writeValue(f, seens);
			mapper.writeValueAsString(seens);
			
		} catch (JsonGenerationException e) {
			System.out.println("error in jason generation");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.out.println("error in jason mapping");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error in IO");
			e.printStackTrace();
		}
	
	}
	public List<Double> read_eigen_string(){
		try {
			String file_path = this.path+"/seen_files/eigen2";
			//System.out.println("in jason helper and reading file");
			List<Double> read_data = mapper.readValue(new File(file_path), List.class);
			//System.out.println(read_data);
			return read_data;
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
		return null;
	}


	
	public void write_seen_string(ArrayList<String> seens){
		try {
			File f = new File (String.format("%s/seen_files/seen2",this.path));
			f.createNewFile();
			mapper.writeValue(f, seens);
			mapper.writeValueAsString(seens);
			
		} catch (JsonGenerationException e) {
			System.out.println("error in jason generation");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.out.println("error in jason mapping");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error in IO");
			e.printStackTrace();
		}
	
	}
	public ArrayList<String> read_seen_string(){
		try {
			String file_path = this.path+"/seen_files/seen2";
			//System.out.println("in jason helper and reading file");
			ArrayList<String> read_data = mapper.readValue(new File(file_path), ArrayList.class);
			//System.out.println(read_data);
			return read_data;
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
		return null;
	}

	public void write(){
		try {
			
			File f = new File (String.format("%s/files2/%s",this.path, data.getName()));
			f.createNewFile();
			mapper.writeValue(f, data);
			mapper.writeValueAsString(data);
		} catch (JsonGenerationException e) {
			System.out.println("error in jason generation");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.out.println("error in jason mapping");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error in IO");
			e.printStackTrace();
		}
	}
	
	public Data read(String filename){
		try {
			String file_path = this.path+"/files2/" + filename;
			//System.out.println("in jason helper and reading file");
			Data read_data = mapper.readValue(new File(file_path), Data.class);
	 
			//System.out.println(read_data);
			return read_data;
	 
		} catch (JsonGenerationException e) {
			System.out.println("json gen error");
			//e.printStackTrace();
	 
		} catch (JsonMappingException e) {
			System.out.println("jason map error");
			//e.printStackTrace();
	 
		} catch (IOException e) {
			System.out.println("jse");
			//e.printStackTrace();
	 
		}
		return null;
	}
	

}
