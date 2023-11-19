import numpy as np
import csv
import matplotlib.pyplot as plt
from scipy.stats import expon
from tqdm import tqdm
import pandas as pd

# Data size: 10,000,000
size = 10000000

# Data range: 0～100,000
range_start = 0
range_end = 100000

# Gaussian distribution parameters
mu = 50000
sigma = 12500

# Exponential distribution parameters
beta = 20000

# Number of data generated at one time
chunk_size = 100000

set_num = 1
def generate_and_save_data(dist_name, data_generator):
    global set_num
    print(f"Generating {dist_name} data...")
    
    count_array = np.zeros(range_end+1, dtype=int)

    with open(f'{dist_name}_data.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        pbar = tqdm(total=size)
        generated = 0
        while generated < size:
            new_data = data_generator(min(chunk_size, size - generated))
            for value in new_data:
                writer.writerow([value])
                count_array[value] += 1
            generated += len(new_data)
            pbar.update(len(new_data))
    pbar.close()
    print(f"{dist_name} data generation completed.")

    # Save count result to csv
    with open(f'{dist_name}_count.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        for i, count in enumerate(count_array):
            writer.writerow([i, count])

    # Plot
    print(f'Preparing for the counting plot of the {dist_name} distribution...')
    plt.bar(range(len(count_array)), count_array)
    plt.xlabel('Value')
    plt.ylabel('Count')
    plt.title(f'Dataset {set_num}: {dist_name} distribution')
    plt.savefig(f'{dist_name}_count.png')
    plt.close()
    set_num += 1
    print(f'The counting plot of the {dist_name} distribution is ready.')
    print()

# Uniform distribution generating
generate_and_save_data("uniform", lambda n: np.random.randint(range_start, range_end, n))

# Gaussian distribution generating
def generate_gaussian(n):
    valid_data = np.array([], dtype=int)
    while len(valid_data) < n:
        data = np.random.normal(mu, sigma, n)
        data = np.round(data).astype(int)
        valid_data = np.concatenate((valid_data, data[(data >= range_start) & (data <= range_end)]))
    return valid_data[:n]
generate_and_save_data("gaussian", generate_gaussian)

# Exponential distribution generating
def generate_exponential(n):
    valid_data = np.array([], dtype=int)
    while len(valid_data) < n:
        data = expon.rvs(scale=beta, size=n)
        data = np.round(data).astype(int)
        valid_data = np.concatenate((valid_data, data[(data >= range_start) & (data <= range_end)]))
    return valid_data[:n]
generate_and_save_data("exponential", generate_exponential)

ranges = [[0,1000], [24500, 25500], [49500, 50500], [74500, 75500], [99000, 100000]]

for distri in ["uniform", "gaussian", "exponential"]:
    df_exp = pd.read_csv(f'{distri}_count.csv', header=None, names=['id', 'count'])
    df_counts = pd.DataFrame(columns=['range', 'count'])
    for r in ranges:
        mask = (df_exp['id'] >= r[0]) & (df_exp['id'] < r[1])
        total_count = df_exp[mask]['count'].sum()
        range_str = f"[{r[0]} {r[1]}]"  # create a formatted string for range with hyphen instead of comma
        df_counts = pd.concat([df_counts, pd.DataFrame({'range': [range_str], 'count': [total_count]})], ignore_index=True)

    df_counts.to_csv(f'{distri}_RBcount.csv', index=False, header=False)