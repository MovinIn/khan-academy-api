package api.KhanAcademySandbox;

import java.util.concurrent.Callable;

public class CourseCallable implements Callable<String> {
	private String id;
	public CourseCallable(String id) {
		this.id=id;
	}
	
	@Override
	public String call() throws Exception {
		Course c=new Course(id);
		return c.toString();
	}

}
