import java.util.Comparator;

public class StringComparator2 implements Comparator<String>{
	// Class string comparator made to implement the comparator interface. The compare method is overridden as we do not want 
	// natural ordering. The strings are ordered by their priority. This is used for priority queue Q.
	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		String[] words1 = o1.split("\\s+");
		String[] words2 = o2.split("\\s+");
		// String is split based on white spaces and the second element (priority) is used to order the priority queue. 
		int a = Integer.parseInt(words1[1]);
		int b = Integer.parseInt(words2[1]);
		if (a > b) {
			return 1;
		}
		else {
			return -1;
		}
	}

}
