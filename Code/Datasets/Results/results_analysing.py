import pandas as pd
import numpy as np
import re
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

for distri in ["uniform", "gaussian", "exponential"]:
	df_exp = pd.read_csv(f'../{distri}_count.csv', header=None, names=['id', 'count'])
	for algo in ["Misra-Gries", "Space-Saving", "Count-Min", "Count-Sketch", "ASketch"]:
		df_asketch = pd.read_csv(f'Single-based/{algo}_{distri}_count.csv', header=None, names=['id', 'count'])
		errors = df_asketch['count'] - df_exp['count'] 
		mean_error = errors.abs().mean()
		std_error = np.sqrt((errors**2).mean())
		range_error = errors.abs().max()
		print(f"{distri} {algo}")
		print("Average error: ", mean_error)
		print("Standard deviation: ", std_error)
		print("Range: ", range_error)
		print()

for distri in ["uniform", "gaussian", "exponential"]:
	print()
	df_ds = pd.read_csv(f'../{distri}_RBcount.csv', header=None, names=['range', 'count'])
	for algo in ["Misra-Gries", "Space-Saving", "Count-Min", "Count-Sketch", "ASketch"]:
		df_sketch = pd.read_csv(f'Range-based/{algo}_{distri}_RBsketch.csv', header=None, names=['range', 'count'])

		ds_counts = df_ds['count'].values
		algo_counts = df_sketch['count'].values
		differences = algo_counts - ds_counts
		std_dev = np.std(differences)
		print(f'{distri} {algo} SD: {std_dev}')

algo_colors = {"Misra-Gries": "red", "Space-Saving": "green", "Count-Min": "blue", "Count-Sketch": "purple", "ASketch": "orange"}
distributions = ["uniform", "gaussian", "exponential"]

for distribution in distributions:
    real_address = f'../{distribution}_count.csv'
    real_count = pd.read_csv(real_address, header=None)

    plt.figure(figsize=(10, 6))

    for algo in algo_order:
        approximation_address = f'Single-based/{algo}_{distribution}_count.csv'
        approx_count = pd.read_csv(approximation_address, header=None)

        plt.scatter(real_count[1], approx_count[1], color=algo_colors[algo], alpha=0.6, label=algo, s=5)

    plt.plot([real_count[1].min(), real_count[1].max()], [real_count[1].min(), real_count[1].max()], 'black', label='y=x line')

    plt.title(f'Real and Estimated Frequencies ({distribution} distribution)')
    plt.xlabel('Real frequency')
    plt.ylabel('Estimated frequency')
    plt.legend()

    plt.savefig(f'{distribution}_plot.png')