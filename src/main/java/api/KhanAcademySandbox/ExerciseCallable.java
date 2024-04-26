package api.KhanAcademySandbox;

import java.util.concurrent.Callable;

public class ExerciseCallable implements Callable<Exercise> {
	private final String id;
	public ExerciseCallable(String id) {
		this.id=id;
	}
	
	@Override
	public Exercise call() throws Exception {
		System.out.println("LOADING Exercise: "+id);
		Exercise e=new Exercise(id);
		System.out.println("LOADED Exercise: "+id);
		return e;
	}

}
