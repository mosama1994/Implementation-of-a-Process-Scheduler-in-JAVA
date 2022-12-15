import java.util.Comparator;

public class StringComparator1 implements Comparator<String>{
	// Class string comparator made to implement the comparator interface. The compare method is overridden as we do not want 
	// natural ordering. The strings are ordered by their arrival time. This is used for priority Queue D.
	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		String[] words1 = o1.split("\\s+");
		String[] words2 = o2.split("\\s+");
		// String is split based on white spaces and the last element (arrival time) is used to order the priority queue. 
		int a = Integer.parseInt(words1[words1.length-1]);
		int b = Integer.parseInt(words2[words2.length-1]);
		if (a > b) {
			return 1;
		}
		else {
			return -1;
		}
	}
}
