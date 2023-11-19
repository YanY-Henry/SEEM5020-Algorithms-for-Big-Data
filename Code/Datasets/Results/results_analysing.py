import pandas as pd
import numpy as np

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