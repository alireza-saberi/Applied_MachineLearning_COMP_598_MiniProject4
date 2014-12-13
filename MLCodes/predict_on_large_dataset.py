# File: predict_on_large_dataset.py
# Creator: Leo
#

from sklearn import svm
from sklearn import tree
from sklearn import ensemble
from sklearn import linear_model
from sklearn import cross_validation
from sklearn import preprocessing
import numpy as np

from sklearn.metrics import confusion_matrix
import matplotlib.pyplot as plt

dataFile = open('filtered_Nov_10-21_with_std.csv', 'r')
dataFile.readline()

dataX = []
dataY = []

for line in dataFile:
	strArr = line.rstrip().split(',')
	
	entry = []
	for i in range(1, 18 + 1):
		entry.append(float(strArr[i]))
	dataX.append(entry)

	target = 0
	if (strArr[20] == 'Walk'):
		target = 0
	if (strArr[20] == 'Bicycle'):
		target = 1
	if (strArr[20] == 'Car'):
		target = 2
	if (strArr[20] == 'Public Transit'):
		target = 3
	if (strArr[20] == 'Car and Public Transit'):
		target = 4
	dataY.append(target)

dataFile.close()

idxOfStartLat = 0
idxOfEndLat = 1
idxOfStartLong = 2
idxOfEndLong = 3
idxOfStartAlt = 4
idxOfEndAlt = 5
idxOfMinSpeed = 6
idxOfMaxSpeed = 7
idxOfAvgSpeed = 8
idxOfStdSpeed = 9
idxOfNO = 10
idxOfNO2 = 11
idxOfPM10 = 12
idxOfO3 = 13
idxOfSO3 = 14
idxOfMaxTemp = 15
idxOfMinTemp = 16
idxOfWindSpeed = 17

featureSelected = [idxOfStartLat,
				   idxOfEndLat,
				   idxOfStartLong,
				   idxOfEndLong,
				   idxOfStartAlt,
				   idxOfEndAlt,
				   idxOfMinSpeed,
				   idxOfMaxSpeed,
				   idxOfAvgSpeed,
				   idxOfStdSpeed,
				   idxOfNO,
				   idxOfNO2,
				   idxOfPM10,
				   idxOfO3,
				   idxOfSO3,
				   idxOfMaxTemp,
				   idxOfMinTemp,
				   idxOfWindSpeed]

X = np.array(dataX)
Y = np.array(dataY)

print 'X shape:', X.shape
print 'Y shape:', Y.shape
print ''

trainX, testX, trainY, testY = cross_validation.train_test_split(X[:,featureSelected], Y, test_size = 0.20, random_state = 0)

print 'trainX shape:', trainX.shape, '; trainY shape:', trainY.shape
print 'testX shape:', testX.shape, '; testY shape:', testY.shape
print ''

# Logistic Regression;
Cs = [0.01, 0.1, 0.2, 0.5, 0.8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

for i in range(0, len(Cs)):
	lrClf = linear_model.LogisticRegression(penalty = 'l2', C = Cs[i])
	lrClf.fit(trainX, trainY)
	print 'C:', Cs[i]
	print 'Logistic Regression:', lrClf.score(testX, testY)
	"""
	# Produce Confusion Matrix;
	y_pred = lrClf.predict(testX)
	# Compute confusion matrix
	cm = confusion_matrix(testY, y_pred)
	np.set_printoptions(precision=4)
	cm_normalized = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]

	print(cm)
	print(cm_normalized)

	# Show confusion matrix in a separate window
	plt.matshow(cm_normalized)
	# plt.title('Confusion matrix')
	plt.colorbar()
	plt.ylabel('True label')
	plt.xlabel('Predicted label')
	plt.show()
	print ''
	"""

# Random Forest;
numOfEstimators = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]

for i in range(0, len(numOfEstimators)):
	rfClf = ensemble.RandomForestClassifier(n_estimators = numOfEstimators[i])
	rfClf.fit(trainX, trainY)
	print '# of Estimators:', numOfEstimators[i]
	print 'Random Forest:', rfClf.score(testX, testY)
	"""
	# Produce Confusion Matrix;
	y_pred = rfClf.predict(testX)
	# Compute confusion matrix
	cm = confusion_matrix(testY, y_pred)
	np.set_printoptions(precision=4)
	cm_normalized = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]

	print(cm)
	print(cm_normalized)

	# Show confusion matrix in a separate window
	plt.matshow(cm_normalized)
	# plt.title('Confusion matrix')
	plt.colorbar()
	plt.ylabel('True label')
	plt.xlabel('Predicted label')
	plt.show()
	print ''
	"""

# SVM;
kernels = ['linear', 'poly', 'rbf', 'sigmoid']

for i in range(0, len(kernels)):
	Cs = [0.01, 0.1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	
	for j in range(0, len(Cs)):
		gamma = [0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1]
		
		for k in range(0, len(gamma)):
			print 'C:', Cs[j]
			print 'gamma:', gamma[0]
			svmClf = svm.SVC(kernel = kernels[i])
			svmClf.fit(preprocessing.scale(trainX), trainY)
			print kernels[i] + ' kernel SVM:', svmClf.score(preprocessing.scale(testX), testY)
			"""
			# Produce Confusion Matrix;
			y_pred = svmClf.predict(testX)
			# Compute confusion matrix
			cm = confusion_matrix(testY, y_pred)
			np.set_printoptions(precision=4)
			cm_normalized = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]

			print(cm)
			print(cm_normalized)

			# Show confusion matrix in a separate window
			plt.matshow(cm_normalized)
			# plt.title('Confusion matrix')
			plt.colorbar()
			plt.ylabel('True label')
			plt.xlabel('Predicted label')
			plt.show()
			print ''
			print ''
			"""










