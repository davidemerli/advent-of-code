import os

def get_input():
	lines = open(os.getcwd() + "/input/day1.txt").readlines()
	return [int(s) for s in lines]

def first_star():
	sum = 0
	for i in get_input():
		sum += int(i / 3) - 2;
	return str(sum)

if __name__ == "__main__":
	print("result1: " + first_star())