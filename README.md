# SEEM5020: Algorithms for Big Data
This is the project of SEEM5020: Algorithms for Big Data in CUHK, 2023 Fall.
Click [here](https://www1.se.cuhk.edu.hk/~swang/course/SEEM5020.html) to visit the course website.

To run the code, I assume that you have already install all necessary Python packages and the right Java JDK.
```
cd Code/Datasets
python datasets_generating.py
cd ..
javac *.java
java MAIN
cd Datasets/Results
python results_analysing.py
```

In this course, we focused on important topics to deal with massive datasets. The topics covered basic tail bounds frequently used for approximation algorithm design, various sketches for efficient summarization of big data, similarity search techniques like LSH for large-scale data, and topics on dealing with matrix data and graph data. Next, we covered different models like the external memory model (for I/O algorithms), the MPC model (for distributed algorithms), the work-depth model (for shared-memory parallel algorithms), and the corresponding algorithms fitted to such modeling. Finally, some topics on sampling-based large-scale graph algorithm designs were covered.
