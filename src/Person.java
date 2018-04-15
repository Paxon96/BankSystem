public class Person {

    private String name;
    private String surname;
    private String pesel;
    private String address;

    Person(String name, String surname, String pesel, String address){
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.address = address;
    }

    public void setName(String name) { this.name = name; }

    public void setSurname(String surname) { this.surname = surname; }

    public void setPesel(String pesel) { this.pesel = pesel; }

    public void setAddress(String address) { this.address = address; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getPesel() { return pesel; }

    public String getAddress() { return address; }

    @Override
    public String toString() {
        return name + " "+ surname+ " " + pesel+ " " + address ;
    }
}