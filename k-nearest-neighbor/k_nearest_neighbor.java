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

public class k_nearest_neighbor{
	public static class tmp{
		public int distance = 0;
		public int index = -1;
		public tmp(int distance, int index){
			this.distance = distance;
			this.index = index;
		}
	}
	public static void main(String[] args) {
		int[] k = {1,2,3,4,5};
		NameToFeat nt = new NameToFeat();
		nt.covToFeat("badges-train.txt","train.txt");
		String[] train = nt.sendToMemory("train.txt", 200);
        nt.covToFeat("badges-test.txt", "test.txt");
        String[] test = nt.sendToMemory("test.txt", 94);
        int error = 0;
        for(int m = 0; m < k.length; m ++){
       	  ArrayList<tmp> kgroup = new ArrayList();
          for(int i = 0; i < test.length; i ++){
        	 for(int j = 0; j < train.length; j ++){
        		int distance = 0;
        		String[] t1 = test[i].split(" "), t2 = train[j].split(" ");
        		for(int n = 1; n <= 4; n++)
        		 if(!t1[n].equals(t2[n]))
                   distance ++;
            boolean flag = false;
            int size = kgroup.size();
            for(int k_index = 0; k_index < size; k_index ++){
              if(kgroup.get(k_index).distance >= distance){
             	 tmp t = new tmp(distance, j);
             	 kgroup.add(k_index, t);
             	 flag = true;
             	 break;
              }
            }
            if(!flag && kgroup.size() < k[m])
             kgroup.add(new tmp(distance, j));
            if(kgroup.size() > k[m])
             kgroup.remove(kgroup.size() - 1);      
          }            
         int cnt = 0;
         for(int j = 0; j < kgroup.size(); j ++){
         	if(train[kgroup.get(j).index].charAt(0) == '+')
              cnt++;
         }
         char pred = (cnt > kgroup.size()/2)? '+':'-'; 
         if(pred != test[i].charAt(0))
         	error ++;
       }
       System.out.println(error*1.0/94);
       error = 0;
      }
	}
}