package ethicalengine;

/**
 * Person.java represents humans in the scenarios.
 * Might be 'you', i.e. the User.
 * Must be present at least once as a Passenger.
 * 
 * @author Nuvi Anggaresti | nanggaresti | 830683
 */
public class Person extends Character{
    /************************** List of enumerations **************************/
    public enum Profession {
        DOCTOR, CEO, CRIMINAL, HOMELESS, STUDENT, ACCOUNTANT, LAWYER, CHEF,
        UNEMPLOYED, UNKNOWN, NONE
    }
    public enum AgeCategory {BABY, CHILD, ADULT, SENIOR}
    /************************** End of enumerations **************************/
    
    
    /************************** List of attributes **************************/
    private Profession profession;
    private boolean isPregnant;
    private AgeCategory ageCategory;
    private boolean isYou;
    /************************** End of attributes **************************/
    
    
    /************************** Constructor declaration(s) **************************/
    /**
     * A constructor for class Person.
     * 
     * @param age           this Person's age (int)
     * @param profession    this Person's profession (enum Profession)
     * @param gender        this Person's gender (enum Gender)
     * @param bodytype      this Person's body type (enum BodyType)
     * @param isPregnant    this Person's pregnancy status (boolean)
     */
    public Person(int age, Profession profession, Gender gender, BodyType bodytype, 
            boolean isPregnant){
        super(age, gender, bodytype);
        this.ageCategory = getAgeCategory();
        
        if (this.ageCategory == AgeCategory.ADULT){
            this.profession = profession;
        } else{
            this.profession = Profession.NONE;
        }
        
        if (this.gender == Gender.FEMALE){
            this.isPregnant = isPregnant;
        } else{
            this.isPregnant = false;
        }
    }
    
    /**
     * Another constructor for class Person
     * makes a copy of another Person.
     * 
     * @param otherPerson   another Person-type object that is to be duplicated
     */
    public Person(Person otherPerson){
        super(otherPerson);
        profession = otherPerson.profession;
        isPregnant = otherPerson.isPregnant;
    }
    
    /**
     * Another constructor for class Person
     * inherits from parent class Character.java.
     */
    public Person(){
        super();
    }
    
    /**
     * Another constructor for class Person
     * inherits from parent class Character.java.
     * 
     * @param age
     * @param gender
     * @param bodytype 
     */
    public Person(int age, Gender gender, BodyType bodytype){
        super(age, gender, bodytype);
    }
    /************************** End of constructor declaration(s) **************************/
    
    
    /************************** Function declaration(s) **************************/
    /**
     * Gets the Person's age category (see enum AgeCategory)
     * takes no parameter.
     * 
     * @return this Person's age category
     */
    @Override
    public AgeCategory getAgeCategory(){
        int age = this.getAge();
        
        if (inclusiveBetween(age, 0, 4)){
            return AgeCategory.BABY;
        }else if (inclusiveBetween(age, 5, 16)){
            return AgeCategory.CHILD;
        }else if(inclusiveBetween(age, 17,68)){
            return AgeCategory.ADULT;
        }else{
            return AgeCategory.SENIOR;
        }
    }
    
    /**
     * Gets this Person's profession (see enum Profession)
     * takes no parameter.
     * 
     * @return this Person's profession
     */
    @Override
    public Profession getProfession(){
        return this.profession;
    }
    
    /**
     * Identifies whether this Person 'isPregnant'
     * takes no parameter.
     * 
     * @return this Person's 'isPregnant' attribute (boolean)
     */
    @Override
    public boolean isPregnant(){
        return this.isPregnant;
    }
    
    /**
     * Modifies this Person's pregnancy status.
     * 
     * @param pregnant  whether this Person 'isPregnant' (boolean)
     */
    public void setPregnant(boolean pregnant){
        if (this.getGender() == Gender.FEMALE){
            this.isPregnant = pregnant;
        } else{
            this.isPregnant = false;
        }
    }
    
    /**
     * Identifies whether this Person 'isYou'.
     * 
     * @return this Person's 'isYou' attribute (boolean)
     */
    @Override
    public boolean isYou(){
        return this.isYou;
    }
    
    /**
     * Modifies this Person's 'isYou' status.
     * 
     * @param isYou     this Person's 'isYou' status (boolean)
     */
    @Override
    public void setAsYou(boolean isYou){
        this.isYou = isYou;
    }
    
    /**
     * Builds a String to display this Person's characteristics.
     * 
     * @return this Person's characteristics detailed as a String
     */
    @Override
    public String toString(){
        String isYou = new String();
        
        String ageCategory = this.getAgeCategory().name().toLowerCase();
        
        String gender = this.getGender().name().toLowerCase();
        
        String toStringProfession = this.toStringProfession();
        String buildString = "";
        if (this.isYou()){
            isYou = "you ";
            buildString = buildString.concat(isYou);
            buildString = buildString.concat(toStringProfession);
        }else{
            buildString = buildString.concat(toStringProfession);
        }
        
        return buildString;
    }
    
    /**
     * Gets this Person's profession, body type, and age category
     * and displays it as a String.
     * 
     * @return a String detailing this Person's body type, age category, and profession
     */
    private String toStringProfession(){
        String toReturn = "";
        
        String bodyType = this.getBodyType().name().toLowerCase();
        String ageCategory = this.getAgeCategory().name().toLowerCase();
        String profession = this.getProfession().name().toLowerCase();
        
        toReturn = toReturn.concat(bodyType + " ");
        toReturn = toReturn.concat(ageCategory + " ");
        
        String toStringPregnant = this.toStringPregnant();
        if (this.isAdult()){
            toReturn = toReturn.concat(profession + " ");
            toReturn = toReturn.concat(toStringPregnant);
        } else{
            toReturn = toReturn.concat(toStringPregnant);
        }
        
        return toReturn;
    }
    /**
     * Gets this Person's pregnancy status and gender
     * and displays it as a String.
     * 
     * @return a String detailing this Person's gender and pregnancy status
     */
    private String toStringPregnant(){
        String toReturn= "";
        
        String gender = this.getGender().name().toLowerCase();
        
        toReturn = toReturn.concat(gender);
        if (this.isPregnant()){
            toReturn = toReturn.concat(" pregnant");
        }else{
            //do nothing
        }
        
        return toReturn;
    }
    
    /**
     * Checks whether a number is inclusively between a lower bound and an upper bound.
     * 
     * @param target    number to be checked
     * @param lower     lower bound
     * @param upper     upper bound
     * @return 'true' or 'false' (boolean)
     */
    private boolean inclusiveBetween(int target, int lower, int upper){
        return (target >= lower && target <= upper);
    }
    
    /**
     * Checks whether this Person is in the 'Adult' age category.
     * 
     * @return 'true' or 'false' (boolean)
     */
    private boolean isAdult(){
        return (this.getAgeCategory() == AgeCategory.ADULT);
    }
    /************************** End of function declaration(s) **************************/
}