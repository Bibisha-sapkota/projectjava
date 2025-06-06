package coursework;



public class Name {
    private String firstName;
    private String lastName;

  
    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    
    public String getFirstName() {
        return firstName;
    }

  
    public String getLastName() {
        return lastName;
    }

    
    public String getFullName() {
        return firstName + " " + lastName;
    }

 
    public String getInitials() {
        return (firstName.charAt(0) + "." + lastName.charAt(0) + ".").toUpperCase();
    }
}

