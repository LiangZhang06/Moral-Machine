package ethicalengine;

/**
 * Animal.java represents animals in the scenarios.
 * Might not be present in the scenarios.
 * 
 * @author Nuvi Anggaresti | nanggaresti | 830683
 */
public class Animal extends Character{
    
    /************************** List of attributes **************************/
    private String species;
    private boolean isPet;
    /************************** End of attributes **************************/
    
    
    /************************** Constructor declaration(s) **************************/
    /**
     * A constructor for class Animal.
     * 
     * @param species   this Animal's species, written as String
     */
    public Animal(String species){
        this.species = species;
    }
    
    /**
     * A constructor for class Animal
     * creates a duplicate of another Animal.
     * 
     * @param otherAnimal   another Animal-type object to be duplicated
     */
    public Animal(Animal otherAnimal){
        species = otherAnimal.species;
    }
    
    /**
     * Another constructor for class Animal
     * takes no parameter.
     * 
     * Inherits from parent class Character.java.
     */
    public Animal(){
        super();
    }
    
    /**
     * Another constructor for class Animal,
     * inherits from parent class Character.java.
     * 
     * @param age       this Animal's age (int)
     * @param gender    this Animal's gender (enum Gender)
     * @param bodytype  this Animal's body type (enum BodyType)
     */
    public Animal(int age, Gender gender, BodyType bodytype){
        super(age, gender, bodytype);
    }
    /************************** End of constructor declaration(s) **************************/
    
    
    /************************** Function declaration(s) **************************/
    /**
     * Gets this Animal's species.
     * 
     * @return this Animal's species (String)
     */
    @Override
    public String getSpecies(){
        return this.species;
    }
    
    /**
     * Identifies whether this Animal 'isPet'
     * 
     * @return 'true' or 'false' (boolean)
     */
    public boolean isPet(){
        return this.isPet;
    }
    
    /**
     * Modifies or sets this Animal's species (String).
     * 
     * @param species   (String)
     */
    public void setSpecies(String species){
        this.species = species;
    }
    
    /**
     * Updates this Animal's 'isPet' status.
     * 
     * @param isPet     (boolean)
     */
    public void setPet(boolean isPet){
        this.isPet = isPet;
    }
    
    /**
     * Modifies or sets this Animal's gender.
     * 
     * @param gender    (enum Gender)
     */
    public void setGender(Gender gender){
        this.gender = gender;
    }
    
    /**
     * Displays this Animal's species and pet status as a String.
     * 
     * @return a String detailing this Animal's species and pet status
     */
    @Override
    public String toString(){
        if(this.isPet()){
            return species + " is pet";
        } else{
            return species;
        }
    }
    /************************** End of function declaration(s) **************************/
}