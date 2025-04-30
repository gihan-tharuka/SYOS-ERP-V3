package model;

public class Cashier extends User {
    private String fullName;
    private String mobile;

    public Cashier(String username, String password, String fullName, String email, String mobile) {
        super(username, password, email);
        this.fullName = fullName;
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}


