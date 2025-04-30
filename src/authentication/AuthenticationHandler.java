package authentication;

public abstract class AuthenticationHandler {
    protected AuthenticationHandler nextHandler;

    public void setNextHandler(AuthenticationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract String handleRequest(String username, String password, String role);
}

//package authentication;
//
//public abstract class AuthenticationHandler {
//    protected AuthenticationHandler nextHandler;
//
//    public void setNextHandler(AuthenticationHandler nextHandler) {
//        this.nextHandler = nextHandler;
//    }
//
//    public abstract boolean handleRequest(String username, String password, String role);
//}
