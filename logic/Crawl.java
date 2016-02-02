package logic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawl {
	private ArrayList<String> seen = new ArrayList<String>();
	private int count = 0;
	JasonHelper js_hlp = new JasonHelper();
	
	
	public void processPage(String url, Element inp_link) {
			
		//System.out.println(count + " " + url );
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url).timeout(1000*1000).get();
		} catch (IOException e) {
			System.out.println("IO exception occured! wow");
			long t= System.currentTimeMillis();
			long end = t+15000;
			boolean finish = true;
			while(System.currentTimeMillis() < end && finish) {
				try {
					doc = Jsoup.connect(url).timeout(1000*1000).get();
					finish = false;
				} catch (IOException e1) {
					
				}
			}
			if (finish){
				System.out.println("IO excepion shod fuck");
				return;
			}
		}
		
		//create data object and write to file as jason
		if(inp_link==null || inp_link.attr("href").startsWith("http://www.goodreads.com/book/show")){
			count ++;
			Data data = parse_doc(doc, url);
			js_hlp.setData(data);
			js_hlp.write();
		}
		
		
		//parse links
		seen.add(url);
		Elements quest =  doc.select("a[href]");
		for (Element link: quest){
			if(link.attr("href").startsWith("http://www.goodreads.com/book/")){
				String new_link = link.attr("href");
				if (! seen.contains(new_link)){
					if (count >= 400)
						return;
					processPage(new_link, link);
				}
			}
		}
	}
	
	//called by main to store seen links in a file to avoid recomputation
	public void store_seen_links (){
		JasonHelper js_hlp = new JasonHelper(null);
		js_hlp.write_seen_string(this.seen);
	}
	
	
	
	
	public Data parse_doc(Document doc, String url){
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
		return d;
	}
}

