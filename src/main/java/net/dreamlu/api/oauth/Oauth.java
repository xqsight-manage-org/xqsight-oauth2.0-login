package net.dreamlu.api.oauth;

import java.util.Map;
import java.util.Properties;

import com.xiaoleilu.hutool.setting.dialect.Props;

import net.dreamlu.api.util.HttpKit;
import net.dreamlu.api.util.HttpKitExt;

/**
 * Oauth 授权
 * @author L.cm
 * email: 596392912@qq.com
 * site:  http://www.dreamlu.net
 * @date Jun 24, 2013 9:58:25 PM
 */
public abstract class Oauth {
	
	
	private static transient  Properties prop = Props.getProp("oauth.properties");
	
	private String clientId;
	private String clientSecret;
	private String redirectUri;

	public Oauth() {
		String name = getSelf().getClass().getSimpleName();
		clientId = prop.getProperty(name + ".openid");
		clientSecret = prop.getProperty(name + ".openkey");
		redirectUri = prop.getProperty(name + ".redirect");
	}

	public abstract Oauth getSelf();

	protected String getAuthorizeUrl(String authorize, Map<String, String> params) {
		return HttpKitExt.initParams(authorize, params);
	}

	protected String doPost(String url, Map<String, String> params) {
		return HttpKit.post(url, HttpKitExt.map2Url(params));
	}

	protected String doGet(String url, Map<String, String> params) {
		return HttpKit.get(url, params);
	}

	protected String doGetWithHeaders(String url, Map<String, String> headers) {
		return HttpKit.get(url, null, headers);
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
}
