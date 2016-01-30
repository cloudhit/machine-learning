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
import java.util.Collections;
import java.io.*;

public class SVM{
  public int dim;
  public int global_t;
  public String file_name_in;
  public String file_name_out;
  public SVM(int d, String file_name_in, String file_name_out){
    this.dim = d;
    this.file_name_in = file_name_in;
    this.file_name_out = file_name_out;
  }
  public List<List<Double>> readFile(String FileName){
     FileInputStream file = null;
     InputStreamReader isr = null;
     BufferedReader br = null;
     List<List<Double>> examples = new ArrayList();
     try{
        file = new FileInputStream(FileName);
        isr = new InputStreamReader(file);
        br = new BufferedReader(isr);
        String str = null;
        while((str = br.readLine()) != null){
          String[] att = str.split(" ");
          List<Double> example = new ArrayList();
          example.add(Double.parseDouble(att[0]));
          for(int i = 1; i < att.length ; i ++){
            double x = Double.parseDouble(att[i].split(":")[1]);
             example.add(x);
           }
          example.add(1.0);
          examples.add(example);
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

  public List<Double> produceW(List<List<Double>> table, double c, int dimension, List<Double> w, double p0){
      for(List<Double> example : table){
       double r = p0/(1 + 1.0 * p0 * global_t / c);
       double y = example.get(0);
       double sum = 0.0;
       for(int i = 1; i <= dimension + 1; i ++)
        sum += w.get(i - 1) * example.get(i);
        sum *= y;
        List<Double> E = new ArrayList();
        if(sum <= 1.0){
         for(int j = 0; j < w.size(); j ++)
          E.add(w.get(j) - c*y*example.get(j + 1));
        }
        else
          E = w;
        for(int j = 0; j < w.size(); j ++)
          w.set(j, w.get(j) - r * E.get(j));
        global_t ++;
      }
     return w;
  }
  public double getAccuracy(List<Double> w, List<List<Double>> table){
   int err = 0;
   for(List<Double> example:table){
    double y = example.get(0);
    double ans = 0.0;
    for(int i = 1; i < example.size(); i ++)
      ans += example.get(i) * w.get(i - 1);
    if(ans * y < 0)
     err ++;
   }
   return 1 - err * 1.0 / table.size();
  }
  public double run(int epo, boolean flag, double p_best, double c_best){
   List<List<Double>> table = readFile(this.file_name_in); 
   List<Double> P = new LinkedList();
   List<Double> C = new LinkedList();
   if(flag){
    P.add(0.001);P.add(0.01);P.add(0.1);P.add(1.0);
    C.add(0.1);C.add(1.0); C.add(10.0); C.add(100.0); C.add(1000.0);
   }else{
       P.add(p_best);
       C.add(c_best);
   }   
   for(Double p0 : P){
    for(Double c : C){
      Double ans = 0.0;
      double[] ww = new double[dim + 1];
      int size = table.size();
      int part = size / 10;
      int remain = size - part * 10;
    int fold_num;
    if(flag) fold_num = 10;
    else fold_num = 1;

    for(int fold = 1; fold <= fold_num; fold ++){
      List<List<Double>> tmp_table = new ArrayList<List<Double>>();
      List<List<Double>> test_table = new ArrayList<List<Double>>();
     if(flag)
      for(int x = 0; x < size; x++){
        if(!(x >= (fold - 1) * part && x < fold * part))
          tmp_table.add(table.get(x));
        else
          test_table.add(table.get(x));
      }else{
       tmp_table = table;
       test_table = readFile(this.file_name_out); 
      }
      List<Double> w = new ArrayList();
      for(int i = 1; i <= dim + 1; i ++)
        w.add(0.0);
       List<List<Double>> tmp = new ArrayList<List<Double>>(tmp_table);
       global_t = 0;
       for(int epoch = 1; epoch <= epo; epoch ++){
         if(epoch != 1)
          Collections.shuffle(tmp);
          w = produceW(tmp, c, dim, w, p0);
      }
     if(flag)
      ans += getAccuracy(w, test_table); 
     else
      ans = getAccuracy(w, test_table);
     }
     if(flag)
     System.out.println("p0 = " + p0 + " C = " + c + " accuracy:" + ans / fold_num);       
     if(!flag)
      return ans / fold_num;
    }
   }
   return 0.0;
  }

}