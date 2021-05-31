import java.util.HashMap;

public class BankAccount{

    BankAccount(){

    }
    public double getAmountInAccount(HashMap<Integer,Double> account,int number) {
        return account.get(number);
    }

    public void setAmountInAccount(HashMap<Integer,Double> account,int number,double dep) {
        account.compute(number,(k,v)-> {
            assert v != null;
            return v +dep;
        });


    }


}


