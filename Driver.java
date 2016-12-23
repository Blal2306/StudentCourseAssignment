import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Driver 
{
    //Names of all the students stored here
    static String[] names = new String[12];
    
    //Preferred courses for each student stored here
    static int[][] pref = new int[12][4];
    
    //Main Data structure
    static int[][] data = new int[12][7];
    
    //Courses
    static int[] courses = {10, 10, 10, 10};
    
    //Output table
    static int[][] output = new int[12][4];
    
    //input file location
    static String input_file = null;
    
    //output file location
    static String output_file = null;
    
    //++++ NEW CODE FOR BUG FIXES +++
    static int prev_course = -1;
    static int prev_count = 0;
    static int random_count = 0;
    //+++++++++++++++++++++++++++++++
    
    public static void main(String[] args) 
    {
        input_file = "file.txt";
        output_file = "out.txt";
        
        readFile();
        
        //print input
        printInput();
        
        //initialize data table
        buildData();
        
        //initialize the output table
        init_output();
        
        //++++ MAIN LOGIC OF THE PROGRAM ++++//
        
        //Continue until all the students been assigned 3 courses
        while(studentsLeft())
        {
            //find the student who's next preferred course is the minimum
            //among all the students
            int next = findNext();
            
            //what is the preferred course for that student
            int course = data[next][5];
            
            BREAK_OUT:
            //If the course is available and isn't being assigned continusly
            //for than six times and was not previously assigned
            if((courses[data[next][course]] > 2) && (prev_count < 5) && !prev_assign(next,course))
            {
                //Get the identifier for the course
                //0 == A
                //1 == B
                //2 == C
                //3 == D
                if(data[next][5]>3)
                    data[next][5] = 0;
                int cr = data[next][data[next][5]];
                
                //***BUG FIX: Prevents course from being assigned multiple times
                //so that course wouldn't result in negative numbers
                if(!prev_assign(next, cr) && courses[cr] > 2)
                {
                    if(cr == prev_course)
                    {
                        prev_count = prev_count +1;
                    }
                    else
                    {
                        prev_course = cr;
                        prev_count = 0;
                    }
                
                    //look up the preference for that course
                    int preference = pref[next][cr];
                
                    //update the preference for that course
                    data[next][6] = data[next][6] + preference;
                
                
                    //insert the course in the ouput table
                    output[next][output[next][0]] = cr;
                
                    //update the insertion slot in the output table
                    output[next][0]++;
                
                    //decrement the courses available
                    courses[cr]--;
                
                    //update the number of courses assigned
                    data[next][4]++;
                
                    //where to begin searching next
                    data[next][5]++;
                }
                else
                {
                    data[next][5]++;
                    if(data[next][5] > 3)
                    {
                        data[next][5] = 0;
                    }
                    break BREAK_OUT;
                }
            }
            
            //*** SPECIAL CASE ****
            //when 3 or less courses are left to be assigned, forget to
            //minimize the overall average preference score, but rather assign
            //based on need. This is handle the worst case when all students
            //have the same preference for all the courses
            else if(courses[0]+courses[1]+courses[2]+courses[3] <= 7)
            {
                //find the next student
                next = findNext();
                
                if(data[next][5] > 3)
                {
                    data[next][5] = 0;
                }
                
                //what is the course
                int cr = data[next][data[next][5]];
                
                //if the course is available and not perviously assigned
                if(courses[cr] > 0 && !prev_assign(next, cr)) 
                {
                    //what was preference number for that course
                    int preference = pref[next][cr];
                    
                    //update the preference for that course
                    data[next][6] = data[next][6] + preference;
                    
                    //insert the course in the ouput table
                    output[next][output[next][0]] = cr;
                    
                    //update the insertion slot in the output table
                    output[next][0]++;
                
                    //decrement the courses available
                    courses[cr]--;
                
                    //update the number of courses assigned
                    data[next][4]++;
                
                    //where to begin searching next
                    data[next][5]++;
                    
                    if(data[next][5] > 4)
                    {
                        data[next][5] = 0;
                    }
                }
                else
                {
                    data[next][5]++;
                    if(data[next][5] > 4)
                    {
                        data[next][5] = 0;
                    }
                }
            }
            else //course is not available, thus move to the next preferred
                 //course that is available, increments in a round-robin fashion
                 //to prevent starvation
            {
                //move to the next preference, start at zero if out of bound
                data[next][5] = random_count + 1;
                random_count++;
                if(random_count == 4)
                {
                    random_count =0;
                }
                if(data[next][5] >= 4)
                {
                    data[next][5] = 0;
                }
                
                //what is the next course
                int cr = data[next][data[next][5]];
                
                //if that course is available and not previously assigned
                if(courses[cr] > 1 && !prev_assign(next,cr))
                {
                    //Get the identifier for the course
                    //0 == A
                    //1 == B
                    //2 == C
                    //3 == D
                
                    //keep track of previously assigned courses
                    prev_course = cr;
                    prev_count = 0;
                
                    //look up the preference for that course
                    int preference = pref[next][cr];
                
                    //update the preference for that course
                    data[next][6] = data[next][6] + preference;
                
                    //insert the course in the ouput table
                    output[next][output[next][0]] = cr;
                
                    //update the insertion slot in the output table
                    output[next][0]++;
                
                    //decrement the courses available
                    courses[cr]--;
                
                    //update the number of courses assigned
                    data[next][4]++;
                
                    //where to begin searching next
                    data[next][5]++;
                }
                else
                {
                    data[next][5] = random_count + 1;
                    if(random_count == 4)
                    {
                        random_count = 0;
                    }
                }
            }
        }
        //++++ END MAIN LOGIC OF THE PROGRAM ++++//
        
        //print the main data table
        print_data_table();
        
        //print the output table
        print_output_table();
        
        //create the ouput file
        outputFile();
        
    }
    /**
     * This method is used to read the input file and load the data into
     * main data table
     * @author Blal
     */
    public static void readFile() 
    {
        String fileName = input_file;
        String line = null;
        String[] temp;
        int current_student = 0;
        try 
        {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null && current_student < 12) 
            {
                line = line.trim();
                temp = line.split(" ");
                //get the name of the student
                names[current_student] = temp[0];
                
                //insert preferred courses for that student
                pref[current_student][0] = Integer.parseInt(temp[1]);
                pref[current_student][1] = Integer.parseInt(temp[2]);
                pref[current_student][2] = Integer.parseInt(temp[3]);
                pref[current_student][3] = Integer.parseInt(temp[4]);
                current_student++;
            }   
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("Couldn't open the file ...");             
        }
        catch(IOException ex) 
        {
            System.out.println("Couldn't read the file ...");
        }
    }
    /**
     * This method prints raw data read for the input file
     * @author Blal
     */
    public static void printInput()
    {
        System.out.println("*** RAW INPUT TABLE ***\n");
        for(int i = 0; i < names.length; i++)
        {
            System.out.println(names[i]+"\t"+pref[i][0]+"\t"+pref[i][1]+
                    "\t"+pref[i][2]+"\t"+pref[i][3]);
        }
    }
    /**
     * This method builds the main data table. It sorts the preferred courses
     * for each student based on their preference and initialize other values
     * in the table
     * @author Blal
     */
    public static void buildData()
    {
        //initialize each row of the data
        for(int i = 0; i < 12; i++)
        {
            init(i);
        }
    }
    /**
     * This is helper method for <code>buildData()</code>. It initialize each
     * row of the main data table sorted based on the student's preference for
     * each course
     * @param x index of student for which the row will initialized.
     */
    public static void init(int x)
    {
        //Find the first minimum preferred course
        int index_1 = 0;
        int min_1 = 100;
        for(int i = 0; i < 4; i++)
        {
            if(pref[x][i] < min_1)
            {
                index_1 = i;
                min_1 = pref[x][i];
            }
        }

        //Find the second preferred course
        int index_2 = 0;
        int min_2 = 100;
        for(int i = 0; i < 4; i++)
        {
            if(pref[x][i] < min_2 && pref[x][i] > min_1)
            {
                index_2 = i;
                min_2 = pref[x][i];
            }
        }
        
        //Find the third preferred course
        int index_3 = 0;
        int min_3 = 100;
        for(int i = 0; i < 4; i++)
        {
            if(pref[x][i] < min_3 && pref[x][i] > min_2)
            {
                index_3 = i;
                min_3 = pref[x][i];
            }
        }
        
        //Find the fourth preferred course
        int index_4 = 0;
        int min_4 = 100;
        for(int i = 0; i < 4; i++)
        {
            if(pref[x][i] < min_4 && pref[x][i] > min_3)
            {
                index_4 = i;
                min_4 = pref[x][i];
            }
        }
        
        //PUT EVERTHING INTO THE DATA TABLE
        
        //put the minimumns
        data[x][0] = index_1;
        data[x][1] = index_2;
        data[x][2] = index_3;
        data[x][3] = index_4;
        
        //the the number of courses assigned
        data[x][4] = 0;
        
        //where to begin searching on the next iteration
        data[x][5] = 0;
        
        //total preference count
        data[x][6] = 0;
    }
    /**
     * This method prints the main data table.
     * @author Blal
     */
    public static void print_data_table()
    {
        System.out.println("\n*** PRINTING MAIN DATA TABLE ***\n");
        System.out.println("PREF 1"+"\t"+"PREF 2"+"\t"+"PREF 3"+"\t"+"PREF 4"
                        +"\t"+"CORSES"+"\t"+"NXT IDX"+"\t"+"TOTAL PREF");
        for(int i = 0; i < 12; i++)
        {
            for(int j = 0; j < 7; j++)
            {
                System.out.print(data[i][j]+"\t");
            }
            System.out.println();
        }
    }
    /**
     * This method checks whether all students have been assigned 3 courses
     * @return ture if all students have not been assigned 3 courses. Otherwise,
     * false is returned
     * @author Blal
     */
    public static boolean studentsLeft()
    {
        for(int i = 0; i < data.length; i++)
        {
            if(data[i][4] < 3)
                return true;
        }
        return false;
    }
    /**
     * This method will find the student who's next preferred course is minimum
     * among all the other students.
     * @return index identify the student
     * @author Blal
     */
    public static int findNext()
    {
        //current student with the minimum index
        int min_student = -1;
        
        //what is the minimum index
        int min_index = 100;
        
        for(int i = 0; i < 12; i++)
        {
            if(data[i][4] < 3 && data[i][5] < min_index)
            {
                min_index = data[i][5];
                min_student = i;
            }
        }
        return min_student;
    }
    /**
     * This method prints the output table.
     * @author Blal
     */
    public static void print_output_table()
    {
        //count the total number of A's B's C's and D's assigned
        int a_count = 0;
        int b_count = 0;
        int c_count = 0;
        int d_count = 0;
        
        System.out.println("\n*** OUTPUT TABLE ***\n");
        
        System.out.println("NXT SLT\tFRS CR\tSND CR\tTHR CR\tPREF SCR");
        for(int i = 0; i < 12; i++)
        {
            System.out.print(names[i]+"\t");
            for(int j = 1; j < 4; j++)
            {
                if(output[i][j] == 0)
                {
                    System.out.print("A"+"\t");
                    a_count++;
                }
                else if(output[i][j] == 1)
                {
                    System.out.print("B"+"\t");
                    b_count++;
                }
                else if(output[i][j] == 2)
                {
                    System.out.print("C" +"\t");
                    c_count++;
                }
                else if(output[i][j] == 3)
                {
                    System.out.print("D"+"\t");
                    d_count++;
                }
            }
            System.out.println(data[i][6]);
        }
        System.out.println("\nA COUNTER : "+a_count);
        System.out.println("B COUNTER : "+b_count);
        System.out.println("C COUNTER : "+c_count);
        System.out.println("D COUNTER : "+d_count);
        
        System.out.println("\nCOURSES LEFT : ");
        System.out.println("A\tB\tC\tD");
        for(int i = 0; i < 4; i++)
        {
            System.out.print(courses[i]+"\t");
        }
        
        //calculate the average
        double avg = 0;
        for(int i = 0; i < 12; i++)
        {
            avg = avg + data[i][6];
        }
        
        System.out.println("\n\nAVERAGE PRF SCORE : "+avg/(double)12);
    }
    /**
     * This method initializes the output table.
     */
    public static void init_output()
    {
        for(int i = 0; i < 12; i++)
        {
            output[i][0] = 1;
            output[i][1] = -1;
            output[i][2] = -1;
            output[i][3] = -1;
        }
    }
    /**
     * This method is used to create the output file.
     * @author Blal
     */
    public static void outputFile()
    {
        PrintWriter writer = null;
        try
        {
            writer = new PrintWriter(output_file, "UTF-8");
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File could't be created ...");
            System.exit(0);
        }
        catch(UnsupportedEncodingException e)
        {
            System.out.println("Unsupported Encoding ...");
            System.exit(0);
        }

        //begin writing the data to the output file
        for(int i = 0; i < 12; i++)
        {
            writer.print(names[i]+" ");
            for(int j = 1; j < 4; j++)
            {
                if(output[i][j] == 0)
                {
                    writer.print("A"+" ");
                }
                else if(output[i][j] == 1)
                {
                    writer.print("B"+" ");
                }
                else if(output[i][j] == 2)
                {
                    writer.print("C" +" ");
                }
                else if(output[i][j] == 3)
                {
                    writer.print("D"+" ");
                }
            }
            writer.println(data[i][6]);
        }
        
        //print the average to the file
        //calculate the average
        double avg = 0;
        for(int i = 0; i < 12; i++)
        {
            avg = avg + data[i][6];
        }
        avg = avg /12;
        writer.println("Average preference_score is: "+avg);
        writer.close();
    }
    /**
     * This method check if the course was previously assigned to the student.
     * This prevents multiple assignment of the same course
     * @param student The current student
     * @param cr The course we want to check for duplicate
     * @return true if the course was previously assigned, otherwise false
     */
    public static boolean prev_assign(int student, int cr)
    {
        for(int i = 1; i < 4; i++)
        {
            if(output[student][i] == cr)
            {
                return true;
            }
        }
        return false;
    }
    
}
