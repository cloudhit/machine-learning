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
import java.util.Map;
import java.util.Collections;
import java.io.*;

public class decisionTree{
	public decisionTreeNode root = null;
	public List<List<Double>> table = null;
    public int max_depth;
    public decisionTree(int depth){
        this.max_depth = depth;
    }
    public decisionTreeNode buildDecisionTree(List<List<Double>> table, String set, double entropy, ArrayList<Integer> attribute, int depth, String flag){
        if(entropy == 0 || max_depth <= depth || set.length() == 0 || attribute.size() == 0){
            decisionTreeNode leaf = new decisionTreeNode();
            if(set.length() == 0)
             leaf = new decisionTreeNode(flag);
            else
             leaf = new decisionTreeNode(leaf.count(table, set));
             return leaf;
        }        //choose the largest information gain
        double[][] entropies = new double[attribute.size()][];
        double max = 0;
        int cur = -1;
        int attribute_cur = -1;
        decisionTreeNode[] node = new decisionTreeNode[attribute.size()];
        for(int i = 0; i < attribute.size(); i ++){
            node[i] = new decisionTreeNode(String.valueOf(attribute.get(i)));
            entropies[i] = node[i].calEntropy(table, set);
            double information_gain = entropy - entropies[i][entropies[i].length - 1];
            if(max <= information_gain){
                max = information_gain;
                cur = i;
                attribute_cur = attribute.get(i);
            }
        }
        String[] subsets = node[cur].classify(table, set);
        int p = 0;
        
        for(int i = 0; i < 2; i++){
            attribute.remove(cur);
            decisionTreeNode child = buildDecisionTree(table, subsets[i], entropies[cur][i], attribute, depth + 1, node[cur].count(table, set));
            attribute.add(cur, attribute_cur);
            node[cur].children.add(child);

        }
        return node[cur];
    }
    public List<List<Double>> readFile(String fileName){
     FileInputStream file = null;
     InputStreamReader isr = null;
     BufferedReader br = null;
     List<List<Double>> examples = new ArrayList();
     try{
        file = new FileInputStream(fileName);
        isr = new InputStreamReader(file);
        br = new BufferedReader(isr);
        String str = null;
        while((str = br.readLine()) != null){
          String[] att = str.split(" ");
          List<Double> example = new ArrayList();
          example.add(Double.parseDouble(att[0]));
          for(int i = 1; i < att.length; i ++){
            double x = Double.parseDouble(att[i].split(":")[1]);
            example.add(x);
           }
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
    public void BuildTree(){
        List<List<Double>> tmp = readFile("badges-train-features.txt");
        Collections.shuffle(tmp);
        
        table = new ArrayList();
        for(int i = 0; i < tmp.size() /2 ; i ++)
            table.add(tmp.get(i));
        double entropy_s = initialEntropy(table);
        String set = "";
        int table_size = table.size();
        for(int i = 1; i < table_size; i ++)
        	set += i + "&";
        set += table_size;
        ArrayList<Integer> attribute = new ArrayList<Integer>();
        for(int i = 1; i <= 270; i ++)
            attribute.add(i);
        this.root = buildDecisionTree(table, set, entropy_s, attribute, 1, "");
    }

    public double initialEntropy(List<List<Double>> table){
    	int total = table.size(), cnt = 0;
        for(int i = 0; i < total; i ++)
            if(table.get(i).get(0) > 0)
                cnt ++;
        double t1 = 1.0 * cnt / total, t2 = 1.0 * (total - cnt) / total;
        return -1 * t1 * (Math.log(t1)/Math.log(2)) - t2 * (Math.log(t2)/Math.log(2));
    }
    //predict the test data
    public double makeJudgement(){
        List<List<Double>> tab = readFile("badges-test-features.txt");
        int correct = 0;
        for(List<Double> i : tab)
        {
            if((this.root.DFS(i).equals("+") && i.get(0) > 0) || (this.root.DFS(i).equals("-") && i.get(0) <= 0))
                correct ++;
           
        }
        //System.out.println("test number:" + tab.size());
        //System.out.println("accuracy:" + (correct * 1.0 /tab.size()));
        return correct * 1.0/tab.size();
    }
    public static void main(String[] args) {
    	//BuildTree();
    }
}