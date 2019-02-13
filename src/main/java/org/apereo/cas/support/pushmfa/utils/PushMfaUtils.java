package org.apereo.cas.support.pushmfa.utils;

import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilities functions that assist with PushMfa functionality
 */
public class PushMfaUtils {

    public static final String PUSH_MFA_PRINCIPAL = "pushMfaPrincipal";

    /**
     * Records the principal in the Java Request's Session
     * @param flowRequestContext the flow context
     */
    public void setUser(RequestContext flowRequestContext){
        String principal = flowRequestContext.getViewScope().get("principal").toString();

        HttpServletRequest req = (HttpServletRequest) flowRequestContext.getExternalContext().getNativeRequest();
        req.getSession().setAttribute(PUSH_MFA_PRINCIPAL, principal);
    }

    /**
     * Clears the principal from the Java Request's Session
     * @param flowRequestContext the flow context
     */
    public void clearUser(RequestContext flowRequestContext){
        HttpServletRequest req = (HttpServletRequest) flowRequestContext.getExternalContext().getNativeRequest();
        req.getSession().setAttribute(PUSH_MFA_PRINCIPAL, null);
    }
}
