import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

schedulers = ["ProterScheduler", "GreedyFCFSScheduler", "StrictFCFSScheduler", "GreedyPriorityScheduler", "StrictPriorityScheduler", "LookaheadScheduler"]
filename_endings = ["-simulations.csv", "-resources.csv", "-tasks.csv"]

data = [None, None, None]

for i, s in enumerate(schedulers):
    for j, f in enumerate(filename_endings):
        df = pd.read_csv('output/'+s+f)
        if (data[j] is None): data[j] = np.zeros((6,df.shape[0],df.shape[1]),dtype=object)
        data[j][i] = df.to_numpy()


makespans = data[0][:,:,2]
# print(makespans.shape)
# for i in range(makespans.shape[0]): plt.plot(np.arange(0,1000), makespans[1,:] - makespans[i,:],label=schedulers[i])
# plt.legend()
# plt.show()

# avgOffsetMakespan = np.average(data[0][:,:,2]-data[0][1,:,2],axis=1)
# print(avgOffsetMakespan.shape)
# plt.bar(schedulers, avgOffsetMakespan)
# plt.show()
