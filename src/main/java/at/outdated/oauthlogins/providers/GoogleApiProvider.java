package at.outdated.oauthlogins.providers;

import at.outdated.oauthlogins.LoginInfo;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 31.12.12
 * Time: 08:33
 * To change this template use File | Settings | File Templates.
 */

public class GoogleApiProvider extends AuthApiProvider {



    @Override
    public String getName() {
        return "Google";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends Api> getApiClass() {
        return GoogleApi.class;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String extractVerifier(HttpServletRequest request) {

        String code = request.getParameter("oauth_verifier");
        super.extractVerifier(code);

        return code;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void updateLoginInfo(LoginInfo info) {
        //To change body of implemented methods use File | Settings | File Templates.

        Response res = getUserData();
        try {
            JSONObject json = new JSONObject(res.getBody());
            info.setRemoteId(json.getString("id"));
            info.setEmail(json.getString("email"));
            info.setUsername(json.getString("name"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        info.setRemoteApi(getName());
    }


    @Override
    public String requestAuthUrl() {
        requestToken = service.getRequestToken();
        return super.requestAuthUrl(requestToken);
    }


    @Override
    public ServiceBuilder buildService() {
        ServiceBuilder builder = super.buildService();

        builder.scope(prop.getProperty("google.api.scope"));

        return builder;
    }
}
