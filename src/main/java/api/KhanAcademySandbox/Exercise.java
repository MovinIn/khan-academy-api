package api.KhanAcademySandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class Exercise {
	public static final String URL="https://www.khanacademy.org/api/internal/graphql/getOrCreatePracticeTask?lang=en&_="+User.URL_ID;
	private final static String QUERY="mutation getOrCreatePracticeTask($input: GetOrCreatePracticeTaskInput!) {\n  getOrCreatePracticeTask(input: $input) {\n    result {\n      error {\n        code\n        debugMessage\n        __typename\n      }\n      userTask {\n        ...userTaskFields\n        __typename\n      }\n      __typename\n    }\n    __typename\n  }\n}\n\nfragment userExerciseFields on UserExercise {\n  exerciseModel: exercise {\n    id\n    assessmentItemCount: numAssessmentItems\n    displayName\n    isQuiz\n    isSkillCheck\n    name\n    nodeSlug\n    progressKey\n    translatedDisplayName\n    relatedContent {\n      id\n      contentKind\n      kind\n      thumbnailUrl\n      translatedTitle\n      topicPaths {\n        path {\n          id\n          kind\n          slug\n          __typename\n        }\n        __typename\n      }\n      ... on Article {\n        kaUrl\n        nodeSlug\n        relativeUrl\n        slug\n        __typename\n      }\n      ... on Video {\n        duration\n        imageUrl\n        kaUrl\n        nodeSlug\n        relativeUrl\n        slug\n        translatedYoutubeId\n        __typename\n      }\n      __typename\n    }\n    relatedVideos {\n      contentKind\n      duration\n      id\n      imageUrl\n      kaUrl\n      kind\n      nodeSlug\n      progressKey\n      relativeUrl\n      slug\n      thumbnailUrl\n      translatedDescription\n      translatedTitle\n      translatedYoutubeId\n      __typename\n    }\n    problemTypes {\n      items {\n        id\n        live\n        sha\n        __typename\n      }\n      name\n      relatedVideos\n      __typename\n    }\n    translatedProblemTypes {\n      items {\n        id\n        live\n        sha\n        __typename\n      }\n      name\n      relatedVideos\n      __typename\n    }\n    __typename\n  }\n  exercise: exerciseName\n  fpmMasteryLevel\n  lastAttemptNumber\n  lastCountHints\n  lastDone\n  lastMasteryUpdate\n  longestStreak\n  maximumExerciseProgressDt: maximumExerciseProgressDate\n  streak\n  totalCorrect\n  totalDone\n  __typename\n}\n\nfragment userTaskFields on PracticeUserTask {\n  cards {\n    done\n    cardType\n    ... on ProblemCard {\n      exerciseName\n      problemType\n      __typename\n    }\n    __typename\n  }\n  includeSkipButton\n  task {\n    contentKey\n    exerciseLength\n    id\n    key\n    pointBounty\n    pointsEarned\n    slug\n    taskType\n    timeEstimate {\n      lowerBound\n      upperBound\n      __typename\n    }\n    completionCriteria {\n      name\n      __typename\n    }\n    promotionCriteria {\n      name\n      value\n      __typename\n    }\n    reservedItems\n    reservedItemsCompleted\n    taskAttemptHistory {\n      correct\n      timeDone\n      seenHint\n      itemId\n      __typename\n    }\n    __typename\n  }\n  userExercises {\n    ...userExerciseFields\n    __typename\n  }\n  __typename\n}";
	private String id;
	private String data;
	private HashMap<String,Problem> problems;
	Exercise(String id) {
		problems=new HashMap<String,Problem>();
		this.id=id;
		try {
			String json=buildJSON();
			try {
				Future<SimpleHttpResponse> result=HttpSender.asyncPost(URL,json.getBytes());
				while(!result.isDone()) {
					Thread.sleep(100);
				}
				data=result.get().getBodyText();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			JSONArray o=new JSONObject(data).getJSONObject("data").getJSONObject("getOrCreatePracticeTask").getJSONObject("result")
					.getJSONObject("userTask").getJSONArray("userExercises").getJSONObject(0).getJSONObject("exerciseModel")
					.getJSONArray("problemTypes");
			List<String> list=new ArrayList<>();
			for(int i=0; i<o.length(); i++) {
				JSONArray items=o.getJSONObject(i).getJSONArray("items");
				for(int j=0; j<items.length(); j++) {
					list.add(items.getJSONObject(j).getString("id"));
				}
			}
			ExecutorService executor = Executors.newFixedThreadPool(list.size());
			List<Callable<Problem>> callableTasks = new ArrayList<>();
			for(String s:list) {
				callableTasks.add(new ProblemCallable(id,s));
			}
			try {
				List<Future<Problem>> p = executor.invokeAll(callableTasks);
				for(Future<Problem> future:p) {
					problems.put(future.get().getId(), future.get());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			executor.shutdown();
			//System.out.println(result);
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println("exceptione!");
			e.printStackTrace();
		}
	}
	
	private String buildJSON() {
		JSONObject main,variables,input;
		
		input=new JSONObject();
		input.put("exerciseId", id);
		input.put("canReserveItems", true);
		input.put("stopCardPersist", false);
		
		variables=new JSONObject();
		variables.put("input", input);
		
		main=new JSONObject();
		main.put("operationName", "getOrCreatePracticeTask");
		main.put("variables", variables);
		main.put("query", QUERY);
		return main.toString();
	}
	
	public String toString() {
		return data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
