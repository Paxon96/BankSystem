public class BankUser {

    private int userId;
    private Person user;
    private double funds;

    BankUser(int userId, Person user, double funds){
        this.userId = userId;
        this.user = user;
        this.funds = funds;
    }

    public int getUserId() { return userId; }

    public Person getUser() { return user; }

    public double getFunds() { return funds; }

    public void addFunds(double fundsToAdd){
        double currentFunds = getFunds();
        this.funds = currentFunds + fundsToAdd;
    }

    public void withdrawFunds(double fundsToWithdraw){
        double currentFunds = getFunds();
        this.funds = currentFunds - fundsToWithdraw;
    }

    @Override
    public String toString() {
        return userId + " " + user + " " + funds;
    }

}
