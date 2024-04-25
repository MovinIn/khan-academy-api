package api.KhanAcademySandbox;

import java.io.IOException;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class LearnMenu {
	public static final String URL = "https://www.khanacademy.org/api/internal/graphql/getLearnMenuProgress?lang=en&_="+User.URL_ID;
	public static final String QUERY="query getLearnMenuProgress($slugs: [String!]) {\n  user {\n    id\n    subjectProgressesBySlug(slugs: $slugs) {\n      topic {\n        id\n        slug\n        __typename\n      }\n      currentMastery {\n        percentage\n        __typename\n      }\n      __typename\n    }\n    __typename\n  }\n}";
	public static String getLearnMenuProgress() {
		String result="";
		try {
			StringEntity entity=new StringEntity(buildJSON(User.SLUG_DATA));
			result=HttpSender.httpPost(URL,entity);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String buildJSON(JSONArray slugs) {
		JSONObject main=new JSONObject();
		JSONObject variables=new JSONObject();
		variables.put("slugs", slugs);
		main.put("operationName", "getLearnMenuProgress");
		main.put("variables", variables);
		main.put("query", QUERY);
		return main.toString();
	}
}
