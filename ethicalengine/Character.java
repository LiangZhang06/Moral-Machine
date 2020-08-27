package ethicalengine;

/**
 * An Abstract class from which all character types inherit.
 * Parent class to Person.java and Animal.java.
 * 
 * @author Nuvi Anggaresti | nanggaresti | 830683
 */
public abstract class Character {
    /************************** List of enumerations **************************/
    public enum Gender {FEMALE, MALE, QUEER, UNKNOWN}
    public enum BodyType {AVERAGE, ATHLETIC, OVERWEIGHT, UNSPECIFIED}
    /************************** End of enumerations **************************/
    
    
    /************************** List of attributes **************************/
    protected int age;
    protected Gender gender;
    protected BodyType bodytype;
    /************************** End of attributes **************************/

    
    /************************** List of constants **************************/
    final private int DEFAULT_AGE = 0;
    /************************** End of constants **************************/
    
    
    /************************** Constructor declaration(s) **************************/
    /**
     * A constructor for class Character
     * takes no parameter.
     */
    public Character(){
        this.age = DEFAULT_AGE;
        this.gender = Gender.UNKNOWN;
        this.bodytype = BodyType.UNSPECIFIED;
    }
    
    /**
     * Another constructor for class Character.
     * 
     * @param age       the age (int) of the character
     * @param gender    the gender (enum Gender) of the character
     * @param bodytype  the body type (enum BodyType) of the character
     */
    public Character(int age, Gender gender, BodyType bodytype){
        try{
            if (!(age >= 0)){
                throw new Exception("Age value is invalid. Revert to default value = 0.");
            }else{
                this.age = age;
            }
        }catch(Exception e){
            this.age = DEFAULT_AGE;
        }finally{
            this.gender = gender;
            this.bodytype = bodytype;
        }
    }
    /**
     * Another constructor for class Character
     * to create a copy of another Character-type object.
     * 
     * @param c     another Character-type object
     */
    public Character(Character c){
        age = c.age;
        gender = c.gender;
        bodytype = c.bodytype;
    }
    /************************** End of constructor declaration(s) **************************/
    
    
    /************************** Function declaration(s) **************************/
    /**
     * Gets the age of this Character
     * takes no parameter.
     * 
     * @return this Character's age
     */
    public int getAge(){
        return this.age;
    }
    /**
     * Gets the gender of this Character
     * takes no parameter.
     * 
     * @return this Character's gender
     */
    public Gender getGender(){
        return this.gender;
    }
    
    /**
     * Gets the body type of this Character
     * takes no parameter.
     * 
     * @return this Character's body type
     */
    public BodyType getBodyType(){
        return this.bodytype;
    }
    
    /**
     * Modifies this Character's age.
     * 
     * @param age 
     */
    public void setAge(int age){
        this.age = age;
    }
    
    /**
     * Modifies this Character's gender.
     * 
     * @param gender 
     */
    public void setGender(Gender gender){
        this.gender = gender;
    }
    
    /**
     * Modifies this Character's body type.
     * 
     * @param bodytype 
     */
    public void setBodyType(BodyType bodytype){
        this.bodytype = bodytype;
    }
    
    // Function placeholders for child classes
    // from this point onwards
    
    /**
     * Method placeholder for class Person;
     * identifies whether Character 'isYou'.
     * 
     * @return isYou (boolean)
     */
    public boolean isYou(){
        // Method placeholder (Person)
        boolean placeholder = true;
        return placeholder;
    }
    
    /**
     * Method placeholder for class Person;
     * modifies Character's 'isYou' attribute.
     * 
     * @param isYou (boolean)
     */
    public void setAsYou(boolean isYou){
        // Method placeholder (Person)
    }
    
    /**
     * Method placeholder for class Person;
     * identifies whether Character 'isPregnant'.
     * 
     * @return isPregnant (boolean)
     */
    public boolean isPregnant(){
        // Method placeholder (Person)
        boolean placeholder = false;
        return placeholder;
    }
    
    /**
     * Method placeholder for class Person;
     * gets the Character's age category.
     * 
     * @return this Person's AgeCategory (enum)
     */
    public Person.AgeCategory getAgeCategory(){
        // Method placeholder (Person)
        Person.AgeCategory placeholder = Person.AgeCategory.ADULT;
        return placeholder;
    }
    
    /**
     * Method placeholder for class Person;
     * gets the Character's profession;
     * 
     * @return this Person's profession (enum)
     */
    public Person.Profession getProfession(){
        // Method placeholder (Person)
        Person.Profession placeholder = Person.Profession.STUDENT;
        return placeholder;
    }
    
    /**
     * Method placeholder for class Animal;
     * gets the Character's species.
     * 
     * @return this Animal's species (String)
     */
    public String getSpecies(){
        // Method placeholder (Animal)
        String placeholder = "";
        return placeholder;
    }
    
    /**
     * Method placeholder for class Animal;
     * identifies whether Character 'isPet'.
     * 
     * @return isPet (boolean)
     */
    public boolean isPet(){
        // Method placeholder (Animal)
        return false;
    }
    /************************** End of function declaration(s) **************************/
}