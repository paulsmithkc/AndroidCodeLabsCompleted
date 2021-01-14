package edu.ranken.paul_smith;

public class Employee extends Person {
    protected String ssn;
    protected Double salary;

//    public Employee() {
//    }

    public Employee(int id, String name, String ssn, Double salary) {
        super(id, name);
        this.ssn = ssn;
        this.salary = salary;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
