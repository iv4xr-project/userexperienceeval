import matplotlib.pyplot as plt
import numpy as np



###### ADD THE FILE NAME OF THE TEST YOU WANT TO VISUALIZE ########

file_name = "Example_Name"


###### CHANGE THE PATH TO MATCH THE LOCATION OF THE REPOSITORY #########

f = open("EXAMPLE_PAT/iv4xrDemo/src/test/resources/emotions/" + file_name + ".txt", "r")







emotions = f.readline()

#Remove extras
emotions = emotions.replace("[", "")
emotions = emotions.replace("E|", "")
emotions = emotions.replace("]]", "")
emotions = emotions.replace("\n", "")

emo_triplets = emotions.split("], ")

emo_final = []

for triplet in emo_triplets:
	emo_final.append(triplet.split(","))

for i in range(len(emo_final)):
	for j in range(len(emo_final[0])):
		emo_final[i][j] = float(emo_final[i][j])



time_stamp = f.readline()



time_stamp = time_stamp.replace("T|", "")
time_stamp = time_stamp.replace("\n", "")
time_stamp = time_stamp.replace("[", "")
time_stamp = time_stamp.replace("]", "")


time_snippets = time_stamp.split(", ")



for i in range(len(time_snippets)):
	time_snippets[i] = time_snippets[i].split("T")[1]



for i in range(len(time_snippets)):

	time_snippets[i] = time_snippets[i].split(".")
	prov = time_snippets[i][1]
	time_snippets[i] = time_snippets[i][0].split(":")
	time_snippets[i].append(prov)




position_stamp = f.readline()




position_stamp = position_stamp.replace("[", "")
position_stamp = position_stamp.replace("P|", "")
position_stamp = position_stamp.replace(">]", "")
position_stamp = position_stamp.replace("]", "")
position_stamp = position_stamp.replace("<", "")
position_stamp = position_stamp.replace("\n", "")

position_triplets = position_stamp.split(">, ")

position_final = []

for triplet in position_triplets:
	position_final.append(triplet.split(","))

for i in range(len(position_final)):
	for j in range(len(position_final[0])):
		position_final[i][j] = float(position_final[i][j])


print(position_final)

fig = plt.figure()
ax = fig.add_subplot(111)



for i in range(len(emo_final)):

	ax.scatter(emo_final[i][0], emo_final[i][2], color= (0, float(i/len(emo_final)), float(i/len(emo_final)) ))


plt.xlim(-6, 6)
plt.ylim(-6, 6)
ax.axhline(y=0, color='k')
ax.axvline(x=0, color='k')
plt.title('Core Affect')
plt.xlabel('Pleasure')
plt.ylabel('Arousal')
plt.show()



fig = plt.figure()
ax = fig.add_subplot(111)



for i in range(len(emo_final)):

	ax.scatter(position_final[i][0], position_final[i][2], color= (1 - ((emo_final[i][0] + 5)/10), (emo_final[i][2] + 5)/10 , 0.0 ) )


plt.title('Core Affect and Position')
plt.xlabel('X')
plt.ylabel('Z')
plt.axis('scaled')
plt.show()







