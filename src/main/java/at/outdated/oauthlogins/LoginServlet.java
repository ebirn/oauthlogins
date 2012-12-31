package at.outdated.oauthlogins;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ebirn
 * Date: 30.12.12
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {


    @EJB
    OAuthLogin login;

    @Inject
    LoginInfo info;

    @Override
    public void init() throws ServletException {

        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {


        // this decides whether full login is done or accesstoken can be reused
        if(info.getAccessToken() == null) {
            String authUrl = login.requestAuthUrl();
            httpServletResponse.sendRedirect(authUrl);
        }
        // reuse access token, verify login
        else {
            boolean loginOk = login.verifyLogin();
            if(loginOk) {
                handleLoginOK(httpServletRequest,httpServletResponse);
            }
            else {
                handleLoginFAIL(httpServletRequest,httpServletResponse);
            }
        }

    }

    // the GET handles the oauth API callbacks with verifier
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        boolean loginOk = false;

        try {
            login.processVerifier(httpServletRequest);
            loginOk = login.verifyLogin();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if(loginOk) {
                handleLoginOK(httpServletRequest, httpServletResponse);
            }
            else {
                handleLoginFAIL(httpServletRequest,httpServletResponse);
            }
        }
    }


    // Point for override/change: successful login,auth
    protected void handleLoginOK(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        // ensure existing http session
        httpServletRequest.getSession(true);

        login.updateUserInfo();

        httpServletResponse.sendRedirect(getServletContext().getContextPath() + "/hello.xhtml");
    }


    // Point for override/change: login error handling
    protected void handleLoginFAIL(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.logout();
        httpServletResponse.sendError(403, "login failed");
    }
}


