package at.outdated.oauthlogins.providers;

import at.outdated.oauthlogins.LoginInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 30.12.12
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
public class TwitterApiProvider extends AuthApiProvider {



    @Override
    public String getName() {
        return "Twitter";
    }

    @Override
    public Class<? extends Api> getApiClass() {
        return TwitterApi.class;
    }

    @Override
    public String requestAuthUrl() {

        requestToken = service.getRequestToken();
        System.out.println("requestToken: " + requestToken);

        return super.requestAuthUrl(requestToken);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String extractVerifier(HttpServletRequest request) {
        String verifierStr = request.getParameter("oauth_verifier");
        String v = super.extractVerifier(verifierStr);

        //requestAccessToken();

        return v;
    }


    @Override
    public void updateLoginInfo(LoginInfo login) {

        String rawData = getUserData().getBody();

        try {

            JSONObject jsonInfo = new JSONObject(rawData);

            login.setUsername(jsonInfo.getString("screen_name"));
            login.setEmail(jsonInfo.getString("name"));
            login.setRemoteId(jsonInfo.getString("id"));

        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

}
