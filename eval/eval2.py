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


# ===


taskScores = np.array((data[2][:,:,3]+2) * data[2][:,:,6], dtype=int)

ids = pd.factorize(data[2][0,:,2])[0]

simScores = np.apply_along_axis(lambda x: np.bincount(ids,weights=x), axis=1, arr=taskScores)

simScores = simScores[:,:] - simScores[1,:]

avgDif = np.average(simScores,axis=1)
stdDif = np.std(simScores,axis=1)


filteredDif = simScores[:, np.any(simScores!=0,axis=0)]

avgFil = np.average(filteredDif,axis=1)
stdFil = np.std(filteredDif,axis=1)

print(avgDif)
# # plt.clf()
width = 0.40
fLabel = "Filtered, "+str(filteredDif.shape[1]/simScores.shape[1]*100)+"% retained"
plt.title("Average Difference in Makespan Compared Against GreedyFCFS")
plt.bar(np.arange(6)-0.2, avgDif, width, color="blue", yerr=stdDif, align='center', ecolor='darkblue', capsize=10, label="Unfiltered")
plt.bar(np.arange(6)+0.2, avgFil, width, color="orange", yerr=stdFil, align='center', ecolor='darkorange', capsize=10, label=fLabel)
plt.grid(True,axis='y')
plt.xticks(np.arange(6), schedulers, rotation=10)
plt.legend()
plt.show()
