CS542 Design Patterns
Fall 2016
PROJECT 1 README FILE

PURPOSE:

	Assign courses to students based on their preferences, while
	maintaing overall minimum average preference score. 

PERCENT COMPLETE:

	I believe I have completed 100% of the project.

FILES:

	Included with this project are 2 files:

	Driver.java, main program responsible for reading the input file and 
	generating the output based on the courses assigned to each student.
	
	README, includes detailed instructions on how to run the program.

SAMPLE OUTPUT:

	S1 A B C 6
	S2 A C B 6
	S3 A C D 6
	S4 A B D 6
	S5 A D B 6
	S6 A D C 6
	S7 A B C 6
	S8 A B C 6
	S9 A D C 6
	S10 A D C 6
	S11 B C D 9
	S12 C B D 9
	Average preference_score is: 6.5

TO COMPILE:
	
	javac *.java

TO RUN:
	
	java Driver input.txt output.txt 


BIBLIOGRAPHY:

	* https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html

CHOICE OF DATA STRUCTURE AND COMPLEXITY:

	A two dimensional array is used as main data structure, which maintains the preferred courses
	for each student sorted based on their preference. This has a complexity of 4*4*n. First 4 is
	to find the minimum preference among 4 courses. Because we find minimum 4 times to have the
	preferences sorted the second 4 is necessary. N indicates number of students. Therefore, to 
	build the main data table has a complity of 4*4*N. The complexity of the main logic of the program
	has a complity of 3*n. Since each student is assigned 3 courses. The loop must iterate N times to find
	the student with the next minimum preferred course. Additionaly, optimizations are used to handle 
	worst cases when all students have same preferences for all the courses. This could keep some students
	from getting assigned 3 courses. Therefore, if the same course is countinusely being assigned multiple
	times. The algorithm must switch over to another preferred course. This continues in a round robin
	circular fashion.
