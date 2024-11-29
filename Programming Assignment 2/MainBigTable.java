import java.util.*;

interface IMemoizedStorage {
    public boolean hasKey(int key1, int key2);
    public void storeKey(int key1, int key2);
    public boolean removeKey(int key1, int key2);
}

class BigTable implements IMemoizedStorage {
    boolean[][] storage;
    public BigTable(int key1Size, int key2Size) {
        this.storage = new boolean[key1Size+1][key2Size+1];

        for (int i=0; i<key1Size+1; i++) {
            for (int j=0; j<key2Size+1; j++)  {
                this.storage[i][j] = false;
            }
        }
    }

    public boolean hasKey(int key1, int key2) {
        return this.storage[key1][key2];
    }

    public void storeKey(int key1, int key2) {
        this.storage[key1][key2] = true;
    }
    public boolean removeKey(int key1, int key2){
        boolean v = this.storage[key1][key2];
        this.storage[key1][key2] = false;
        return v;
    }
}

class BackTrackingSolver {
    int ferryLength;
    ArrayList<Integer> cars;
    IMemoizedStorage storageMemoization;

    int bestK;
    int[] currX;
    int[] bestX;

    int[] currS;

    public BackTrackingSolver(int ferryLength, ArrayList<Integer> cars, IMemoizedStorage storageMemoization) {
        this.cars = cars;
        this.ferryLength = ferryLength;
        this.storageMemoization = storageMemoization;
        this.bestK = 0;
        this.currX = new int[cars.size()];

        for (int i=0; i<cars.size(); i++) 
        {
            this.currX[i]=-1;
        }
        this.bestX = new int[cars.size()];

        this.currS = new int[cars.size()];
        if (cars.size() > 0) {
            this.currS[0] = cars.get(0);
            for (int i=1; i<cars.size(); i++){
                this.currS[i] = this.currS[i-1] + cars.get(i);
            } 
        }
    }

    public void backtrackSolve(int currK, int leftSpace) {
        if (currK > this.bestK) {
            this.bestK = currK;

            for (int i=0; i<currK; i++) {
                this.bestX[i] = currX[i];
            }
        }

        // this is after to handle the off by 1 error from 0 indexing
        if (currK >= this.cars.size()) return;

        int currentCarLength = this.cars.get(currK);

        if (leftSpace >= currentCarLength && !this.storageMemoization.hasKey(currK+1, leftSpace-currentCarLength)) {
            this.currX[currK] = 1;
            int newLeftSpace = leftSpace - currentCarLength;
            this.backtrackSolve(currK + 1, newLeftSpace);
            this.storageMemoization.storeKey(currK + 1, newLeftSpace);
        }

        int rightSpace = ferryLength * 2 - leftSpace;
        if (currK > 0) rightSpace-= this.currS[currK-1];


        if (rightSpace >= currentCarLength && !this.storageMemoization.hasKey(currK+1, leftSpace)) {
            currX[currK] = 0;
            this.backtrackSolve(currK+1, leftSpace);
            this.storageMemoization.storeKey(currK + 1, leftSpace);
        }
    }

    class SolutionStruct {
        public int bestK;
        public int[] bestX;
        public SolutionStruct(int bestK, int[] bestX) {
            this.bestK = bestK; this.bestX = bestX;
        }
    }

    public SolutionStruct getSolution() {
        return new SolutionStruct(this.bestK, this.bestX);
    }
}
class MainBigTable  {
    int L;
    ArrayList<Integer> integers;
    BackTrackingSolver solver;

    //**** many other member variables will be added by you ******/
    //**** many other member methods will be added by you  *******/
    //**** you are allowed to create a new class, but it must be added to this file  as the online judge accepts a single java file **/

    // reads each problem from input file and call method to solve and print output   
    public void process() throws Exception {
        Scanner scanner = new Scanner(System.in);

        ArrayList<BackTrackingSolver.SolutionStruct> solutions = new ArrayList<>();

        if (scanner.hasNextInt()) {
            int numTests=scanner.nextInt(); // reads the number of test cases
            for (int i=0; i< numTests; i++) {
                integers= new ArrayList<Integer>();
                int cNum=scanner.nextInt(); // this line contains the length of the ferry (L) in meters
                L=0;
                if (cNum !=0) { 
                    L=cNum*100; // convert L from meters to centimeters
                    while ((cNum=scanner.nextInt()) !=0) 
                        integers.add(cNum); // this reads the length of each car
                }

                // **** call a method do solve the current problem: 
                // at this point L contains the length of the ferry and integers contains the length of the cars
                // **** call a method to print solution of the current problem

                IMemoizedStorage memoImplmentation = null;

                memoImplmentation = new BigTable(integers.size(), L);
                
                this.solver = new BackTrackingSolver(L, integers, memoImplmentation);
                this.solver.backtrackSolve(0, L);

                // delay printing until all the processing is done
                solutions.add(this.solver.getSolution());
            }	    
        }

        for (int i=0; i<solutions.size(); i++) {
            BackTrackingSolver.SolutionStruct solution = solutions.get(i);
            System.out.println(""+solution.bestK);
            for (int j=0; j<solution.bestK; j++) {
                if (solution.bestX[j] == 0) System.out.println("starboard");
                if (solution.bestX[j] == 1) System.out.println("port");
            }

            if (i < solutions.size() - 1) {
                System.out.println(""); // add newline between solutions
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MainBigTable inputProcessor = new MainBigTable();
        inputProcessor.process();
    }

	public int getL() {
		return L;
	}

	public void setL(int l) {
		L = l;
	}

	public ArrayList<Integer> getIntegers() {
		return integers;
	}

	public void setIntegers(ArrayList<Integer> integers) {
		this.integers = integers;
	}

	public BackTrackingSolver getSolver() {
		return solver;
	}

	public void setSolver(BackTrackingSolver solver) {
		this.solver = solver;
	}

}