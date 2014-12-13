# File: merge_data.py
# Creator: Leo

import csv

dataFile_1 = open('less_filtered_Nov_10-14_with_std.csv', 'r')
header = dataFile_1.readline().rstrip().split(',')
modes = []

data_10_14 = []
for line in dataFile_1:
	strArr = line.rstrip().split(',')
	data_10_14.append(strArr)
	if (strArr[20] not in modes):
		modes.append(strArr[20])
dataFile_1.close()

dataFile_2 = open('less_filtered_Nov_15-21_with_std.csv', 'r')
dataFile_2.readline()

data_15_21 = []
for line in dataFile_2:
	strArr = line.rstrip().split(',')
	data_15_21.append(strArr)
	if (strArr[20] not in modes):
		modes.append(strArr[20])
dataFile_2.close()

with open('less_filtered_Nov_10-21_with_std.csv', 'wb') as outputFile:
	writer = csv.writer(outputFile)
	writer.writerow(header)
	for i in range(0, len(data_10_14)):
		writer.writerow(data_10_14[i])
	for j in range(0, len(data_15_21)):
		writer.writerow(data_15_21[j])
outputFile.close()

print modes

