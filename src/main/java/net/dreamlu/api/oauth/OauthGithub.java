package net.dreamlu.api.oauth;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.dreamlu.api.util.TokenUtil;

/**
 * github 登录
 * @author L.cm
 * email: 596392912@qq.com
 * site:  http://www.dreamlu.net
 * @date Jun 24, 2013 10:18:23 PM
 * 
 * DOC :https://developer.github.com/v3/oauth/
 */
public class OauthGithub extends Oauth {

	private static final Logger LOGGER = LogManager.getLogger(OauthGithub.class);
	
	private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
	private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
	private static final String USER_INFO_URL = "https://api.github.com/user";

	private static OauthGithub oauthGithub = new OauthGithub();

	/**
	 * 用于链式操作
	 * @return
	 */
	public static OauthGithub me() {
		return oauthGithub;
	}

	/**
	 * @throws UnsupportedEncodingException 
	 * 获取授权url
	 * @param @return	设定文件
	 * @return String	返回类型
	 * @throws
	 */ 
	public String getAuthorizeUrl(String state) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("response_type", "code");
		params.put("client_id", getClientId());
		params.put("redirect_uri", getRedirectUri());
		if (StringUtils.isBlank(state)) {
			params.put("state", state); //OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中
		}
		return super.getAuthorizeUrl(AUTH_URL, params);
	}

	/**
	 * 获取token
	 * @param @param code
	 * @param @return	设定文件
	 * @return String	返回类型
	 * @throws
	 */
	public String getTokenByCode(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("client_id", getClientId());
		params.put("client_secret", getClientSecret());
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", getRedirectUri());
		String token = TokenUtil.getAccessToken(super.doPost(TOKEN_URL, params));
		LOGGER.debug(token);
		return token;
	}

	/**
	 *  获取用户信息
	 * @param accessToken
	 * @return
	 */
	public JSONObject getUserInfo(String accessToken) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Authorization", "token " + accessToken);
		String userInfo = super.doGetWithHeaders(USER_INFO_URL, params);
		JSONObject dataMap = JSON.parseObject(userInfo);
		LOGGER.debug(dataMap.toJSONString());
		return dataMap;
	}
	
	/**
	 * 根据code一步获取用户信息
	 * @param @param args	设定文件
	 * @return void	返回类型
	 * @throws
	 */
	public JSONObject getUserInfoByCode(String code) {
		String accessToken = getTokenByCode(code);
		if (StringUtils.isBlank(accessToken)) {
			return null;
		}
		JSONObject dataMap = getUserInfo(accessToken);
		dataMap.put("access_token", accessToken);
		return dataMap;
	}
	
	@Override
	public Oauth getSelf() {
		return this;
	}
}
