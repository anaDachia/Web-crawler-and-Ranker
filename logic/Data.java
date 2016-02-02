package logic;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

	private String name;
	private ArrayList<String> authors;
	private ArrayList<String> comments;
	private float score;
	private String intro;
	private String url;
	
	public Data(){	
	}
	
	public Data(String name,String intro,float score, ArrayList<String> authors, ArrayList<String> comments, String url){
		this.name = name;
		this.intro = intro;
		this.score= score;
		this.authors = authors;
		this.comments = comments;
		this.url = url;
	}
	public String getName(){
		return this.name;
	}
	public ArrayList<String> getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}
	public String authors_string (){
		String res = "";
		for (int i= 0; i < this.authors.size(); i ++){
			res = res + " " +this.authors.get(i);
		}
		return res;
	}
	public ArrayList<String> getComments() {
		return comments;
	}
	public String comment_string(){
		String res = "";
		for (int i= 0; i < this.comments.size(); i ++){
			res = res + " " + this.comments.get(i);
		}
		return res;
	}
	public void setComments(ArrayList<String> comments) {
		this.comments = comments;
	}
	public float getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String geturl() {
		return url;
	}
	public void seturl(String intro) {
		this.url = intro;
	}
	@Override
	public String toString() {
		return "Data [name=" + name + ", authors=" + authors + ", comments="
				+ comments + ", score=" + score + ", intro=" + intro + "]";
	}
}
