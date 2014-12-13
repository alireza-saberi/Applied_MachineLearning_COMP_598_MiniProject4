"filter_data.py": Filter segmented trips from Nov. 10 - 14 and Nov. 15 - 21, eliminate noise data points
due to wrong GPS recordings etc.

"merge_data.py": Merge data from Nov. 10 - 14 and Nov. 15 - 21 together.

"predict_on_large_dataset.py": Predict travel mode from segmented GPS trips, augmented with pollutant and
weather info; cross validate parameters for Logistic Regression, Random Forrest & SVM.

"confusion_matrix.py": Produce normalized confusion matrix. 