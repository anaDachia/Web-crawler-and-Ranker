package logic;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class Lucene {
	
	final static String data_address = "./Data/";
	final static String docs_address = "Docs/doc";
	final static String queries_address = "Queries/Q";
	final static String relevance_address = "RelevancyJudgments/relevance";

	final static int QUERY_QUANTITY = 225; // 225
	final static int DOC_QUANTITY = 1400; // 1400
	final static String cranFilePath = "./cran.all.1400";
	final static String indexLocation = "/run/media/ana/New Volume/sharif/term7/JavaCodes/data/Index";
	final static String indexLocation2 = "/run/media/ana/New Volume/sharif/term7/JavaCodes/data/Index2";
	final static Version LUCENE_VERSION = Version.LUCENE_4_10_2;
	final static String books_folder = "/run/media/ana/New Volume1/sharif/term7/JavaCodes/data/files2";
	JasonHelper js = new JasonHelper();
	
	static SnowballAnalyzer analyzer = new SnowballAnalyzer(LUCENE_VERSION, "English", StandardAnalyzer.STOP_WORDS_SET );
	
	@SuppressWarnings("deprecation")
	public void index(boolean use_pr) throws IOException, ParseException, org.apache.lucene.queryparser.surround.parser.ParseException {	

	    IndexWriterConfig index_writer_config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
	    Directory directory;
	    if (use_pr)
	    	directory = FSDirectory.open(new File( indexLocation2 ));
	    else
	    	directory = FSDirectory.open(new File( indexLocation ));
	    
	    IndexWriter index_writer = new IndexWriter(directory, index_writer_config);
	    
	    File books_dir = new File (books_folder);
	    File[] f = books_dir.listFiles();
	    
	    
	    for (int i = 0; i < f.length ; i++){
	    	System.out.println("lucene: file name " + f[i].getName());
	    	
	    	Document doc = new Document(); 	
	    	Data read_data = js.read(f[i].getName());
	    	float cons_coof = read_data.getScore();
	    	if (use_pr == true){
	    		JasonHelper js = new JasonHelper();
	    		ArrayList<String> seen = js.read_seen_string();
	    		String data_link = "";
	    		
	    		
	    		URL url = new URL( read_data.geturl());
				data_link = url.getPath();
	    		
				
				
	    		List<Double> prs = js.read_eigen_string();
	    		int indx = seen.indexOf(data_link);
	    		
	    		Double pr;
	    		if(indx != -1){
	    			pr = prs.get(indx) * 10;
	    			float pr_float = (float)pr.doubleValue();
		    		cons_coof = cons_coof * pr_float;
	    		}
	    			
	    		else
	    			System.out.println("ketab mabud!!");
	    		
	    	}
	    	
	    	TextField tf1 = new TextField( "title", read_data.getName(), Field.Store.YES );
	    	TextField tf2 = new TextField( "intro", read_data.getIntro(), Field.Store.YES );
	    	TextField tf3 = new TextField( "author", read_data.authors_string(), Field.Store.YES );
	    	TextField tf4 = new TextField( "cm", read_data.comment_string(), Field.Store.YES );
	    	tf1.setBoost(3 * cons_coof); tf2.setBoost((float)1.5 * cons_coof); 
	    	tf3.setBoost((float) 2.5 * cons_coof); tf4.setBoost(1* cons_coof);
	    	doc.add(tf1); doc.add(tf2); doc.add(tf3); doc.add(tf4);

	    //	doc.setBoost(read_data.getScore());
	    	index_writer.addDocument(doc);
	    
		}
		
		index_writer.close();
		System.out.println("Indexing Completed");
		
		
	}
	
	public ArrayList<Data> query_parser (String query, boolean pr) throws IOException, ParseException{
		
		String str_in = query;
		System.out.println(indexLocation);
		
		String loc = "";
		if (pr)
			loc = indexLocation;
		else
			loc = indexLocation2;
				
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(loc)));
		IndexSearcher searcher = new IndexSearcher(reader);
		ArrayList<String> seen_title = new ArrayList<String>();
		
	    //Query q = new QueryParser("body", analyzer).parse(str_in);
		Query q = new MultiFieldQueryParser
				(new String[]{"title", "author", "intro", "cm"}, analyzer).parse(str_in);
	    
	    // Score with TopFieldCollector
	    Sort sort = new Sort(SortField.FIELD_SCORE);
	    TopFieldCollector topField = TopFieldCollector.create(sort, 10, true, true, true, false);
	    searcher.search(q, topField);   
	    ScoreDoc[] hits = topField.topDocs().scoreDocs; 
	    
        
	    //make a list of Data object from results.
	    ArrayList<Data> res_datas = new ArrayList<Data>();
	    int id = hits[0].doc;
	    
	    for( int i=0;i < hits.length; ++i) {
	          try {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				
				Data read_data = js.read(d.get("title"));
				
				if (read_data != null && !seen_title.contains(read_data.getName())){
					seen_title.add(read_data.getName());
					res_datas.add(read_data);
				}
			} catch (Exception e) {
				System.out.println("moshkel dar khundan pish umad valla be khoda ");
			}
	    }
	    return res_datas;
	    
	}
}