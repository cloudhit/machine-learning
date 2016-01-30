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

public class solution{
    public static void output(decisionTree[] tree, String fileName, String outputfile){
        List<List<Double>> table = tree[0].readFile(fileName);
        int correct = 0;
        try{
        File out_file = new File(outputfile);
        out_file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(out_file));
        for(List<Double> i : table)
        {
          StringBuilder str = new StringBuilder("");
          str.append(String.valueOf(i.get(0)) + " ");
          for(int j = 1; j <= tree.length; j ++){
          	if(tree[j - 1].root.DFS(i).equals("+"))
          		str.append(j + ":1");
          	else
          		str.append(j + ":-1");
          	if(j != tree.length)
          		str.append(" ");
          	else
          		str.append("\n");
          }
          out.write(str.toString());
          out.flush();
        }
        out.close();
       }catch(Exception e){
       	e.printStackTrace();
       }
    }
	public static void main(String[] args) {
    System.out.println("problem 3.2:");
    SVM p = new SVM(270, "badges-train-features.txt", "badges-test-features.txt");
    System.out.println("The cross validation accuracies of SVM with different pairs of parameters on the original file:");
    p.run(10, true, 1.0, 1.0);
    System.out.println("The accuracy on the test set of SVM with best parameters on the original file:");
    double ans = 0.0;
    for(int i = 0; i < 10; i ++)
     ans += p.run(30, false, 0.001, 10.0);
    System.out.println("average accuracy on the test set with the best parameters:" + ans / 10); 
    
    System.out.println("problem 3.3(The tree depth is 4):");
		decisionTree[] tree = new decisionTree[100];
		double mean = 0.0;
		double[] values = new double[100];
		for(int i = 0; i < 100; i ++){
		 tree[i] = new decisionTree(4);
		 tree[i].BuildTree();
		 values[i] = tree[i].makeJudgement();
		 mean += values[i];
		}
		mean /= 100;
		double var = 0.0;
		for(int i = 0; i < 100; i ++)
         var += Math.pow(values[i] - mean, 2);
        var /= 100;
		System.out.println("The mean accuracy of the 100 decision trees:" + mean);
		System.out.println("The variance of the accuracy: " + var);
		output(tree, "badges-train-features.txt", "new-train.txt");
		output(tree, "badges-test-features.txt", "new-test.txt");
    SVM p4 = new SVM(100, "new-train.txt", "new-test.txt");
    System.out.println("The cross validation accuracies of SVM with different pairs of parameters on the new file:");
    p4.run(10, true, 1.0, 1.0);
    System.out.println("The accuracy on the test set of SVM with best parameters on the new file:");
    ans = 0.0;
    for(int i = 0; i < 10; i ++)
     ans += p4.run(30, false, 0.01, 10.0);
    System.out.println("average accuracy on the test set with the best parameters:" + ans / 10); 

    System.out.println("problem 3.4(The tree depth is 8):");
    tree = new decisionTree[100];
    mean = 0.0;
    values = new double[100];
    for(int i = 0; i < 100; i ++){
     tree[i] = new decisionTree(8);
     tree[i].BuildTree();
     values[i] = tree[i].makeJudgement();
     mean += values[i];
    }
    mean /= 100;
    var = 0.0;
    for(int i = 0; i < 100; i ++)
         var += Math.pow(values[i] - mean, 2);
        var /= 100;
    System.out.println("The mean accuracy of the 100 decision trees:" + mean);
    System.out.println("The variance of the accuracy: " + var);
    output(tree, "badges-train-features.txt", "new-train.txt");
    output(tree, "badges-test-features.txt", "new-test.txt");
    SVM p8 = new SVM(100, "new-train.txt", "new-test.txt");
    System.out.println("The cross validation accuracies of SVM with different pairs of parameters on the new file:");
    p8.run(10, true, 1.0, 1.0);
    System.out.println("The accuracy on the test set of SVM with best parameters on the new file:");
    ans = 0.0;
    for(int i = 0; i < 10; i ++)
     ans += p8.run(30, false, 0.001, 10.0);
    System.out.println("average accuracy on the test set with the best parameters:" + ans / 10); 

    System.out.println("problem 3.4(The tree depth is 20):");
    tree = new decisionTree[100];
    mean = 0.0;
    values = new double[100];
    for(int i = 0; i < 100; i ++){
     tree[i] = new decisionTree(20);
     tree[i].BuildTree();
     values[i] = tree[i].makeJudgement();
     mean += values[i];
    }
    mean /= 100;
    var = 0.0;
    for(int i = 0; i < 100; i ++)
         var += Math.pow(values[i] - mean, 2);
        var /= 100;
    System.out.println("The mean accuracy of the 100 decision trees:" + mean);
    System.out.println("The variance of the accuracy: " + var);
    output(tree, "badges-train-features.txt", "new-train.txt");
    output(tree, "badges-test-features.txt", "new-test.txt");
    SVM p20 = new SVM(100, "new-train.txt", "new-test.txt");
    System.out.println("The cross validation accuracies of SVM with different pairs of parameters on the new file:");
    p20.run(10, true, 1.0, 1.0);
    System.out.println("The accuracy on the test set of SVM with best parameters on the new file:");
    ans = 0.0;
    for(int i = 0; i < 10; i ++)
     ans += p20.run(30, false, 0.001, 1.0);
    System.out.println("average accuracy on the test set with the best parameters:" + ans / 10);
	}
}