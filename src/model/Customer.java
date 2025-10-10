package model;

public class Customer extends User {
    private String mobile;

    public Customer(String username, String password, String email, String mobile) {
        super(username, password, email);
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
