import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

schedulers = ["Proter", "GreedyFCFS", "StrictFCFS", "GreedyPriority", "StrictPriority", "Lookahead"]
filename_endings = ["-simulations.csv", "-resources.csv", "-tasks.csv"]

data = [None, None, None]

for i, s in enumerate(schedulers):
    for j, f in enumerate(filename_endings):
        df = pd.read_csv('output/1/'+s+"Scheduler"+f)
        if (data[j] is None): data[j] = np.zeros((6,df.shape[0],df.shape[1]),dtype=object)
        data[j][i] = df.to_numpy()


makespans = data[0][:,:,2]
# print(makespans.shape)
# for i in range(makespans.shape[0]): plt.plot(np.arange(0,1000), makespans[1,:] - makespans[i,:],label=schedulers[i])
# plt.legend()
# plt.show()

makespanDif = np.array(data[0][:,:,2]-data[0][1,:,2], dtype=float)
avgDif = np.average(makespanDif,axis=1)
stdDif = np.std(makespanDif,axis=1)

# plt.bar(schedulers, avgDif, yerr=stdDif, align='center', ecolor='black', capsize=10)
# plt.grid(True,axis='y')
# plt.xticks(rotation=45)
# plt.show()

filteredDif = makespanDif[:, np.any(makespanDif!=0,axis=0)]

avgFil = np.average(filteredDif,axis=1)
stdFil = np.std(filteredDif,axis=1)

# plt.clf()
width = 0.40
fLabel = "Filtered, "+str(filteredDif.shape[1]/makespanDif.shape[1]*100)+"% retained"
plt.title("Average Difference in Makespan Compared Against GreedyFCFS")
plt.bar(np.arange(6)-0.2, avgDif, width, color="blue", yerr=stdDif, align='center', ecolor='darkblue', capsize=10, label="Unfiltered")
plt.bar(np.arange(6)+0.2, avgFil, width, color="orange", yerr=stdFil, align='center', ecolor='darkorange', capsize=10, label=fLabel)
plt.grid(True,axis='y')
plt.xticks(np.arange(6), schedulers, rotation=10)
plt.legend()
plt.show()
