import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;

public class MockSecurityContext implements SecurityContext {
    private Principal principal;
    public MockSecurityContext(String name) {
        principal = new Principal() {
            private String principalName = name;

            public String getName() {
                return principalName;
            }
        };
    }

    public String getAuthenticationScheme() {
        return "Bearer";
    }

    public Principal getUserPrincipal() {
        return this.principal;
    }

    public boolean isSecure() {
        return true;
    }

    public boolean isUserInRole(String role) {
        return false;
    }
}