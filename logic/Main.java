package logic;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;
//lates result : seen2 file2  ---> eigen call nashode faghat uncomment kardam!

public class Main {
	static Crawl2 c = new Crawl2();
	public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.surround.parser.ParseException {

//--------------------------------------------------------------------------------------------------
		//we first process pages
		/*
		try {
			c.crawl_list("/book/show/3241368-the-little-prince-letter-to-a-hostage");
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.store_seen_links();
		*/
		
		
		//-----> from now on i just read seen links from file!!
//---------------------------------------------------------------------------------------------------
		//then we compute page ranks
		
		JasonHelper js = new JasonHelper();
		ArrayList<String>seen_url =  js.read_seen_string();
		/*
		System.out.println(seen_url);
		PageRank pr = new PageRank(seen_url, "http://www.goodreads.com");
		pr.build_adj_matrix();
		pr.comp_eigen();
		*/
		
		
		//-----> from now on I read eigen values from file
//--------------------------------------------------------------------------------------------------
		//print maximum and minimum page rank : results are written below
		  
		  /*
		List<Double> read_eigen = js.read_eigen_string();
		String max_name;
		String min_name;
		double max = -1000;
		double min = 1000;
		int max_ind = -1;
		int min_ind = -1;
		for (int i = 0; i < read_eigen.size(); i++) {
			double num = read_eigen.get(i);
			if ( num > max){
				max = num;
				max_ind = i;
			}
			if(num < min && num != 0){
				min = num;
				min_ind = i;
			}
		}
		max_name = seen_url.get(max_ind);
		min_name = seen_url.get(min_ind);
		System.out.println(max_name + " " + max);
		System.out.println(min_name + " " + min);*/
		
		/*
		/book/show/18721816-le-petit-prince 0.027828859660642268
		/book/show/1915739.It_s_the_Great_Pumpkin_Charlie_Brown 4.072241100986309E-56
		 * */
//-------------------------------------------------------------------------------------				
		//then we index by lucene with out page rank 
		Lucene l = new Lucene();
		l.index(false);
		
		//then we index with page rank in another index file
		l.index(true);
		
 
	
	}

}
