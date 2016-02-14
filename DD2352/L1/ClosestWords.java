/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurssidan http://www.csc.kth.se/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

public class ClosestWords {
	LinkedList<String> closestWords = null;

	int closestDistance = -1;

	static int MAX_WORD_LENGTH = 42;
	static int INFINITY = 999999;
	static int M[][] = new int[MAX_WORD_LENGTH][MAX_WORD_LENGTH];

	static {
		for (int i = 0; i < MAX_WORD_LENGTH; i++)
			M[i][0] = M[0][i] = i;  // edit distance for those empty string transitions
	}

	int partDistDP(String w1, String w2, int m, int n) { // DP
		char[] A = w1.toCharArray();
		char[] B = w2.toCharArray();

		// w1 is laid out vertically, and w2 is laid out horizontally!
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (A[i - 1] == B[j - 1]) {
					M[i][j] = M[i - 1][j - 1];
				} else {
					M[i][j] = 1 + min(M[i - 1][j - 1], M[i - 1][j], M[i][j - 1]);
				}
			}
		}

		return M[m][n];
	}

	int partDistDPO(String input, String currentWord, String lastWord) { // DP with optimization to reuse the M matrix from the last dictionary word
		int inputLength = input.length();
		int currentWordLength = currentWord.length();

		// "input" is laid out vertically, "currentWord" and "lastWord"
		// are laid out horizontally (as the usual convention).
		//
		// "currentWord" and "lastWord" come from the dictionary.

		// If currentWord matches lastWord for first "p" character then,
		// The M matrix is same for both cases from M[0][0] to
		// M[inputLength][p]
		int start = 1;
		int len = currentWordLength < lastWord.length() ? currentWordLength:lastWord.length();
		for (int i = 0; i < len && currentWord.charAt(i) == lastWord.charAt(i); i++) {
			start++;  // optimization, comment out this line to disable matrix reuse!
			// System.out.println(start);
		}

		// optimization, do we already have a better minimum edit distance?
		if (currentWordLength - inputLength > getMinDistance() && getMinDistance() != -1) {
			return INFINITY;
		}

		for (int i = 1; i <= inputLength; i++) {
			for (int j = start; j <= currentWordLength; j++) {
				if (input.charAt(i - 1) == currentWord.charAt(j - 1)) {
					M[i][j] = M[i - 1][j - 1];
				} else {
					M[i][j] = 1 + min(M[i - 1][j - 1], M[i - 1][j], M[i][j - 1]);
				}

			}
		}

		return M[inputLength][currentWordLength];
	}

	private int min(int a, int b, int c) {
		return Math.min(Math.min(a,b), c);
	}

	int Distance(String w1, String w2) {
		return partDistDP(w1, w2, w1.length(), w2.length());
	}

	public ClosestWords(String w, List<String> wordList) {
		String lastWord = "";
		for (String s : wordList) {
			int dist = partDistDPO(w, s, lastWord);
			// int dist = Distance(w, s);
			// System.out.println("d(" + w + "," + s + ")=" + dist);
			if (dist < closestDistance || closestDistance == -1) {
				closestDistance = dist;
				closestWords = new LinkedList<String>();
				closestWords.add(s);
			}
			else if (dist == closestDistance)
				closestWords.add(s);

			// keep track of the last word from the wordList
			if (dist != INFINITY) // we skipped over this particular word, so don't track it
				lastWord = s;
		}
	}

	public ClosestWords() { // used for testing

	}

	int getMinDistance() {
		return closestDistance;
	}

	List<String> getClosestWords() {
		return closestWords;
	}
}
