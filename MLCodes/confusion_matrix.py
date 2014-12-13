from collections import defaultdict
import matplotlib.pyplot as plt
import numpy as np

def print_CC(actual, predicted):
    '''
    Prints out the confusion matrix
    '''

    CC = defaultdict(lambda: defaultdict(float))
    for a, b in zip(actual, predicted):
        CC[a][b] += 1

    all_classes = list(set(actual + predicted))
    print '        |' + ' '.join('%8s' % c for c in all_classes)
    print '-' * 8 * (len(all_classes) + 2)
    for c1 in all_classes:
        values = [CC[c1][c2] for c2 in all_classes]
        total = sum(values) / 100
        row = ["%8.2f" % (v / total) for v in values]
        print "{:8}| {}".format(c1, ' '.join(row))


# Confusion matrix;
conf_arr = [
    [00.00,    00.00,     00.00,     100.00,     00.00],
    [00.00,    00.00,     00.00,     100.00,     00.00],
    [00.00,    00.00,     00.00,     100.00,     00.00],
    [00.00,    00.00,     00.00,     100.00,     00.00],
    [00.00,    00.00,     00.00,     100.00,     00.00]
]


norm_conf = []
for i in conf_arr:
    a = 0
    tmp_arr = []
    a = sum(i, 0)
    for j in i:
        tmp_arr.append(float(j)/float(a))
    norm_conf.append(tmp_arr)

fig = plt.figure()
plt.clf()
ax = fig.add_subplot(111)
ax.set_aspect(1)
ax.xaxis.tick_top()
res = ax.imshow(np.array(norm_conf), cmap=plt.cm.jet,
                interpolation='nearest')

width = len(conf_arr)
height = len(conf_arr[0])

for x in xrange(width):
    for y in xrange(height):
        ax.annotate("%.1f" % conf_arr[x][y], xy=(y, x),
                    horizontalalignment='center',
                    verticalalignment='center', color='#ffffff')

cb = fig.colorbar(res)
alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
plt.xticks(range(width), range(10))
plt.yticks(range(height), range(10))
plt.title('Predicted Label', y=1.05)
plt.ylabel('Actual Label', size=14)
# plt.show()
plt.savefig('confusion_matrix.png', format='png')












