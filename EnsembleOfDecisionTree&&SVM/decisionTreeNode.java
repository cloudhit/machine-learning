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
import java.io.*;

public class decisionTreeNode{
	public String attributeName;
    public List<decisionTreeNode> children = null;
	public decisionTreeNode(){};
	public decisionTreeNode(String name){
		this.attributeName = name;
        this.children = new ArrayList<decisionTreeNode>();
	}
    public String DFS(List<Double> list){
            if(this.attributeName.equals("+") || this.attributeName.equals("-"))
                return this.attributeName;
            double value = list.get(Integer.parseInt(this.attributeName));
            decisionTreeNode child = this.children.get((int)(value));
            return child.DFS(list);
    }
	public String[] classify(List<List<Double>> table, String set){
         String[] tmp = set.split("&");
         String[] ans = new String[2];
         ans[0] = ""; ans[1] = "";
         int index = Integer.parseInt(attributeName);
         for(int i = 0; i < tmp.length; i ++){
            int id = Integer.parseInt(tmp[i]) - 1;
            if(table.get(id).get(index) > 0)
             ans[1] += tmp[i] + "&";
            else
             ans[0] += tmp[i] + "&";   
         }
        if(ans[0].length() > 0)
            ans[0] = ans[0].substring(0, ans[0].length() - 1);
        if(ans[1].length() > 0)
            ans[1] = ans[1].substring(0, ans[1].length() - 1);
        return ans;
	}
    public double[] calEntropy(List<List<Double>> table, String set){     
        String[] subsets = classify(table, set);        
        double[] ans = new double[subsets.length + 1];
        int set_total = set.split("&").length;
        for(int i = 0; i < subsets.length; i ++){
            String tmp = subsets[i];
            if(tmp.length() == 0 || tmp == null){
                ans[i] = 0;
                continue;
            }
            String[] example = tmp.split("&");
            int total = example.length, cnt = 0;
            for(String exam : example){
             if(table.get(Integer.parseInt(exam) - 1).get(0) > 0)
                cnt ++;
            }
            double p_t = 1.0 * cnt/total, p_f = 1.0 * (total - cnt)/total;      
            ans[i] = (p_t == 0 || p_f == 0) ? 0: -1.0 * p_t * (Math.log(p_t)/Math.log(2)) - 1.0 * p_f * (Math.log(p_f)/Math.log(2));
            ans[subsets.length] += 1.0 * total/set_total * ans[i];
        }
        return ans;
    }
    public String count(List<List<Double>> table, String set){
        String[] tmp = set.split("&");
        int cnt = 0, total = tmp.length;
        for(String i:tmp)
            if (table.get(Integer.parseInt(i) - 1).get(0) > 0)
                cnt ++;
        return (cnt >= total /2)? "+":"-";
    }

}