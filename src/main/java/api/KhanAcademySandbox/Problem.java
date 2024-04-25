package api.KhanAcademySandbox;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

public class Problem {
	private final static String URL="https://www.khanacademy.org/api/internal/_mt/graphql/getAssessmentItem?lang=en&_="+User.URL_ID;
	private final static String QUERY="query getAssessmentItem($input: AssessmentItemInput!) {\n  assessmentItem(input: $input) {\n    item {\n      id\n      sha\n      problemType\n      itemData\n      __typename\n    }\n    error {\n      code\n      debugMessage\n      __typename\n    }\n    __typename\n  }\n}";
	private String id;
	private String sha;
	private JSONObject data;
	
	public static Problem empty() {
		return new Problem();
	}
	
	public static Problem createWithId(String exerciseId, String itemId) {
		Problem p=new Problem();
		String json=p.buildJSON(exerciseId,itemId);
		try {
			StringEntity entity=new StringEntity(json);
			String result=HttpSender.httpPost(URL,entity);
			p.setValues(result);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static Problem createWithSha(String ex,String sha,int probNum) throws IllegalArgumentException {
		Problem p=new Problem();
		String json=p.buildJSONSha(ex,sha,probNum);
		try {
			StringEntity entity=new StringEntity(json);
			String result=HttpSender.httpPost(URL,entity);
			p.setValues(result);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public void setValues(String httpResult) {
		JSONObject o=new JSONObject(httpResult).getJSONObject("data").getJSONObject("assessmentItem").getJSONObject("item");
		id=o.getString("id");
		sha=o.getString("sha");
		data=new JSONObject(o.getString("itemData"));
	}
	
	private Problem() {}
	
	private String buildJSONSha(String ex,String sha,int problemNum) {
		JSONObject input=new JSONObject();
		input.put("exerciseId", ex);
		input.put("lastSeenProblemSha", sha);
		input.put("problemNumber", problemNum);
		//input.put("quizProblemNumber", null);
		return buildJSON(input);
	}
	
	private String buildJSON(String ex,String item) {
		JSONObject input=new JSONObject();
		input.put("exerciseId", ex);
		input.put("itemId", item);
		return buildJSON(input);
	}
	
	private String buildJSON(JSONObject input) {
		JSONObject main=new JSONObject();
		JSONObject variables=new JSONObject();
		variables.put("input", input);
		main.put("operationName", "getAssessmentItem");
		main.put("variables", variables);
		main.put("query", QUERY);
		return main.toString();
	}
	
	public String toString() {
		return "id: "+id+"\nsha: "+sha+"\ndata: "+data;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSha() {
		return sha;
	}
	public void setSha(String sha) {
		this.sha = sha;
	}
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
}

