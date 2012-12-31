package at.outdated.oauthlogins.providers;

import at.outdated.oauthlogins.LoginInfo;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 30.12.12
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 */
public class FacebookApiProvider extends AuthApiProvider {

    @Override
    public String getName() {
        return "Facebook";
    }

    @Override
    public Class<? extends Api> getApiClass() {
        return FacebookApi.class;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String extractVerifier(HttpServletRequest request) {
        String verifierStr = request.getParameter("code");
        return super.extractVerifier(verifierStr);
    }


    @Override
    public String requestAccessToken() {
        accessToken = service.getAccessToken(null, verifier);
        return accessToken.getToken();
    }

    @Override
    public void updateLoginInfo(LoginInfo info) {

        String rawData = getUserData().getBody();

        System.out.println("Facebook info:");
        System.out.println(rawData);
        try {
            JSONObject jsonObject = new JSONObject(rawData);

            info.setUsername(jsonObject.getString("name"));
            info.setNickname(jsonObject.getString("username"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

}
