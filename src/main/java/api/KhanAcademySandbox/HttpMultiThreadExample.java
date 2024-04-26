package api.KhanAcademySandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class HttpMultiThreadExample {
	public static void example() {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Callable<String> callableTask = () -> {
			HttpSender.httpGet("https://api.hypixel.net/v2/resources/achievements");
		    return 	HttpSender.httpGet("https://api.hypixel.net/v2/resources/achievements");
		};
		List<Callable<String>> callableTasks = new ArrayList<>();
		for(int i=0; i<10; i++) {
			callableTasks.add(callableTask);
		}
		System.out.println(callableTasks.size());
		try {
			List<Future<String>> result = executor.invokeAll(callableTasks);
			for(Future<String> future:result) {
				String str=future.get();
				System.out.println(str.substring(0,Math.min(50, str.length()))+"...");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}
	
	public static void example2() {
		String[] courseid= {"x6b17ba59","x2eef969c74e0d802","xaacaf77b"};
		ExecutorService executor = Executors.newFixedThreadPool(courseid.length);
		List<Callable<String>> callableTasks = new ArrayList<>();
		for(String id:courseid) {
			callableTasks.add(new CourseCallable(id));
		}
		System.out.println(callableTasks.size());
		try {
			List<Future<String>> result = executor.invokeAll(callableTasks);
			for(Future<String> future:result) {
				String str=future.get();
				System.out.println(str.substring(0,Math.min(50, str.length()))+"...");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}
}
