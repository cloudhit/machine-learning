#!/bin/sh
cd code
  javac Perceptron.java 
  javac MarginPerceptron.java
  javac AggressivePerceptron.java
  javac solution.java
  java solution
  exit 1;
fi