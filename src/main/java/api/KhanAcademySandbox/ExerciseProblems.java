package api.KhanAcademySandbox;

import java.util.HashMap;

public class ExerciseProblems {
	private String LAST_SHA="d61e237d022eb711db35bd8d7cd99a19aea5b06c";
	HashMap<String,Problem> problems=new HashMap<String,Problem>();
	ExerciseProblems(String exerciseId) {
		Problem p;
		int prevSize=0;
		do {
			prevSize=problems.size();
			p=Problem.createWithSha(exerciseId, LAST_SHA, prevSize+1);
			problems.put(LAST_SHA=p.getSha(), p);
		}
		while(problems.size()!=prevSize);
	}
	public String toString() {
		return problems.size()+" Problems:\n"+problems.toString();
	}
}
