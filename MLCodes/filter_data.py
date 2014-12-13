# File: filter_data.py
# Creator: Leo
#

import csv

dataFile = open('merged-segmented100pct-with-STDev.csv', 'r')
header = dataFile.readline().rstrip().split(',')
# print header
# print ''
header[0] = header[0]
header[1] = header[1][1:len(header[1])]
header[2] = header[2][2:len(header[2]) - 1]
header[3] = header[3][1:len(header[3])]
header[4] = header[4][1:len(header[4])]
header[5] = header[5][1:len(header[5])]
header[6] = header[6][1:len(header[6])]
header[7] = header[7][2:len(header[7]) - 1]
header[8] = header[8]
header[9] = header[9][2:len(header[9]) - 1]
header[10] = header[10][1:len(header[10])]
header[11] = header[11]
header[12] = header[12]
header[13] = header[13]
header[14] = header[14]
header[15] = header[15]
header[16] = header[16]
header[17] = header[17]
header[18] = header[18]
header[19] = header[19][1:len(header[19])]
header[20] = header[20]
# print header
# print ''

data = []
indexOfMode = 20
# modes = []

for line in dataFile:
	strArr = line.rstrip().split(',')
	data.append(strArr)
	# if (strArr[indexOfMode] not in modes):
	#	modes.append(strArr[indexOfMode])

dataFile.close()
# print modes

with open('less_filtered_Nov_15-21_with_std.csv', 'wb') as outputFile:
	writer = csv.writer(outputFile)
	writer.writerow(header)
	for i in range(0, len(data)):
		if ((float(data[i][1]) != 0) and
			(float(data[i][2]) != 0) and
			(float(data[i][3]) != 0) and
			(float(data[i][4]) != 0) and
			(float(data[i][5]) != 0) and
			(float(data[i][6]) != 0) and
			(float(data[i][7]) >= 0) and
			(float(data[i][8]) > 0) and
			(float(data[i][9]) > 0) and
			(float(data[i][10]) > 0) and
			(data[i][indexOfMode] != 'N/A')):
			if (data[i][indexOfMode] == 'InterCampus Shuttle'):
				data[i][indexOfMode] = 'Public Transit'
			if (data[i][indexOfMode] == 'Car (as driver)'):
				data[i][indexOfMode] = 'Car'
			if (data[i][indexOfMode] == 'Car (as passanger)'):
				data[i][indexOfMode] = 'Car'
			if (data[i][indexOfMode] == 'Car   public transit'):
				data[i][indexOfMode] = 'Car and Public Transit'
			writer.writerow(data[i])

outputFile.close()






