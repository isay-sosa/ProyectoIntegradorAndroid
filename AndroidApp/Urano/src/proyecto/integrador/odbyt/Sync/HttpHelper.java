package proyecto.integrador.odbyt.Sync;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpHelper {
	private String _method = "", _url = "";

	public HttpHelper() {

	}

	public HttpHelper(String _method) {
		setMethod(_method);
	}

	public HttpHelper(String _method, String _url) {
		setMethod(_method);
		setUrl(_url);
	}

	public void setMethod(String _method) {
		_method = _method.toUpperCase();

		if (_method.equals("POST"))
			this._method = "POST";
		else if (_method.equals("GET"))
			this._method = "GET";
	}

	public String getMetod() {
		return _method;
	}

	public void setUrl(String _url) {
		this._url = _url;
	}

	public String getUrl() {
		return _url;
	}

	public String getJSONData(ArrayList<NameValuePair> _params) {
		String JSONData = "";
		InputStream is = null;

		if (_method.length() > 0 && _url.length() > 0) {
			if (_method.equals("POST")) {
				is = getInputStreamFromHttpPostConnection(_params);
			} else if (_method.equals("GET")) {
				is = getInputStreamFromHttpGetConnection();
			}
			
			if (is != null)
				JSONData = getResponse(is);
		}

		return JSONData;
	}

	public String getJSONData(String _method, String _url,
			ArrayList<NameValuePair> _params) {
		setMethod(_method);
		setUrl(_url);

		return getJSONData(_params);
	}

	public InputStream getInputStreamFromHttpPostConnection(
			ArrayList<NameValuePair> _params) {
		InputStream is = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(_url);
			httppost.setEntity(new UrlEncodedFormEntity(_params));
			
			// ejecuto peticion enviando datos por POST
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			is = null;
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		return is;
	}

	public InputStream getInputStreamFromHttpGetConnection() {
		InputStream is = null;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(_url);
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			is = null;
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		
		return is;
	}

	public String getResponse(InputStream is) {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return builder.toString();
	}
}
