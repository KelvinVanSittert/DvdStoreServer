/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dvdstoreserver;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Jaco
 */
public class Message implements Serializable {
    private static final long serialVersionUID = -5399605122490343339L;

    private ArrayList<Customer> Customers;
    private ArrayList<DVD> DVDs;
    private ArrayList<Rental> Rentals;
    private Customer Customer;
    private DVD Dvd;
    private Rental Rental;
    private Action ActionToPerform;
    private Target TargetToPerformActionTo;
    private String Statement;
    
    public enum Action {
        Update,
        Insert,
        Get
    }
    
    public enum Target {
        Customer,
        DVD,
        Rental
    }
    
    
    public <E> Message(ArrayList<E> list, Action action, Target target, String statement ){
        
        if(target == Target.Customer)
           this.Customers = (ArrayList<Customer>)list;
        if(target == Target.DVD)
           this.DVDs = (ArrayList<DVD>)list; 
        if(target == Target.Rental)
           this.Rentals = (ArrayList<Rental>)list; 

        this.ActionToPerform = action;
        this.TargetToPerformActionTo = target;
        this.Statement = statement;
    }
    
    public Message(Customer customer, Action action, Target target, String statement ){
        this.Customer = customer; 
        this.ActionToPerform = action;
        this.TargetToPerformActionTo = target;
        this.Statement = statement;
    }
    
    public Message(DVD dvd, Action action, Target target, String statement ){
        this.Dvd = dvd; 
        this.ActionToPerform = action;
        this.TargetToPerformActionTo = target;
        this.Statement = statement;
    }
    
    public Message(Rental rental, Action action, Target target, String statement ){
        this.Rental = rental; 
        this.ActionToPerform = action;
        this.TargetToPerformActionTo = target;
        this.Statement = statement;
    }    

    public ArrayList<Customer> getCustomers() { return Customers;};
    public ArrayList<DVD> getDVDs() { return DVDs;};
    public ArrayList<Rental> getRentals() { return Rentals;};
    public Customer getCustomer() { return Customer; }
    public DVD getDvd() { return Dvd; }
    public Rental getRental() { return Rental; }
    public Action getAction() { return ActionToPerform; }
    public Target getTarget() { return TargetToPerformActionTo; }
    public String getStatement() { return Statement; }
}
