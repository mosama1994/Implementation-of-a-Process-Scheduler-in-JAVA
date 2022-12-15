import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ProcessScheduling {
	public static void main(String[] args) {
		
		// Data Structure D string priority Queue with String Comparator to store string objects in order of their arrival time, from
		// lowest to highest
		PriorityQueue<String> queue1 = new PriorityQueue<>(new StringComparator1());
		// Data Structure Q string priority Queue with String Comparator to store string objects in order of their priority, from
		// lowest to highest
		PriorityQueue<String> systemqueue = new PriorityQueue<>(new StringComparator2());

		// Initializing variables to store current time, running status, max wait time, total no of processes, and a string word
		// into which each line of the input file is stored
		int currenttime = 0;
		boolean running = false;
		int max_waittime = 30;
		String word;
		int no_of_process = 0;

		// Reading the file using Scanner class object and storing each string read into the priority queue D named "queue1" in
		// this project
		try {
			Scanner scan = new Scanner(new File("process_scheduling_input.txt"));

			while (scan.hasNextLine() == true) {
				no_of_process += 1;
				word = scan.nextLine();
				queue1.add(word);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found at Location");
			e.printStackTrace();
		}
		
		
		// Creating an iterator object of the priority queue to read the queue and print the processes
		Iterator<String> iter = queue1.iterator();
		while(iter.hasNext()) {
			String wrd = iter.next();
			String[] words = wrd.split(" ");
			System.out.println("Id = " + words[0] + ", priority = " + words[1] + ", duration = " + words[2] + ", arrival time = " + words[3]);
		}
		System.out.println("\nMaximum wait time = " + max_waittime);
		
		// From here onwards, the processes are run, some variables initialized
		// duration is used to store the time at which a process which has started will end. total_waittime is used to store
		// the total wait time for the complete run of all processes. empty is a flag used to check when priority queue D is
		// empty. current _process is used to store the id of the current running process.
		int duration = -1;
		double total_waittime = 0;
		boolean empty = false;
		int current_process = 0;
		
		// This is the main loop to run all the process. This will keep running until both the priority queue D & Q are empty
		// or the running status of processes is true.
		while (queue1.size() != 0 || systemqueue.size() != 0 || running == true) {
			
			// In this part the priority queue D is checked if empty, and if not then the top process in the queue is peeked
			// to check if the arrival time is <= current time. If the condition is met then the process is removed from D and
			// inserted into Q. While loop is used because there could be multiple processes with the same arrival time.
			boolean check_time = true;
			while(check_time == true && queue1.size() != 0) {
				String a = queue1.peek();
				String[] a_1 = a.split(" ");
				int arrivaltime = Integer.parseInt(a_1[3]); 
				if (arrivaltime <= currenttime) {
					String b = queue1.poll();
					systemqueue.add(b);
				}
				else check_time = false;
			}
			
			// In this part the priority queue D is checked if empty and if empty then the message is printed that D is empty.
			if (queue1.size() == 0 && empty == false) {
				System.out.println("\nD becomes empty at time " + currenttime + "\n");
				empty = true;
			}
			
			// In this part a function defined below is being called. If the queue Q is not empty and running status is false
			// meaning there is no process currently running then a process is removed from the Q. The string value returned
			// from the function is used to update the duration, total_waittime and current_process variables in the main function 
			if (systemqueue.size() != 0 && running == false) {
				String c = sysqueue(systemqueue, running, currenttime, total_waittime);
				running = true;
				String[] c_1 = c.split(" ");
				duration = Integer.parseInt(c_1[0]);
				total_waittime = Double.parseDouble(c_1[1]);
				current_process = Integer.parseInt(c_1[2]);
			}
			
			// In this part, if the duration(end time of process) is equal to current time, then priority of processes need to be updated.
			if (duration == currenttime) {
				System.out.println("Process " + current_process + " finished at time " + duration);
				// Two Array list created. remove is used to store the process of which the priority needs to be changed. added
				// is used to store the same process with the new updated priority.
				ArrayList<String> remove = new ArrayList<>();
				ArrayList<String> added = new ArrayList<>();
				// Running status set to false as the process has finished.
				running = false;
				System.out.println("\nUpdate priority:");
				// If Q is not empty then the priorities will be updated.
				if (systemqueue.size() != 0) {
					// Iterator object made on Q to get each process and check if wait time is greater than the max wait time
					// of each process.
					Iterator<String> iter_1 = systemqueue.iterator();
					while(iter_1.hasNext()) {
						String wrd_1 = iter_1.next();
						String[] words_1 = wrd_1.split(" ");
						int arrival_time_1 = Integer.parseInt(words_1[3]);
						int wait_time_1 = currenttime - arrival_time_1;
						int priority = Integer.parseInt(words_1[1]);
						// Here it is being checked if the process has wait time greater than the max wait time or if the
						// priority is zero or not. If any of the conditions are met then the body of the if statement will
						// not run.
						if (wait_time_1 > max_waittime && (priority - 1) != 0) {
							int new_priority = priority - 1;
							String new_process = words_1[0] + " " + new_priority + " " + words_1[2] + " " + words_1[3];
							remove.add(wrd_1);
							added.add(new_process);
							System.out.println("PID = " + words_1[0] + ", wait time = " + wait_time_1 + 
									", current priority = " + words_1[1]);
							System.out.println("PID = " + words_1[0] + ", new priority = " + new_priority);
						}
					}
				}
				// Once the priorities have been updated, the arrays remove and added are used to update the priority queue Q.
				// First remove array list is used to remove the processes from Q whose priority have been updated. Then the
				// added array list is used to add the processes back with the update priorities. 
				Iterator<String> iter_2 = remove.iterator();
				Iterator<String> iter_3 = added.iterator();
				while (iter_2.hasNext()) {
					systemqueue.remove(iter_2.next());
				}
				while (iter_3.hasNext()) {
					systemqueue.add(iter_3.next());
				}
				// As the running status is false again, the priority Q is check if empty or not and if not empty then the top
				// process is removed and run.
				if (systemqueue.size() != 0 && running == false) {
					String c = sysqueue(systemqueue, running, currenttime, total_waittime);
					running = true;
					String[] c_1 = c.split(" ");
					duration = Integer.parseInt(c_1[0]);
					total_waittime = Double.parseDouble(c_1[1]);
					current_process = Integer.parseInt(c_1[2]);
				}
			}
			// Current time incremented with each iteration of the while loop.
			currenttime++;
		}
		System.out.println("\nTotal wait time = " + total_waittime);
		System.out.println("Average wait time = " + total_waittime / no_of_process);
	}
	
	// This is the function being called to remove the process from the priority queue Q to be run. The inputs to this function
	// are the priority queue Q, the running status, the current time, and the total wait time. The total wait time of the
	// process to be removed is calculated and the details of the removed process are printed. This method returns a string
	// containing the duration (end time of process), total wait time and process id of the current process running. These
	// values are then set in main.
	public static String sysqueue (PriorityQueue<String> systemqueue, boolean running, int ct, double total_waittime) {
		
		String b_1 = systemqueue.poll();
		String[] b_2 = b_1.split(" ");
		int arrival_time = Integer.parseInt(b_2[3]);
		int wait_time = ct - arrival_time;
		total_waittime += wait_time;
		int duration_time = Integer.parseInt(b_2[2]);
		int duration = ct + duration_time;
		running = true;
		System.out.println("\nProcess removed from queue is: id = " + b_2[0] + ", at time " + 
		ct + ", wait time = " + wait_time + " Total wait time = " + total_waittime);
		System.out.println("Process id = " + b_2[0]);
		System.out.println("\tPriority = " + b_2[1]);
		System.out.println("\tArrival = " + b_2[3]);
		System.out.println("\tDuration = " + b_2[2]);
		String a = duration + " " + total_waittime + " " + b_2[0];
		return a;
		
	}
}
