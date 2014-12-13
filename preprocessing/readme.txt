Segmentor.java file is the main and only class that perform the pre-processing file. To execute it:

1) Open the Segmentor.java file, go to the main method
2) Change the address of input file. They are two input file:
	2.1) Points (GPS traces) file whose path is stored in variable coorPath
	2.2) User information file whose path is stored in variable userInfoPath
3) Execute the file

They are 4 output files:

1) firstStep-segmented.csv contains the results of the executing first step in segmentation of coordinate file.
2) secondStep-segmented.csv contains the results of the executing first step in segmentation of firstStep-segmented.csv file.
3) merged-segmented.csv is the final result and contains the results of merging individual travels in secondStep-segmented.csv.
4) transport-method.txt contains the method of transport for each individual travel in merged-segmented.csv.
	