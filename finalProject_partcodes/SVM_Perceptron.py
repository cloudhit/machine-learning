import csv
import string
import re
import os
import numpy as np
import random
import copy
data = []
test_set = []
bits = []
def convertForm(path):
    url = re.compile('[\",$\r\n\'()]')
    Revenue = []
    Expenses = []
    Assets = []
    Liabilities = []
    tmp_set = []
    in_pos_count = 0
    in_neg_count = 0
    out_pos_count = 0
    out_neg_count = 0
    pos_flag = 0
    neg_flag = 0
    for root, dirs, files in os.walk(path):
        flag = 0
        count = 0
        for fn in files:
            pos_flag = 0
            neg_flag = 0
            count += 1
            if count == 1:
                continue
            else:
                flag += 1
            tmp = root + "/" + fn
            if not 'Revenue_Expense' in fn: 
                for line in open(tmp):
                    arr = line.split(",\"")
                    if arr[0] == 'Total Assets':
                        Assets = arr[4:len(arr)]
                    elif arr[0] == 'Total Liabilities':
                        Liabilities = arr[4:len(arr)]
            else:
                for line in open(tmp):
                    arr = line.split(",\"")
                    if arr[0] == 'Total Revenue':
                        Revenue = arr[1:len(arr)]
                    elif arr[0] == 'Total Expenses':
                        Expenses = arr[1:len(arr)]
            if flag == 2:
                flag = 0
                data_tmp = []
                for i in range(0, len(Revenue)):
                    Revenue[i] = url.sub("", Revenue[i])
                    Expenses[i] = url.sub("", Expenses[i])
                    if i > 0:
                        data_tmp.append(float(Revenue[i]) / float(Expenses[i]))
                        data_tmp.append(float(Revenue[i]) - float(Expenses[i]))
                        if i < len(Revenue) - 1:
                            Revenue[i + 1] = url.sub("", Revenue[i + 1])
                            Expenses[i + 1] = url.sub("", Expenses[i + 1])
                            before = float(Revenue[i + 1]) - float(Expenses[i + 1]) + 1
                            after = float(Revenue[i]) - float(Expenses[i]) + 1
                            data_tmp.append((after - before) / before)
                    else:
                        data_tmp.append(float(Revenue[i]) / float(Expenses[i]))
                if data_tmp[0] > 1:
                    data_tmp[0] = 1
                    pos_flag = 1
                    print fn
                else:
                    neg_flag = 1
                    data_tmp[0] = -1
                for i in range(0, len(Assets)):
                    Assets[i] = url.sub("", Assets[i])
                    Liabilities[i] = url.sub("", Liabilities[i])
                i = 1
                while i < len(Assets):
                    before = float(Assets[i]) - float(Liabilities[i]) + 1
                    after = float(Assets[i + 1]) - float(Liabilities[i + 1]) + 1
                    data_tmp.append(float(Assets[i]) - float(Liabilities[i]))
                    data_tmp.append((float(Assets[i]) + 1) / (1 + float(Liabilities[i])))
                    data_tmp.append((after - before) / before)
                    i += 3
                #print fn, len(data_tmp)
                if pos_flag == 1 and in_pos_count < 50:
                    in_pos_count += 1
                    data.append(data_tmp)
                elif neg_flag == 1 and in_neg_count < 50:
                    in_neg_count += 1
                    data.append(data_tmp)
                elif pos_flag == 1 and out_pos_count < 23:
                    out_pos_count += 1
                    test_set.append(data_tmp)
                elif neg_flag == 1 and out_neg_count < 23:
                    out_neg_count += 1
                    test_set.append(data_tmp)
        print len(data), len(test_set), len(data[0])                   
def cross_v_test(w, data):
   error = 0
   for i in range(0, len(data)):
        tmp = copy.copy(data[i])
        tmp.append(1)
        x = np.array(tmp[1:len(tmp)])
        y = tmp[0]
        if y * np.dot(x, w) <= 0:
            error += 1
   return 1 - error / float(len(data))
def cross_v_SVM(data, p0, C):
   part = len(data) / 5
   accuracy = 0
   for t in range(0, 5):
        train_data = []
        test_data = []
        for j in range(0, len(data)):
            if j < (t + 1)*part and j >= t * part:
                test_data.append(data[j])
            else:
                train_data.append(data[j])
        w = np.array([0] * len(train_data[0]))
        t = 0
        T = 20
        for epoch in range(1, T + 1):
            random.shuffle(train_data)
            for i in range(0, len(train_data)):
                tmp = copy.copy(train_data[i])
                r = float(p0) / (1 + p0 * t / C)
                tmp.append(1)
                x = np.array(tmp[1:len(tmp)])
                y = tmp[0]
                if y * np.dot(w, x) <= 1: ·
                    E = w - C * y * x
                else:
                    E = w
                w = w - r * E
                t += 1
        accuracy += cross_v_test(w, test_data)     
   return accuracy / 5
def SVM_SGD(data, p0, C, T):
    w = np.array([0] * len(data[0]))
    t = 0
    for epoch in range(1, T + 1):
        random.shuffle(data)
        for i in range(0, len(data)):
            tmp = copy.copy(data[i])
            r = float(p0) / (1 + p0 * t / C)
            tmp.append(1)
            x = np.array(tmp[1:len(tmp)])
            y = tmp[0]
            if y * np.dot(w, x) <= 1:
                E = w - C * y * x
            else:
                E = w
            w = w - r * E
            t += 1
    return w

def cross_v_perceptron(data, u):
    part = len(data) / 5
    accuracy = 0
    T = 30
    for t in range(0, 5):
        train_data = []
        test_data = []
        for j in range(0, len(data)):
            if j < (t + 1)*part and j >= t * part:
                test_data.append(data[j])
            else:
                train_data.append(data[j])
        w = np.array([0] * len(train_data[0]))
        for epoch in range(1, T + 1):
            random.shuffle(train_data)
            for i in range(0, len(train_data)):
                tmp = copy.copy(train_data[i])
                tmp.append(1)
                x = np.array(tmp[1:len(tmp)])
                y = tmp[0]
                if y * np.dot(w, x) <= u:
                    r = (u - y*np.dot(w,x)) / np.dot(x, x)
                    w = w + r*y*x
        accuracy += cross_v_test(w, test_data)     
    return accuracy / 5
def perceptron(data, u, T):
    w = np.array([0] * len(data[0]))
    for epoch in range(1, T + 1):
        random.shuffle(data)
        for i in range(0, len(data)):
            tmp = copy.copy(data[i])
            tmp.append(1)
            x = np.array(tmp[1:len(tmp)])
            y = tmp[0]
            if y * np.dot(w, x) <= u:
                r = (u - y*np.dot(w,x)) / np.dot(x, x)
                w = w + r*y*x
    return w

convertForm("newdata")
print "SVM with different parameters p0 and C on the train set using cross validation"
t_arr = [1, 0.1, 0.01, 0.5, 0.001, 0.05]
C_arr = [0.1, 1, 10, 100, 1000]
max_ans = 0
max_index1 = -1
max_index2 = -2
for t in t_arr:
    for C in C_arr:
        ans = cross_v_SVM(data, t, C)
        print "p0:", t, "C:", C, "accuracy:", ans
        if ans > max_ans:
            max_ans = ans
            max_index1 = t
            max_index2 = C
print "SGD based SVM with parameters p0 = ", max_index1, " and C = ", max_index2, " on the test set with different epoches"
T_arr = [3, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50]
for T in T_arr:
    w = SVM_SGD(data, max_index1, max_index2, T)
    #print "w = ", w
    print "epoches:", T, "accuracy:", cross_v_test(w, test_set)

print "perceptron with different parameters u on the train set using cross validation"        
u_arr = [1,2,3,4,5,6,7,8]
max_ans = 0
max_index = -1
for u in u_arr:
    ans = 0
    for i in range(0, 10):
        ans += cross_v_perceptron(data, u)
    print "u:", u, "accuracy:",ans / 10
    if ans / 10 > max_ans:
        max_ans = ans/10
        max_index = u

T_arr = [3, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50]
print "perceptron with parameter u =", max_index, " and different epoches on test set" 
for T in T_arr:
    ans = 0
    for i in range(0, 10):
        w = perceptron(data, max_index, T)
        ans += cross_v_test(w, test_set)
        #print "w = ", w
    print "epoch:", T, "accuracy:", ans / 10
#print data
