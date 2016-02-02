package logic;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageRank {
	
		private ArrayList<String> seen_url;
		private double[][] matrix_ana;
		private double[] start_ana;
		private List<Double> principalEigenvector;
		String domain = "";
		
		
		public PageRank(ArrayList<String> seen_url, String domain) {
		super();
		this.domain = domain;
		this.seen_url = seen_url;
		matrix_ana = new double[seen_url.size()][seen_url.size()];
		start_ana = new double[seen_url.size()];
		}


		public void build_adj_matrix() {
			Document doc = null;
			System.out.println("dar page rank va size seen ha :" + seen_url.size());
			System.out.println((double) (1/ seen_url.size()));

			for (int i =0; i < this.seen_url.size(); i ++){
				String tmp = domain + seen_url.get(i);
				
				//fetch ith url
				try {
					System.out.println(tmp);
					doc = Jsoup.connect(tmp).timeout(1000*1000).get();
				} catch (IOException e) {
					System.out.println(i + "IO avalie");
					long t= System.currentTimeMillis();
					long end = t+15000;
					boolean finish = true;
					while(System.currentTimeMillis() < end && finish) {
						try {
							doc = Jsoup.connect(tmp).timeout(1000*1000).get();
							System.out.println("ok shod");
							finish = false;
						} catch (IOException e1) {
							
						}
					}
					if (finish){
						System.out.println(i + "IO excepion shod fuck");
						continue;
					};
				}
				
				
				//extract links and set them in matrix
				Elements quest =  doc.select("a[href]");
				String link_str;
			
				for (Element link: quest){
					
					link_str = link.attr("href");
					
					int ind = seen_url.indexOf(link_str);
					
					//vojud dasht!
					if(ind != -1){
						matrix_ana[i][ind] = 1;
						//System.out.println("az" + seen_url.get(i) + "  be  " + seen_url.get(ind));
					}				
					
				}
			}
			
			System.out.println("now compute final p");
			comp_final_P();
		}

		private void comp_final_P(){
			for (int i = 0; i < matrix_ana.length; i++) {
				for (int j = 0; j < matrix_ana[i].length; j++) {
					matrix_ana[i][j] = matrix_ana[i][j]*0.9 + 0.1*((double)(1 / matrix_ana.length));
				}
			}
		}
		
		public double get_PageRank_URL(String url){
			int ind = this.seen_url.indexOf(url);
			return principalEigenvector.get(ind);
			
		}
		
		public String get_max_PageRank(){
			double max = -1000;
			int max_ind = -1;
			for (int i = 0; i < principalEigenvector.size(); i++) {
				double num = principalEigenvector.get(i);
				if ( num > max){
					max = num;
					max_ind = i;
				}
			}
			return this.seen_url.get(max_ind);
		}
		
		
		public String get_min_PageRank(){
			double min = -1000;
			int min_ind = -1;
			for (int i = 0; i < principalEigenvector.size(); i++) {
				double num = principalEigenvector.get(i);
				if ( num < min){
					min = num;
					min_ind = i;
				}
			}
			return this.seen_url.get(min_ind);
		}
		
		
		public void comp_eigen(){
			DoubleMatrix matrix = new DoubleMatrix(this.matrix_ana);
			//ComplexDoubleMatrix eigenvalues = Eigen.eigenvalues(matrix);
			principalEigenvector = getPrincipalEigenvector(matrix);
			principalEigenvector = normalised(principalEigenvector);
			JasonHelper js = new JasonHelper();
			js.write_eigen(principalEigenvector);
		}
		private List<Double> getPrincipalEigenvector(DoubleMatrix matrix) {
		    int maxIndex = getMaxIndex(matrix);
		    ComplexDoubleMatrix eigenVectors = Eigen.eigenvectors(matrix)[0];
		    return getEigenVector(eigenVectors, maxIndex);
		}
		private int  getMaxIndex(DoubleMatrix matrix) {
		    ComplexDouble[] doubleMatrix = Eigen.eigenvalues(matrix).toArray();
		    int maxIndex = 0;
		    for (int i = 0; i < doubleMatrix.length; i++){
		        double newnumber = doubleMatrix[i].abs();
		        if ((newnumber > doubleMatrix[maxIndex].abs())){
		            maxIndex = i;
		        }
		    }
		    return maxIndex;
		}
		 
		private List<Double> getEigenVector(ComplexDoubleMatrix eigenvector, int columnId) {
		    ComplexDoubleMatrix column = eigenvector.getColumn(columnId);
		 
		    List<Double> values = new ArrayList<Double>();
		    for (ComplexDouble value : column.toArray()) {
		        values.add(value.abs()  );
		    }
		    return values;
		}
		private List<Double> normalised(List<Double> principalEigenvector) {
		    double total = sum(principalEigenvector);
		    List<Double> normalisedValues = new ArrayList<Double>();
		    for (Double aDouble : principalEigenvector) {
		        normalisedValues.add(aDouble / total);
		    }
		    return normalisedValues;
		}
		 
		private double sum(List<Double> principalEigenvector) {
		    double total = 0;
		    for (Double aDouble : principalEigenvector) {
		        total += aDouble;
		    }
		    return total;
		}
		
}