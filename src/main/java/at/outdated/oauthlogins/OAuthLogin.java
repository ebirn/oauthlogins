package at.outdated.oauthlogins;

import org.scribe.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 30.12.12
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */

@Stateless
public class OAuthLogin {

    @Inject
    LoginInfo login;

    @PostConstruct
    public void init() {

    }

    public String requestAuthUrl() {
         return login.getApiProvider().requestAuthUrl();
    }

    public String processVerifier(HttpServletRequest request) {
        return login.getApiProvider().extractVerifier(request);
    }

    public boolean verifyLogin() {


        Response response = login.getApiProvider().getUserData();

        boolean success = response.isSuccessful();
        if(success) {
            System.out.println("LOGIN SUCCESSFUL");
        }
        else {
            System.err.println("LOGIN FAILED");
        }

        return success;
        //
    }

    public void updateUserInfo() {
        login.getApiProvider().updateLoginInfo(login);
    }


}
