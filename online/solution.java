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

public class solution{
	public static void main(String[] args) {
		//3.1 Perceptron
		//Perceptron p1 = new Perceptron(4, 0);
		//Perceptron p2 = new Perceptron(123, 0);
		MarginPerceptron p3 = new MarginPerceptron(123, 0);
		AggressivePerceptron p4 = new AggressivePerceptron(123, 0);

		System.out.println("Problem 3.1:");
		p1.run(5, false, "table2.txt", "table2.txt", 3);
		System.out.println();

		System.out.println("Problem 3.2 Perceptron algorithm:");
		p2.run(5, false, "a1a.train.txt", "a1a.test.txt", 100);
		System.out.println();

        System.out.println("Problem 3.2 MarginPerceptron algorithm:");	
		p3.run(5, false);
		System.out.println();

		System.out.println("Problem 3.3 Perceptron algorithm with 3 epochs:");
		p2.run(3, true, "a1a.train.txt", "a1a.test.txt", 100);
		System.out.println();

		System.out.println("Problem 3.3 Perceptron algorithm with 5 epochs:");
		p2.run(5, true, "a1a.train.txt", "a1a.test.txt", 100);
		System.out.println();

 		System.out.println("Problem 3.3 MarginPerceptron algorithm with 3 epochs:");
		p3.run(3, true);
		System.out.println();

 		System.out.println("Problem 3.3 MarginPerceptron algorithm with 5 epochs:");
		p3.run(5, true);
		System.out.println();

		System.out.println("Problem 3.4 AggresivePerceptron algorithm without shuffling:");
		p4.run(5, false);
		System.out.println();

		System.out.println("Problem 3.4 AggresivePerceptron algorithm with 3 epochs:");
		p4.run(3, true);
		System.out.println();

        System.out.println("Problem 3.4 AggresivePerceptron algorithm with 5 epochs:");
		p4.run(5, true);
		System.out.println();

	}
}