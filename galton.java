package edu.princeton.cs.algs4;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class galton {
  
  public static void main(String[] args) {
    try {
      PrintStream myconsole = new PrintStream(new File("edu/princeton/cs/algs4/galton.txt"));
      System.setOut(myconsole);
      int n = 1000000000;
      double alpha = 1.5;
      double gamma = 0.75;
      double delta = 0.001;
      int samplesize = 100;
      int sizeX = 10;
      int k = 0;
      double[] sumArray = new double[samplesize];
      while (k < samplesize) {
        Graph G = GaltonWatson(alpha);
        double funVal = sumLeaf(G,n,alpha,gamma,delta);
      if (funVal > 0) 
        {
          sumArray[k] = funVal;
          k++;
        }
      }
      double x0 = sumArray[0];
      double[] x = new double[sizeX];
      double increment = (100*x0-x0/100)/sizeX;
      double start = x0 - increment*(sizeX/2);
      for(int j = 0; j < sizeX; j++) {
        x[j] = start + increment*j;
      }
      for(int i = 0; i < sizeX; i++) {
        System.out.println(x[i]);
      }
    }
    catch (FileNotFoundException fx) { System.out.println(fx);}
  }

  public static Graph GaltonWatson(double alpha) 
  {
    double c = 1/Riemann(alpha);
    double d = 1 - c*Riemann(alpha+1);
    Graph G = new Graph(1);
    boolean bool = true;
    boolean[] covered = new boolean[100000];
    while(bool) 
    {
      bool = false;
      if(G.V() == 1) 
      {
        int id = invDist(Math.random(),d,c,alpha);
        if (id > 0) bool = true;
        covered[0] = true;
        while(id > 0) 
        {
          id--;
          G.addVertex();
          G.addEdge(0,G.V()-1);
        }
      }
      else 
      {
        for (int w: G.leaves()) {
          if(covered[w] == true) {}
          else {
            covered[w] = true;
            int id = invDist(Math.random(),d,c,alpha);
            if (id > 0) bool = true;
            while(id > 0) 
            {
              id--;
              G.addVertex();
              G.addEdge(w,G.V()-1);
            }
          }
        }
      }
    }
    return G;  
  }
  
  public static double sumLeaf(Graph T, int n, double alpha, double gamma, double delta) 
  {
    double mysum = 0;
    for (int u : T.leaves()) {
      double tempsum = 0;
      for (int v : T.pathToRoot(u)) {
        if (T.degree(v)-1 <= delta*Math.pow(n,1/alpha)) {
          tempsum += Math.pow((T.degree(v)-1)/Math.pow(n,1/alpha),gamma);
        }
      }
      if(mysum < tempsum) {
        mysum = tempsum;
      }
    }
    return mysum;
  }
  
  public static double Riemann(double s) 
  {
    double sum = 0;
    for (int i = 1; i < 1000000; i++)
    {
      sum += Math.pow(i,-s);
    }
    return sum;
  }
  /** A function which recieves a random number in (0,1) 
    *  and returns the integer corresponding to the distribution which we defined.
    */
  public static int invDist(double r, double d, double c, double alph) 
  {
    double test = d;
    int j = 0;
    while(r > test) 
    {
      j++;
      test += c*Math.pow(j,alph+1);
    }
    return j;
  }
  
}