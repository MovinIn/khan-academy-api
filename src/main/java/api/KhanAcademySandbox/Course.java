package api.KhanAcademySandbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

public class Course {
	public static final String URL="https://www.khanacademy.org/api/internal/graphql/courseProgressQuery?lang=en&_="+User.URL_ID;
	public static final String QUERY="query courseProgressQuery($topicId: String!) {\n  user {\n    id\n    courseProgress(topicId: $topicId) {\n      currentMasteryV2 {\n        percentage\n        pointsEarned\n        __typename\n      }\n      masteryMap {\n        progressKey\n        status\n        __typename\n      }\n      unitProgresses {\n        currentMasteryV2 {\n          percentage\n          pointsEarned\n          __typename\n        }\n        unitId\n        __typename\n      }\n      __typename\n    }\n    exerciseData {\n      masteryChallengeStatus(courseId: $topicId) {\n        totalQuestions\n        isEligible\n        currentAttempt {\n          id\n          canResume\n          timeLeftSeconds\n          expirationTime\n          expirationPeriodHours\n          __typename\n        }\n        __typename\n      }\n      __typename\n    }\n    latestCourseChallengeAttempt(courseId: $topicId) {\n      id\n      numAttempted\n      numCorrect\n      completedDate\n      canResume\n      isCompleted\n      __typename\n    }\n    __typename\n  }\n}";
	private String topicId;
	private JSONArray masteryMap;
	private HashMap<String,Exercise> exercises;
	Course(String course) {
		exercises=new HashMap<String,Exercise>();
		topicId=course;
		try {
			StringEntity entity=new StringEntity(buildJSON());
			String result=HttpSender.httpPost(URL,entity);
			masteryMap=new JSONObject(result).getJSONObject("data").getJSONObject("user")
					.getJSONObject("courseProgress").getJSONArray("masteryMap");
			
			ExecutorService executor = Executors.newFixedThreadPool(masteryMap.length());
			List<Callable<Exercise>> callableTasks = new ArrayList<>();
			for(int i=0; i<masteryMap.length(); i++) {
				String s=masteryMap.getJSONObject(i).getString("progressKey").substring(1);
				callableTasks.add(new ExerciseCallable(s));
			}
			System.out.println("#tasks: "+callableTasks.size());
			try {
				List<Future<Exercise>> exFutures = executor.invokeAll(callableTasks);
				for(Future<Exercise> future:exFutures) {
					exercises.put(future.get().getId(), future.get());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			executor.shutdown();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private String buildJSON() {
		JSONObject main,variables;
		
		variables=new JSONObject();
		variables.put("topicId", topicId);
		
		main=new JSONObject();
		main.put("operationName", "courseProgressQuery");
		main.put("variables", variables);
		main.put("query", QUERY);
		return main.toString();
	}
	
	public String toString() {
		return exercises.toString();
	}
}
