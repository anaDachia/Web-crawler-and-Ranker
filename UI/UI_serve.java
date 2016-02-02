package UI;

import java.io.IOException;

import logic.Data;
import logic.Lucene;
import logic.Lucene;
import java.util.ArrayList;
import java.util.Date;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Servlet implementation class UI_serve
 */
@WebServlet("/UI_serve")
public class UI_serve extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 public static final String HTML_START="<html><body>";
	    public static final String HTML_END="</body></html>";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UI_serve() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		////////////////////////initial page settings //////////////////////
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "results";
		String docType =  "<!doctype html public \"-//w3c//dtd html 4.0 " +
			      "transitional//en\">\n";
		
		
		///////////////////////retrive form input////////////////////////////
		String query = request.getParameter("query");

		boolean pr = false;
		String use_pr = request.getParameter("Q2"); 
		if ("y".equals(use_pr)) 
			pr = true;
		System.out.println(pr);
		
		///////////////////////retirieve doc by lucene//////////////////////
	    Lucene ls = new Lucene();
	    ArrayList<Data> res_datas= null;
	    
	    try {
			res_datas = ls.query_parser(query, pr);
		} catch (ParseException e) {
			System.out.println("exception occured in converting query results to Data objects");
		}
	    System.out.println("dar UI" + res_datas.size());
	    
	    String text = "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h3 align=\"center\">" + title + "</h3>\n";
		String tmp;
		tmp = "<ul>\n";
		
	    for (int i = 0; i < res_datas.size(); i++) {
	    	tmp = tmp +
					"<li> <b>name</b>:"+
					res_datas.get(i).getName() +
					"<li> <b>authors</b>:" +
					"<ul>\n";
			ArrayList<String> res_authors = res_datas.get(i).getAuthors();
			for(int j = 0; j < res_authors.size();j++){
					tmp = tmp +
							"<li>" +
							res_authors.get(j);
							
			}
			tmp = tmp + "</ul>\n"+
					"<li> <b>url</b>:" +
					"<a href=\" + " +
					res_datas.get(i).geturl()+
					"\">"+res_datas.get(i).getName() +"</a>";
			
			tmp = tmp + "<hr>";
			
		}
	    text = text + tmp + "</ul>\n" +"</body></html>";  
	    
	    out.println(docType  + text);
	    
   
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
