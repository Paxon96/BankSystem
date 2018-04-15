import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

// poprawic deleteUser na koniec


public class SystemBankowy {

    private ArrayList<BankUser> usersList = new ArrayList<>();
    private Scanner in = new Scanner(System.in);


    public void BankInterface() {
        usersList = loadDatabase();
        int choice = 0;
        boolean end = false, good;
        do {
            do {
                good = true;
                System.out.println("1 - Add User\n2 - Delete User\n3 - Display Users\n4 - Find User\n5 - Add Funds\n6 - Transfer Funds\n7 - Withdraw Funds\n8 - End");
                try {
                    choice = in.nextInt();
                } catch (InputMismatchException e) {
                    choice = 0;
                    System.out.println("Zle");
                    good = false;
                    in.nextLine();
                }
                if ((choice < 1 || choice > 8) && good)
                    System.out.println("Zly numer");
            } while (choice < 1 || choice > 8);

            if (confirm()) switch (choice) {
                case 1:
                    addUser();
                    saveDatabase(usersList);
                    break;
                case 2:
                    deleteUser();
                    saveDatabase(usersList);
                    break;
                case 3:
                    displayUsers();
                    break;
                case 4:
                    findUser();
                    break;
                case 5:
                    addFunds();
                    saveDatabase(usersList);
                    break;
                case 6:
                    boolean flag;
                    int indexToTakeFunds = 0, indexToGiveFunds = 0;
                    double givenFunds = 0;
                    displayUsers();

                    do{
                        flag = false;
                        System.out.println("Podaj id uzytkownika, od ktorego chcesz wyplacic fundusze: ");
                        try{
                            indexToTakeFunds = findUserInDatabase(in.nextInt());
                        }catch (InputMismatchException e) {
                            System.out.println("Wprowadzona wartosc nie jest liczba!\nWprowadz ja jeszcze raz.");
                            flag = true;
                            in.nextLine();
                        }

                        if(indexToTakeFunds == -1 && !flag){
                            System.out.println("Id nie moze.\nWprowadz je jeszcze raz: ");
                            flag = true;
                        }
                    }while(flag);

                    do{
                        flag = false;
                        System.out.println("Podaj id uzytkownika, ktoremu chcesz wplacic pieniadze: ");
                        try{
                            indexToGiveFunds = findUserInDatabase(in.nextInt());
                        }catch (InputMismatchException e) {
                            System.out.println("Wprowadzona wartosc nie jest liczba!\nWprowadz ja jeszcze raz.");
                            flag = true;
                            in.nextLine();
                        }

                        if(indexToGiveFunds == -1 && !flag){
                            System.out.println("Id nie moze.\nWprowadz je jeszcze raz: ");
                            flag = true;
                        }
                    }while(flag);

                    do{
                        flag = false;
                        System.out.println("Wprowadz ilosc funduszy ktore chcesz wyplacic : ");
                        try{
                            givenFunds = in.nextDouble();
                        }catch(InputMismatchException e){
                            System.out.println("Podana wartosc nie jest liczba!\nWprowadz ja jeszcze raz.");
                            flag = true;
                            in.nextLine();
                        }

                        if(givenFunds <=0 && !flag){
                            System.out.println("Fundusze nie mogą byc mniejsze lub rowne zeru.\nWprowadz je jeszze raz.");
                            flag = true;
                        }else if(givenFunds > usersList.get(indexToTakeFunds).getFunds() && !flag){
                            System.out.println("Nie mozesz przelac wiecej funduszy niz uzytkownika ma na koncie!\nWprowadz je jeszcze raz: ");
                            flag = true;
                        }
                    }while(flag);

                    usersList.get(indexToTakeFunds).withdrawFunds(givenFunds);
                    usersList.get(indexToGiveFunds).addFunds(givenFunds);

                    break;
                case 7:
                    withdrawFunds();
                    saveDatabase(usersList);
                    break;
                case 8:
                    System.out.println("Pa Pa");
                    end = true;
                    saveDatabase(usersList);
                    break;

            }

        } while (!end);
    }

    private void withdrawFunds(){
        boolean flag;
        int indexToWithdrawFunds = 0;
        double fundsToWithdraw = 0;
        displayUsers();
        do{
            flag = false;
            System.out.println("Podaj id uzytkownika, od ktorego chcesz wyplacic fundusze: ");
            try{
                indexToWithdrawFunds = findUserInDatabase(in.nextInt());
            }catch (InputMismatchException e) {
                System.out.println("Wprowadzona wartosc nie jest liczba!\nWprowadz ja jeszcze raz.");
                flag = true;
                in.nextLine();
            }

            if(indexToWithdrawFunds == -1 && !flag){
                System.out.println("Id nie moze.\nWprowadz je jeszcze raz: ");
                flag = true;
            }
        }while(flag);

        do{
            flag = false;
            System.out.println("Wprowadz ilosc funduszy ktore chcesz wyplacic : ");
            try{
                fundsToWithdraw = in.nextDouble();
            }catch(InputMismatchException e){
                System.out.println("Podana wartosc nie jest liczba!\nWprowadz ja jeszcze raz.");
                flag = true;
                in.nextLine();
            }

            if(fundsToWithdraw <=0 && !flag){
                System.out.println("Fundusze nie mogą byc mniejsze lub rowne zeru.\nWprowadz je jeszze raz.");
                flag = true;
            }else if(fundsToWithdraw > usersList.get(indexToWithdrawFunds).getFunds() && !flag){
                System.out.println("Nie mozesz wyplacic wiecej pieniedzy niz masz na koncie!\nWprowadz ilosc funduszy raz jeszcze: ");
                flag = true;
            }
        }while(flag);

        usersList.get(indexToWithdrawFunds).withdrawFunds(fundsToWithdraw);
    }

    private void addFunds(){
        boolean flag;
        int indexToAddFunds = 0;
        double fundsToAdd = 0;
        displayUsers();
        do{
            flag = false;
            System.out.println("Podaj id uzytkownika, ktoremu chcesz wplacic pieniadze: ");
            try{
                indexToAddFunds = findUserInDatabase(in.nextInt());
            }catch (InputMismatchException e) {
                System.out.println("Wprowadzona wartosc nie jest liczba!\nWprowadz ja jeszcze raz.");
                flag = true;
                in.nextLine();
            }

            if(indexToAddFunds == -1 && !flag){
                System.out.println("Id nie moze.\nWprowadz je jeszcze raz: ");
                flag = true;
            }
        }while(flag);

        do{
            flag = false;
            System.out.println("Wprowadz ilosc funduszy ktore chcesz wplacic danemu uzytkownikowi: ");
            try{
                fundsToAdd = in.nextDouble();
            }catch(InputMismatchException e){
                System.out.println("Podana wartosc nie jest liczba!\nWprowadz ja jeszcze raz.");
                flag = true;
                in.nextLine();
            }

            if(fundsToAdd <=0 && !flag){
                System.out.println("Fundusze nie mogą byc mniejsze lub rowne zeru.\nWprowadz je jeszze raz.");
                flag = true;
            }
        }while(flag);

        usersList.get(indexToAddFunds).addFunds(fundsToAdd);

    }
    private void displayUsers(){
        for (BankUser i : usersList)
            System.out.println(i);
    }
    private void findUser(){
        boolean flag;
        String CriterionToSearch, Criterion;
        int id = 0;
        in.nextLine();
        do {
            flag = false;
            System.out.println("Jakie kryterium chcesz przyjac aby znalesc uzytkownika? [imie/nazwisko/adres/pesel/id]");
            CriterionToSearch = in.nextLine();

            if (!isTheCriterionToSearchCorrect(CriterionToSearch)) {
                System.out.println("Wproawdzony ciag znakow nie jest poprawnym kryterium!\nWprowadz je jeszcze raz.");
                flag = true;
            }

        } while (flag);


        do {
            flag = false;
            System.out.println("Podaj " + CriterionToSearch + " (ktore/ktory) chcesz wyszukac: ");
            Criterion = in.nextLine();

            if (CriterionToSearch.equalsIgnoreCase("pesel") && !ifPeselIsRight(Criterion)) {
                System.out.println("Podany ciag znakow nie zpelnia kryterium pesela!\nWprowadz go jeszcze raz: ");
                flag = true;
            } else if (CriterionToSearch.equalsIgnoreCase("adres")){
                if(Criterion.length() == 0){
                    System.out.println("Adres nie moze byc pusty!\nWprowadz go jeszcze raz: ");
                    flag = true;
                }
            }else if(CriterionToSearch.equalsIgnoreCase("id")){
                try{
                    id = Integer.parseInt(Criterion);
                }catch(NumberFormatException e){
                    System.out.println("Wprowadzony ciag znakow nie jest liczba!\nWprowdz go jeszcze raz: ");
                    flag = true;
                }
            }else if(!CriterionToSearch.equalsIgnoreCase("pesel") && !CriterionToSearch.equalsIgnoreCase("adres")){
                if(!ifStringIsRight(Criterion)){
                    System.out.println("Podany ciag znakow nie spelnia kryterium "+ CriterionToSearch + "\nWprowadz go jeszcze raz: ");
                    flag = true;
                }
            }
        } while (flag);

        switch (CriterionToSearch.toLowerCase()) {
            case ("imie"):
                for (BankUser i : usersList) {
                    if (i.getUser().getName().equalsIgnoreCase(Criterion)){
                        System.out.println(i);
                        flag = true;
                    }
                }
                if(!flag)
                    System.out.println("Uzytkownika o podanym imieniu nie ma w bazie danych!");

                break;

            case ("nazwisko"):
                for (BankUser i : usersList) {
                    if (i.getUser().getSurname().equalsIgnoreCase(Criterion)){
                        System.out.println(i);
                        flag = true;
                    }
                }
                if(!flag)
                    System.out.println("Uzytkownika o podanym nazwisku nie ma w bazie danych!");

                break;

            case ("adres"):
                for (BankUser i : usersList) {
                    if (i.getUser().getAddress().equalsIgnoreCase(Criterion)){
                        System.out.println(i);
                        flag = true;
                    }
                }
                if(!flag)
                    System.out.println("Uzytkownika o podanym adresie nie ma w bazie danych!");

                break;

            case("pesel"):
                for (BankUser i : usersList) {
                    if (i.getUser().getPesel().equals(Criterion)){
                        System.out.println(i);
                        flag = true;
                    }
                }
                if(!flag)
                    System.out.println("Uzytkownika o podanym peselu nie ma w bazie danych!");
                break;
            case("id"):
                for (BankUser i : usersList) {
                    if (i.getUserId() == id){
                        System.out.println(i);
                        flag = true;
                    }
                }
                if(!flag)
                    System.out.println("Uzytkownika o podanym id nie ma w bazie danych!");
                break;
        }
    }

    private void addUser(){

        boolean flag;
        int getUserId;
        String getUserName, getUserSurname, getUserPesel, getUserAddress;
        double getUserFunds = 0;
        int nextFreeId = usersList.get(usersList.size() - 1).getUserId() + 1;
        in.nextLine();
        do {
            flag = false;
            System.out.println("Podaj imie: ");
            getUserName = in.nextLine();
            if(!ifStringIsRight(getUserName) || getUserName.length() == 0){
                flag = true;
                System.out.println("Podany ciag znakow nie spelnia standardow imienia!\nWprowadz imie jeszcze raz.");
            }
        } while (flag);

        do {
            flag = false;
            System.out.println("Podaj nazwisko: ");
            getUserSurname = in.nextLine();
            if(!ifStringIsRight(getUserSurname) || getUserSurname.length() == 0){
                flag = true;
                System.out.println("Podany ciag znakow nie spelnia standardow nazwiska!\nWprowadz nazwisko jeszcze raz.");
            }
        } while (flag);

        do {
            flag = false;
            System.out.println("Podaj pesel: ");
            getUserPesel = in.nextLine();
            if(!ifPeselIsRight(getUserPesel)){
                flag = true;
                System.out.println("Podany ciag znakow nie spelnia standardow peselu!\nWprowadz pesel jeszcze raz.");
            }
        } while (flag);

        do {
            flag = false;
            System.out.println("Podaj adres: ");
            getUserAddress = in.nextLine();
            if(getUserAddress.length()==0){
                flag = true;
                System.out.println("Podany ciag znakow jest za krotki!\nWprowadz adres jeszcze raz.");
            }
        } while (flag);

        do {
            flag = false;
            System.out.println("Podaj fundusze: ");
            try {
                getUserFunds = in.nextDouble();
            }catch (InputMismatchException e){
                System.out.println("Nie wprowadzono liczby!\nWprowadz fundusze jeszcze raz.");
                flag = true;
                in.nextLine();
            }
            if(getUserFunds < 0 && !flag){
                flag = true;
                System.out.println("Warosc funduszy nie moze byc mniejsza od 0!\nWprowadz je jeszcze raz.");
            }
        } while (flag);

        getUserId = nextFreeId;
        BankUser user = new BankUser(getUserId, new Person(getUserName, getUserSurname, getUserPesel, getUserAddress),getUserFunds);
        usersList.add(user);
    }

    private void deleteUser(){
        int indexToDelete = 0;
        boolean flag;

        for (BankUser i:usersList)
            System.out.println(i);


        do {
            flag = false;
            System.out.println("Podaj ID uzytkownika, ktorego chcesz usunac z bazy: ");
            try {
                indexToDelete = findUserInDatabase(in.nextInt());
            }catch (InputMismatchException e) {
                System.out.println("Wprowadzona wartosc nie jest liczba!\nWprowadz ja jeszcze raz.");
                flag = true;
                in.nextLine();
            }

            if(indexToDelete == -1 && !flag){
                System.out.println("Nie ma takiego uzytkownika w bazie!\nWprowadz Id jeszcze raz.");
                flag = true;
            }

            if(!flag) {
                usersList.remove(indexToDelete);
            }

        }while(flag);
    }

    private boolean confirm() {
        Scanner in = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Czy na pewno chcesz wykonac dana czynnosc? T/N");
            choice = in.nextLine();
        } while (!choice.equals("T") && !choice.equals("t") && !choice.equals("N") && !choice.equals("n"));

        if (choice.equals("T") || choice.equals("t"))
            return true;
        else
            return false;
    }

    private boolean ifStringIsRight(String stringToCheck) {
        if (Pattern.matches("[a-zA-Z]+", stringToCheck))
            return true;
        else
            return false;
    }

    private boolean ifPeselIsRight(String peselToCheck) {
        if (Pattern.matches("[0-9]+", peselToCheck) && peselToCheck.length() == 11)
            return true;
        else
            return false;
    }

    private boolean isTheCriterionToSearchCorrect(String stringToCheck){
        if(stringToCheck.equalsIgnoreCase("imie") || stringToCheck.equalsIgnoreCase("nazwisko") || stringToCheck.equalsIgnoreCase("adres") || stringToCheck.equalsIgnoreCase("pesel") || stringToCheck.equalsIgnoreCase("id"))
            return true;
        else
            return false;
    }


    private void saveDatabase( ArrayList<BankUser> userArrayList){
        Gson gson = new Gson();

        try(FileWriter save = new FileWriter("C:\\Users\\Bartek\\Desktop\\Java\\PO2\\SystemBankowy\\database.json")){
            gson.toJson(userArrayList, save);
        }catch(IOException e){
            System.out.println("Cos nie tak z zapisem bazy");
            e.printStackTrace();
        }
    }

    private ArrayList<BankUser> loadDatabase(){
        ArrayList<BankUser> users = new ArrayList<>();
        Gson gson = new Gson();
        try(Reader load = new FileReader("C:\\Users\\Bartek\\Desktop\\Java\\PO2\\SystemBankowy\\database.json")){
            users = gson.fromJson(load, new TypeToken<ArrayList<BankUser>>(){}.getType());
        }catch(IOException e){
            System.out.println("Cos nie tak z odczytem bazy");
            e.printStackTrace();
        }

        return users;
    }

    private int findUserInDatabase(int id){
        int indexToReturn = -1;
        for (BankUser i:usersList) {
            if (i.getUserId() == id) {
                indexToReturn = usersList.indexOf(i);
                break;
            }
        }
        return indexToReturn;
    }

    private double roundTwoDecimal(double doubleToRound){
        DecimalFormat df = new DecimalFormat(",##");
        return Double.valueOf(df.format(doubleToRound));
    }
}
