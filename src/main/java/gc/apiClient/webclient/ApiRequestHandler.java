package gc.apiClient.webclient;

import org.springframework.web.util.UriComponents;

import org.springframework.web.util.UriComponentsBuilder;


import java.util.ArrayList;
import java.util.Arrays;


/**
 * URI를 만들어주는 클래스
 * api를 호출할때 Path parameter, 혹은 (Path parameter,Query Parameters)와 함께 호출하는 경우가 있는데 
 * Path parameter, 혹은 (Path parameter,Query Parameters)와 함께 호출되는 경우를 다루는 클래스이다. 
 * 
 */
public class ApiRequestHandler {

	public UriComponents buildApiRequest(String path, Object... pathVariables) {// uri를 api에 맞게 커스터 마이징.

		String BASE_URL = WebClientConfig.getBaseUrl();

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(BASE_URL).path(path);//여기서 path는 endpoint.예)'"/api/v2/outbound/contactlists/{contactListId}/contacts"'  'WebClientConfig'클래스 참조. 

		// path parameter가 몇 개 필요한지 파악
		int cnt = 0;

		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i) == '{') {
				cnt++;
			}
		}
		
		Object[] pathVars; // path parameter를 복사하여 저장하는 변수
		Object[] queryParams; // query parameter를 복사하여 저장하는 변수
		if (pathVariables.length > 0) {

			// buildApiRequest 함수를 사용할 때, 2번째 인자부터는 path parameter나 query parameter를 쓰면 된다.
			// 하지만 순서는 path parameter부터 쓴다.
			pathVars = Arrays.copyOfRange(pathVariables, 0, cnt);// index 0번째부터 index cnt까지 복사.
			queryParams = Arrays.copyOfRange(pathVariables, cnt, pathVariables.length); // index cnt번째부터 index 끝까지 복사.

			UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(pathVars);// 먼저path paramter를 expand해준다.

			uriComponentsBuilder = UriComponentsBuilder.fromUri(uriComponents.toUri());
			
			if (queryParams.length > 0) {
				for (int i = 0; i < queryParams.length; i += 2) {
					uriComponentsBuilder.queryParam(String.valueOf(queryParams[i]), queryParams[i + 1]);// 그 다음 query. '키,값' 짝지어서. 
				}
			}

		} else { 
			UriComponents uriComponents = uriComponentsBuilder.buildAndExpand();

			uriComponentsBuilder = UriComponentsBuilder.fromUri(uriComponents.toUri());
		}

		return uriComponentsBuilder.build();
	}

	

	public UriComponents apiReqForContacts(String path, Object... pathVariables) {// uri를 api에 맞게 커스터 마이징.
		String BASE_URL = WebClientConfig.getBaseUrl();
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(BASE_URL).path(path);

		// path parameter가 몇 개 필요한지 파악
		int cnt = 0;

		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i) == '{') {
				cnt++;
			}
		}

		Object[] pathVars; // path parameter를 복사하여 저장하는 변수
		Object[] queryParams; // query parameter를 복사하여 저장하는 변수
		if (pathVariables.length > 0) {

			// buildApiRequest 함수를 사용할 때, 2번째 인자부터는 path parameter나 query parameter를 쓰면 된다.
			// 하지만 순서는 path parameter부터 쓴다.
			pathVars = Arrays.copyOfRange(pathVariables, 0, cnt);// index 0번째부터 index cnt까지 복사.
			queryParams = Arrays.copyOfRange(pathVariables, cnt, pathVariables.length); // index cnt번째부터 index 끝까지 복사.

			UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(pathVars);// 먼저path paramter를 expand해준다.

			uriComponentsBuilder = UriComponentsBuilder.fromUri(uriComponents.toUri());
			ArrayList<String> arr = new ArrayList<String>();
			if (queryParams.length > 0) {
				for (int i = 0; i < queryParams.length; i++) {
					arr.add(queryParams[i].toString());
				}

			}
			String result = arr.toString();
			result = result.substring(1, result.length() - 1);

			uriComponentsBuilder.queryParam("contactIds", result);// 그 다음 query parameter.

		} else { // path parameter나 query parameter가 아예 없이 endpoint만 있는 api
			UriComponents uriComponents = uriComponentsBuilder.buildAndExpand();

			uriComponentsBuilder = UriComponentsBuilder.fromUri(uriComponents.toUri());
		}

		return uriComponentsBuilder.build();
	}

}
