 
### Task:

##### The task is to identify cheating students in the class
  
Students were taking an exam which was the a multichoice test. We know the location in class where each student was sitting and their answers.
Student's sitting location is in format x.y, where:
- x = the row
- = the sitting place in the row/column. 
  	
Rows are numbered from front to back and left to right

Each student had a chance to cheat and write answers down from:

- his neighbours in the same row,
- the 3 other students sitting in front
 
Class looks like:

      (.....back........)
      (x,x,x,x,x,x,x,x,x)
      (x,x,x,x,x,y,s,y,x)
      (x,x,x,x,x,y,y,y,x)
      (....front........) 
      
- s = the student
- y = the neighbours
 
If you find that you are missing requirements, take your own judgment.
 
Data can be found in results.csv file, but CSV parsing is already done and results are mapped to a Student objects list.  

### Solution:

We need to find each student's neighbours. Then we need to find identical answers between the student and each of his neighbour. If there are are five or more identical answers, student and his neighbour particiapated in the act cheating - then they are added to a potential map of cheaters. Then we need to apply probability of 50% - i.e. only half of potentials cheaters actually did cheat and others were just lucky to have the identical answers.
  
### Tests:
  
- StudenUtilsTest: 
  - applyProbability() check if map that has 4 entires is successfully reduced by 50%
  - generateRandomNumbers() check if IllegalArgumentException is thrown when trying to generate 10 unique random numbers in the range [1-9]
  - round() check if doubles are being rounded as expected - leaving one decimal place.

