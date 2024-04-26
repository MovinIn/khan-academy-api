package api.KhanAcademySandbox;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
	private static String token;
    public static void main( String[] args )
    {
    	Scanner s=new Scanner(System.in);
    	while(s.hasNext()) {
    		token = s.next();
			long time=System.currentTimeMillis();
    		System.out.println("Computing "+token+":");
    		if(eq("exercise")||eq("ex")) {
    			new Exercise(s.next());
    		}
    		else if(eq("testEx")) {
    			System.out.println(new Exercise("3002"));
    		}
    		else if(eq("testPr")) {
    	    	System.out.println(Problem.createWithId("47765362","x63df74dcd5497c94"));
    		}
    		else if(eq("problem")||eq("pr")) {
    			String identifier=s.next();
    			Problem p=Problem.empty();
    			if(identifier.equalsIgnoreCase("sha"))
    				p=Problem.createWithSha(s.next(), s.next(), s.nextInt());
    			else if(identifier.equalsIgnoreCase("id"))
    				p=Problem.createWithId(s.next(),s.next());
				System.out.println(p);
    		}
    		else if(eq("exerciseProblems")||eq("expr")) {
    			System.out.println(new ExerciseProblems(s.next()));
    		}
    		else if(eq("learnmenu")) {
    			System.out.println(LearnMenu.getLearnMenuProgress());
    		}
    		else if(eq("course")) {
    			System.out.println(new Course(s.next()));
    		}
    		else if(eq("threads1")) {
    			HttpMultiThreadExample.example();
    		}
    		else if(eq("threads2")) {
    			HttpMultiThreadExample.example2();
    		}
    		else if(eq("q")) {
    			s.close();
    			break;
    		}
    		System.out.println("##########################################################");
    		System.out.println("Time Taken: "+(System.currentTimeMillis()-time)+"ms");
    	}
    }
    private static boolean eq(String other) {
    	return token.equalsIgnoreCase(other);
    }
}
