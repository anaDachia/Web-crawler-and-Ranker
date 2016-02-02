package logic;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawl2 {
	private ArrayList<String> seen = new ArrayList<String>();
	private int count = 0;
	JasonHelper js_hlp = new JasonHelper();
	private HashMap<String, Integer> visited_page_index = new HashMap<String, Integer>();
	
	
	//called by main to store seen links in a file to avoid recomputation
	public void store_seen_links (){
		JasonHelper js_hlp = new JasonHelper(null);
		js_hlp.write_seen_string(this.seen);
	}
	
	public void crawl_list(String inp_link) throws IOException{
		Queue<String> contain_book_urls = new LinkedList<String>();
		Queue<String> book_urls = new LinkedList<String>();
		book_urls.add(inp_link);
		final String domain = "http://www.goodreads.com";
		
		
		while( ( book_urls.size() != 0 || contain_book_urls.size() != 0 ) && count < 400)
		{

			//get a new url
			String book_url = null;
			
			if( book_urls.size() != 0 )
				book_url = book_urls.remove();
			else
				book_url = contain_book_urls.remove();
			
			if( seen.contains(book_url) )
				continue;
			
			System.out.println("get url: " + book_url );
			//----------------------------------------------------------------------
			
			Document doc = null;
			try {
				doc = Jsoup.connect(domain + book_url).timeout(100 * 1000).get();
			} catch (Exception e) {
				System.out.println("error in jsoup connect ");
				continue;
			}
			
			//first of parse this url
			if( book_url.startsWith("/book/show/") )
				parse_doc(doc, domain+ book_url);

			//extract page links-------------------------------------------
			//add them to a set first
			Elements links = doc.select("a[href]"); // a with href 
			HashSet<String> links_set = new HashSet<String>();
			
			for (Element new_link:links){
				String link = new_link.attr("href");
				if( link.startsWith("#") ) continue;
				else if( link.startsWith(domain) )
				{
					URL url = new URL( link );
					link = url.getPath();
				}
				if( !link.startsWith("/book/") ) continue;
				links_set.add(link);
			}
			
			//ArrayList<String> out_links = new ArrayList<String>();
			
			//now analyze links if they are good to be fetched 
			for (Iterator iterator = links_set.iterator(); iterator.hasNext(); ) {
				String link = (String) iterator.next();
				
				if( !seen.contains(link) )
				{
					if( link.startsWith("/book/show/") )
						book_urls.add( link );
					else
						contain_book_urls.add( link );
				}
				
				//out_links.add(link);
			}
								
			seen.add(book_url);
			//visited_pages_out_urls.add(out_links);
			visited_page_index.put(book_url, count);
			count++;
				
				//System.out.println("Done link retrieval of " + book_url + " : " + out_links.size() + " link");
			
		}
	}
	
	
	public void parse_doc(Document doc, String url){
		String title = "", intro = "";
		float score = 0;
		ArrayList<String> comments = new ArrayList<>();
		ArrayList<String> authors = new ArrayList<>();
		
		/////////////title///////////////
		Elements title_els = doc.getElementsByClass("bookTitle");
		for (Element t : title_els){
			title = t.text();
		}
		/////////////authors//////////////
		
		Elements auth_els = doc.select("span[itemprop=name]");//getElementsByClass("authorName");
		//System.out.println(auth_els);
		for (Element el: auth_els){
			//System.out.println(el.text() + "salam");
			authors.add(el.text());
		}
		
		////////////intro//////////////////

		Element des_els = doc.getElementById("description");
		try {
			intro = des_els.child(1).text();
		} catch (Exception e) {
			try {
				intro = des_els.child(0).text();
			} catch (Exception e2) {
				intro  = "";
			}
			
		}
		
		//System.out.println(intro);
		
		/////////////comments////////////////
		String cm;
		Elements comnt_els = doc.select("div[class= reviewText stacked]");
		for (Element c : comnt_els){
			//System.out.println("--------------");
			Element wrapper = c.child(0);
			try {
				cm = wrapper.child(1).text();
			} catch (Exception e) {
				cm = wrapper.child(0).text();
			}
			comments.add(cm);
		}
		
		////////////////score///////////////////
		Elements score_els = doc.select("span[itemprop=ratingValue]");//getElementsByClass("authorName");
		//System.out.println(auth_els);
		for (Element el: score_els){
			//System.out.println("in the scoring" + el.text());
			try{
				score = Float.parseFloat(el.text());
			}
			catch (Exception e){
				score = 0;
			}
		}
		
		Data d = new Data(title,intro,score, authors, comments, url);
		js_hlp.setData(d);
		js_hlp.write();
	}
}

