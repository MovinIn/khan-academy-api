package api.KhanAcademySandbox;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Scanner s=new Scanner(System.in);
    	while(s.hasNext()) {
    		String token = s.next();
    		System.out.println("Computing "+token+":");
    		if(token.equalsIgnoreCase("exercise")||token.equalsIgnoreCase("ex")) {
    			new Exercise(s.next());
    		}
    		else if(token.equalsIgnoreCase("testEx")) {
    			new Exercise("3002");
    		}
    		else if(token.equalsIgnoreCase("testPr")) {
    	    	System.out.println(Problem.createWithId("47765362","x63df74dcd5497c94"));
    		}
    		else if(token.equalsIgnoreCase("problem")||token.equalsIgnoreCase("pr")) {
    			String identifier=s.next();
    			Problem p=Problem.empty();
    			if(identifier.equalsIgnoreCase("sha"))
    				p=Problem.createWithSha(s.next(), s.next(), s.nextInt());
    			else if(identifier.equalsIgnoreCase("id"))
    				p=Problem.createWithId(s.next(),s.next());
				System.out.println(p);
    		}
    		else if(token.equalsIgnoreCase("exerciseProblems")||token.equalsIgnoreCase("expr")) {
    			System.out.println(new ExerciseProblems(s.next()));
    		}
    		else if(token.equalsIgnoreCase("learnmenu")) {
    			System.out.println(LearnMenu.getLearnMenuProgress());
    		}
    		else if(token.equalsIgnoreCase("course")) {
    			System.out.println(new Course(s.next()));
    		}
    	}
    }
}
