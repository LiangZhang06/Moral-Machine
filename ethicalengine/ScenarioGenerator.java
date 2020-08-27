package ethicalengine;
import java.util.Random;
import java.util.ArrayList;

/**
 * Creates a variety of random Scenarios.
 * 
 * @author Nuvi Anggaresti | nanggaresti | 830683
 */
public class ScenarioGenerator {
    
    /************************** List of enumerations **************************/
    public enum Light {RED, GREEN}
    public enum YourLocation {PASSENGER, PEDESTRIAN, ABSENT}
    /************************** End of enumerations **************************/
    
    
    /************************** List of attributes **************************/
    private int passengerCountMin;
    private int passengerCountMax;
    private int pedestrianCountMin;
    private int pedestrianCountMax;
    
    private YourLocation yourLocation;
    
    private Random rand;
    /************************** End of attributes **************************/
    
    
    /************************** List of constants **************************/
    final private int DEFAULT_MIN = 1;
    final private int DEFAULT_MAX = 5;
    final private int GENDER_SIZE = Person.Gender.values().length;
    final private int BODYTYPE_SIZE = Person.BodyType.values().length;
    final private int PROFESSION_SIZE = Person.Profession.values().length;
    /************************** End of constants **************************/
    
    
    /************************** Constructor declaration(s) **************************/
    /**
     * A constructor for ScenarioGenerator
     * takes no parameter.
     */
    public ScenarioGenerator(){
        this.rand = new Random();
        
        this.passengerCountMin = DEFAULT_MIN;
        this.passengerCountMax = DEFAULT_MAX;
        this.pedestrianCountMin = DEFAULT_MIN;
        this.pedestrianCountMax = DEFAULT_MAX;
    }
    
    /**
     * Another constructor for ScenarioGenerator
     * with a specified seed for this Generator's Random attribute.
     * 
     * @param seed      specifies the Random seed of this Generator (long)
     */
    public ScenarioGenerator(long seed){
        this.rand = new Random(seed);
        
        this.passengerCountMin = DEFAULT_MIN;
        this.passengerCountMax = DEFAULT_MAX;
        this.pedestrianCountMin = DEFAULT_MIN;
        this.pedestrianCountMax = DEFAULT_MAX;
    }
    
    /**
     * Another constructor for ScenarioGenerator
     * with a specified seed for this Generator's Random attribute
     * and specified numbers of maximum and minimum passenger and pedestrian counts.
     * 
     * @param seed                          the Random seed of this Generator (long)
     * @param passengerCountMinimum         the minimum number of passengers (int)
     * @param passengerCountMaximum         the maximum number of passengers (int)
     * @param pedestrianCountMinimum        the minimum number of pedestrians (int)
     * @param pedestrianCountMaximum        the maximum number of pedestrians (int)
     */
    public ScenarioGenerator(long seed, int passengerCountMinimum, int passengerCountMaximum,
            int pedestrianCountMinimum, int pedestrianCountMaximum){
        this.rand = new Random(seed);
        
        this.passengerCountMin = passengerCountMinimum;
        this.passengerCountMax = passengerCountMaximum;
        this.pedestrianCountMin = pedestrianCountMinimum;
        this.pedestrianCountMax = pedestrianCountMaximum;
    }
    /************************** End of constructor declaration(s) **************************/
    
    
    /************************** Function declaration(s) **************************/
    /**
     * Sets the minimum passenger count.
     * 
     * @param min       minimum passenger count (int)
     */
    public void setPassengerCountMin(int min){
        this.passengerCountMin = min;
    }
    
    /**
     * Sets the maximum passenger count.
     * 
     * @param max       maximum passenger count (int)
     */
    public void setPassengerCountMax(int max){
        this.passengerCountMax = max;
    }
    
    /**
     * Sets the minimum pedestrian count.
     * 
     * @param min       minimum pedestrian count (int)
     */
    public void setPedestrianCountMin(int min){
        this.pedestrianCountMin = min;
    }
    
    /**
     * Sets the maximum pedestrian count.
     * 
     * @param max       maximum pedestrian count (int)
     */
    public void setPedestrianCountMax(int max){
        this.pedestrianCountMax = max;
    }
    
    /**
     * Creates a new, randomised Person.
     * 
     * @return a new randomised Person
     */
    public Person getRandomPerson(){
        int MAX_AGE = 90;
        
        Random rand = this.rand;
        
        int age = rand.nextInt(MAX_AGE); // randomises Person's age
        // randomises Person's profession, validity is guarded in the Person constructor
        Person.Profession profession = Person.Profession.values()[rand.nextInt(PROFESSION_SIZE)];
        // randomises Person's gender
        Person.Gender gender = Person.Gender.values()[rand.nextInt(GENDER_SIZE)];
        // randomises Person's body type
        Person.BodyType bodytype = Person.BodyType.values()[rand.nextInt(BODYTYPE_SIZE)];
        // randomises Person's pregnancy status, validity is guarded in the Person constructor
        boolean isPregnant = rand.nextBoolean();
        
        // Creates a new Person based on random characteristics above
        Person person = new Person(age, profession, gender, bodytype, isPregnant);
        
        return person;
    }
    
    /**
     * Creates a new, randomised Animal.
     * 
     * @return a new random Animal
     */
    public Animal getRandomAnimal(){
        int MAX_AGE = 20;
        
        // A list of possible Animal species, more species may be added/removed from the list
        String[] animalList = {"cat", "dog", "bird", "snake", "wombat", "squirrel", "kangaroo"};
        int animalListSize = animalList.length;
        
        Random rand = this.rand;
        
        String species = animalList[rand.nextInt(animalListSize)];
        int age = rand.nextInt(MAX_AGE); // randomising Animal's age
        // randomising Animal's body type
        Animal.BodyType bodytype = Animal.BodyType.values()[rand.nextInt(BODYTYPE_SIZE)];
        // randomising Animal's gender
        Animal.Gender gender = Animal.Gender.values()[rand.nextInt(GENDER_SIZE)];
        boolean isPet = rand.nextBoolean(); // randomising Animal's pet status
        
        // Creates a new Animal based on randomised characteristics above
        Animal animal = new Animal(species);
        animal.setAge(age);
        animal.setGender(gender);
        animal.setBodyType(bodytype);
        animal.setPet(isPet);
        
        return animal;
    }
    
    /**
     * Generates a new Scenario based on randomised Passengers, Pedestrians, and crossing legality
     * 
     * @return a new random Scenario
     */
    public Scenario generate(){
        int ZERO = 0;
        int ONE = 1;
        
        Random rand = this.rand;
        this.yourLocation = YourLocation.ABSENT;
        
        int passengerCount = rand.nextInt(this.passengerCountMax - this.passengerCountMin) 
                + this.passengerCountMin;
        int pedestrianCount = rand.nextInt(this.pedestrianCountMax - this.pedestrianCountMin) 
                + this.pedestrianCountMin;
        
        ArrayList<Character> passengerList = new ArrayList();
        ArrayList<Character> pedestrianList = new ArrayList();
        
        boolean humanPassengerPresent = false;
        for (int i=ZERO; i < passengerCount; i++){
            if (i == passengerCount - ONE && !humanPassengerPresent){
                // adds a human passenger if no human has been added until the last passenger
                Person newPassenger = this.getRandomPerson();
                passengerList.add(newPassenger);
                humanPassengerPresent = true;
                if (this.yourLocation == YourLocation.ABSENT){
                    newPassenger.setAsYou(rand.nextBoolean());
                }else{
                    newPassenger.setAsYou(false);
                }
                
                if (newPassenger.isYou()){
                    this.yourLocation = YourLocation.PASSENGER;
                }
            }else{ // free randomisation
                Character newPassenger = randomiseCharacter(passengerList, rand);
                if(newPassenger.isYou()){
                    this.yourLocation = YourLocation.PASSENGER;
                }
            }
        }
        
        for (int j=ZERO; j < pedestrianCount; j++){
            // free character randomisation for pedestrians
            Character newPedestrian = randomiseCharacter(pedestrianList, rand);
            if(newPedestrian.isYou()){
                this.yourLocation = YourLocation.PEDESTRIAN;
            }
        }
        
        // converts ArrayList<Character> to Character[]
        Character[] passengerArr = new Character[passengerList.size()];
        passengerList.toArray(passengerArr);
        Character[] pedestrianArr = new Character[pedestrianList.size()];
        pedestrianList.toArray(pedestrianArr);       
        
        boolean isLegalCrossing = rand.nextBoolean(); // randomises crossing legality
        
        // Creates a new Scenario based on randomised Passengers, Pedestrians, and Legality
        Scenario scenario = new Scenario(passengerArr, pedestrianArr, isLegalCrossing);
        
        return scenario;
    }
    
    public Character randomiseCharacter(ArrayList<Character> list, Random rand){
        int ZERO = 0;
        int TWO = 2;
        int MAX_RANDOMISE = 100;
        
        int randomiser = ZERO; // To choose randomly between animal or person
        randomiser = rand.nextInt(MAX_RANDOMISE);
        randomiser = randomiser % TWO; // Check if randomiser is even or odd
        
        if (randomiser == ZERO){ // if randomiser is even, new character is an animal
            Animal newAnimal = this.getRandomAnimal();
            list.add(newAnimal);
            return newAnimal;
        }else{// if randomiser is odd, new character is a person
            Person newPerson = this.getRandomPerson();
            if (this.yourLocation == YourLocation.ABSENT){
                newPerson.setAsYou(rand.nextBoolean());
            }else{
                newPerson.setAsYou(false);
            }
            list.add(newPerson);
            return newPerson;
        }
    }
    /************************** End of function declaration(s) **************************/
}