import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import java.util.Arrays;
import java.lang.Math;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;
import java.util.Collections;

public class Perceptron{
  public int dim;
  public int update_time;
  public Perceptron(int d, int u){
    this.dim = d;
    this.update_time = u;
  }
	public List<String> readFile(String FileName){
	 FileInputStream file = null;
     InputStreamReader isr = null;
     BufferedReader br = null;
     List<String> examples = new ArrayList<String>();
     try{
     	file = new FileInputStream(FileName);
		isr = new InputStreamReader(file);
        br = new BufferedReader(isr);
        String str = null;
        StringBuilder add = new StringBuilder("");
        while((str = br.readLine()) != null){
        	add.setLength(0);
            String[] tmp = str.split(" ");
            if(tmp[0].equals("+1"))
            	add.append('1');
            else
            	add.append('0');
            for(int i = 1; i <= dim; i ++)
                add.append('0');
            for(int i = 1; i < tmp.length; i ++){
                int index = Integer.parseInt(tmp[i].substring(0, tmp[i].indexOf(':')));
                add.setCharAt(index, '1');
            }
            examples.add(add.toString());
        }
     }catch (FileNotFoundException e) {
         System.out.println("can't find file");
        }catch (IOException e) {
        System.out.println("read/write fails");
        } finally {
          try {
           br.close();
           isr.close();
           file.close();
       } catch (IOException e) {
         e.printStackTrace();
       }
       return examples;
     }

	}
	public List<Double> produceW(List<String> table, double r, int dimension, List<Double> w){
    //for(Double w1: w)
      //System.out.print(w1 + " ");
    //System.out.println();
	  int cnt_0 = 0;
	  for(String i : table){
	    int y = i.charAt(0) == '1'? 1 : -1;
        i = i.substring(1) + "1";
        double sum = 0;
        for(int j = 0; j < w.size(); j ++){
            double t = (i.charAt(j) == '1')? 1 : 0;
        	sum += w.get(j) * t;
        }
        sum *= y;
        if(sum < 0 || (sum == 0 && y == -1)){
            cnt_0 ++;
        	for(int k = 0; k < w.size(); k ++){
        		double t = i.charAt(k) == '1'? 1 : 0;
        		w.set(k, w.get(k) + r * y * t);
        }
       }
      }
     update_time += cnt_0;
     //System.out.println("Update Times:" + cnt_0); 
     return w;
	}
	public double getAccuracy(List<Double> w, List<String> table){
	  double b = w.get(w.size() - 1);
      //System.out.println(b);
	  w.remove(w.size() - 1);
	  int error_num = 0;
	  for(String tmp : table){
	  int y = tmp.charAt(0) == '1'? 1 : -1;
	  tmp = tmp.substring(1);
	  double sum = 0;
	  for(int i = 0; i < w.size(); i ++){
	  	double t = tmp.charAt(i) == '1' ? 1:0;
         sum += w.get(i) * t;
     }
     sum += b;
     if(sum * y < 0 || (sum == 0 && y == -1))
       error_num ++;
	 }
     w.add(b);
	 return error_num * 1.0 / table.size();
	}
  public void run(int epo, boolean flag, String train_filename, String test_filename, int exnum){
    //dim = 123;
    int a = 0;
    Double[] rr = {1.0, 0.1, 0.01, 0.001};
    if(!test_filename.equals("table2.txt"))
      a = 4;
    else
      a = 1;
    for(int m = 0; m < a; m ++){
      Double r = rr[m];
      Double ans = 0.0, ans1 = 0.0;
      double[] ww = new double[dim + 1];
     update_time = 0;
     for(int k = 1; k <= exnum; k ++){
      List<String> table = readFile(train_filename);
      List<Double> w = new ArrayList();
      for(int i = 1; i <= dim + 1; i ++)
        w.add(Math.random() * 2 - 1);
      if(flag){
       List<String> tmp = new ArrayList<String>(table);
       for(int epoch = 1; epoch <= epo; epoch ++){
        if(epoch != 1)
         Collections.shuffle(tmp);
        w = produceW(tmp, r, dim, w);
       }
      }
      else
       w = produceW(table, r, dim, w);
      for(int i = 0; i < w.size(); i ++)
        ww[i] += w.get(i);
      ans += getAccuracy(w, table);     
      List<String> table_test = readFile(test_filename);
      ans1 += getAccuracy(w, table_test);    
     }
     //System.out.println(ww.length);
     if(dim == 4)
     for(double x : ww)
      System.out.print(x / exnum + " ");
     System.out.println();
     System.out.println("update time:" + update_time / exnum);
     System.out.println("r:" + r + "train accuracy:" + (1 - ans / exnum));
     System.out.println("r:" + r + " test accuracy:" + (1 - ans1 / exnum) + "\n");
    }
  }
	/*public static void main(String[] args) {

	}*/

}