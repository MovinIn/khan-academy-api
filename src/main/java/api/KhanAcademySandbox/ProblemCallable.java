package api.KhanAcademySandbox;

import java.util.concurrent.Callable;

public class ProblemCallable implements Callable<Problem> {

	private final String ex,id;
	public ProblemCallable(String ex,String id) {
		this.ex=ex;
		this.id=id;
	}
	
	@Override
	public Problem call() throws Exception {
		Problem p=Problem.createWithId(ex, id);
		return p;
	}

}
