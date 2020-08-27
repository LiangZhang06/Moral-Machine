import ethicalengine.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.io.*;
import java.math.RoundingMode;
import java.nio.file.*;
import java.text.DecimalFormat;

/**
 * Inspects the algorithm with the purpose of revealing inherent biases that may be 
 * (unintentionally) built in.
 * 
 * @author Nuvi Anggaresti | nanggaresti | 830683
 */
public class Audit extends Exception{
    /************************** List of attributes **************************/
    private String auditType;
    // Storing the number of times a relevant characteristic occurs in inspected Scenario(s)
    private HashMap<String, Double> charTotal;
    // Storing the number of times a relevant characteristic occur in surviving Characters
    private HashMap<String, Double> charSurvived;
    private Scenario[] scenarios;
    private double ageSum;
    private int run;
    private boolean auditSimulated; // to check if simulation has been run
    /************************** End of attributes **************************/
    
    
    /************************** List of constants **************************/
    final private double ZERO_DBL = 0.0;
    final private int ZERO = 0;
    final private double ONE_DBL = 1.0;
    /************************** End of constants **************************/
    
    
    /************************** Constructor declaration(s) **************************/
    /**
     * A constructor for class Audit
     * takes no parameter.
     * 
     */
    public Audit(){
        this.auditType = "Unspecified";
        this.charTotal = new HashMap<>();
        this.charSurvived = new HashMap<>();
        this.ageSum = ZERO;
        this.auditSimulated = false;
    }
    
    /**
     * Another constructor for class Audit.
     * 
     * @param scenarios     a list of specified Scenarios (Scenario[])
     */
    public Audit(Scenario[] scenarios){
        this.setScenario(scenarios);
        this.auditType = "Unspecified";
        this.charTotal = new HashMap<>();
        this.charSurvived = new HashMap<>();
        this.ageSum = ZERO;
        this.auditSimulated = false;
    }
    /************************** End of constructor declaration(s) **************************/
    
    
    /************************** Function declaration(s) *************************/
    /**
     * A function to run the Audit once
     * by calling this Audit's implementDecision() function.
     * 
     */
    public void run(){
        this.auditSimulated = true;
        setAuditType("Unspecified");
        this.implementDecision();
    }
    
    /**
     * A function to run the Audit for a specified number of time
     * by calling this Audit's implementDecision() function.
     * 
     * @param runs      the number of times the Audit must be run (int)
     */
    public void run(int runs){
        this.run = runs;
        this.auditSimulated = true;
        setAuditType("Unspecified");
        this.implementDecision();
    }
    
    /**
     * Sets this Audit's type (String): typically User, Algorithm, or Unspecified.
     * 
     * @param name      this Audit's type (String)
     */
    public void setAuditType(String name){
        this.auditType = name;
    }
    
    /**
     * Updates this Audit's auditSimulated status (whether this Audit has been run).
     * 
     * @param auditSimulated (a boolean)
     */
    public void setAuditSimulation(boolean auditSimulated){
        this.auditSimulated = auditSimulated;
    }
    
    /**
     * Gets this Audit's type.
     * 
     * @return this Audit's type
     */
    public String getAuditType(){
        return this.auditType;
    }
    
    /**
     * Specifies the Scenarios that must be inspected by this Audit.
     * 
     * @param scenarios     an array of Scenarios (Scenario[]) that is to be audited.
     */
    public void setScenario(Scenario[] scenarios){
        this.run += scenarios.length;
        this.scenarios = scenarios.clone();
    }
    
    /**
     * Filling the HashMaps with associated values based on the Decision made either by a user
     * or the engine based on a specific Scenario.
     * 
     * @param decision      Decision made by engine or User in regards of a Scenario
     * @param scenario      the Scenario which outcome was decided for
     */
    public void insertDecision(EthicalEngine.Decision decision, Scenario scenario){
        int ZERO = 0;
        
        ArrayList<ethicalengine.Character> passengerList = new ArrayList<>();
        ArrayList<ethicalengine.Character> pedestrianList = new ArrayList<>();
        
        // Copying the Passengers and Pedestrians of the current inspected Scenario into ArrayLists
        for (int i=ZERO; i < scenario.getPassengerCount(); i++){
            passengerList.add(scenario.getPassengers()[i]);
        }
        for (int i=ZERO; i < scenario.getPedestrianCount(); i++){
            pedestrianList.add(scenario.getPedestrians()[i]);
        }
        
        ArrayList<ethicalengine.Character> characters = new ArrayList<>();
        characters.addAll(passengerList);
        characters.addAll(pedestrianList);
        populateMap(this.charTotal, characters); // Populating HashMap charTotal with all characters
        countRedGreen(scenario.isLegalCrossing(), this.charTotal, characters);
        
        if (decision == EthicalEngine.Decision.PASSENGERS){
            ArrayList<ethicalengine.Character> survivors;
            survivors = new ArrayList<>();
            survivors.addAll(passengerList);
            this.ageSum += sumAge(survivors);

            populateMap(this.charSurvived, survivors);
            countRedGreen(scenario.isLegalCrossing(), this.charSurvived, survivors);


        }else{ // decision == EthicalEngine.Decision.PEDESTRIANS
            ArrayList<ethicalengine.Character> survivors;
            survivors = new ArrayList<>();
            survivors.addAll(pedestrianList);
            this.ageSum += sumAge(survivors);

            populateMap(this.charSurvived, survivors);
            countRedGreen(scenario.isLegalCrossing(), this.charSurvived, survivors);
        }

        this.charTotal.entrySet().forEach((character) -> {
            boolean inSurvivor = false;
            for (HashMap.Entry<String, Double> survivor : this.charSurvived.entrySet()){
                if (character.getKey().equals(survivor.getKey())){
                    inSurvivor = true;
                }
            }
            if (!inSurvivor) {
                this.charSurvived.put(character.getKey(), ZERO_DBL);
            }
        });
    }
    
    /**
     * A function to generate n random Scenarios.
     * Followed by requesting the engine to make a Decision regarding the Scenarios.
     * Followed by filling the HashMaps with relevant values based on the above Decision.
     * 
     */
    public void implementDecision(){
        ScenarioGenerator generateScenario = new ScenarioGenerator();
        
        this.scenarios = new Scenario[this.run]; // creates a new Scenario[] based on number of runs
        for (int i=ZERO; i < this.run; i++){ // for each run:
            Scenario scenario = generateScenario.generate(); // generates a new Scenario
            this.scenarios[i] = scenario;
            EthicalEngine.Decision decision = EthicalEngine.decide(scenario); // engine Decides
            insertDecision(decision, scenario); // fills HashMap
        }
    }
    
    /**
     * A function to request the engine to Decide on the Scenario(s) specified in the Audit.
     * Followed by filling the HashMaps with relevant values based on the Decision(s).
     * 
     */
    public void decideScenarios(){
        int ZERO = 0;
        
        this.setAuditSimulation(true);
        for (int i=ZERO; i < this.scenarios.length; i++){ // for each Scenario:
            // decides outcome
            EthicalEngine.Decision decision = EthicalEngine.decide(this.scenarios[i]);
            insertDecision(decision, this.scenarios[i]); // fils HashMaps
        }
    }
    
    /**
     * Displays the Decision inspection results as a readable, human-friendly String.
     * 
     * @return a String detailing the ratio of Saved : Present for each characteristic and avg age
     */
    @Override
    public String toString(){
        String toReturn = new String();
        
        if (auditSimulated){
            double averageAge;
            if (this.charSurvived.containsKey("person")){
                averageAge = this.ageSum / this.charSurvived.get("person");
            }else{
                averageAge = ZERO;
            }
            DecimalFormat df = new DecimalFormat("0.0");
            df.setRoundingMode(RoundingMode.FLOOR);
            
            String border = "======================================";
            String title = "# " + this.auditType + " Audit";
            String savedAfter = "- % SAVED AFTER " + Integer.toString(this.run) + " RUNS";
            toReturn = toReturn.concat(border + "\n" + title + "\n" + border + "\n" + savedAfter);
            
            HashMap<String, Double> percentageSaved = new HashMap<>();
            for(HashMap.Entry<String, Double> character : this.charTotal.entrySet()){
                percentageSaved.put(character.getKey(), 
                        this.charSurvived.get(character.getKey()) / character.getValue() );
            }
            
            LinkedHashMap<String, Double> sortedPercentage = new LinkedHashMap<>();
            ArrayList<Double> percentageList = new ArrayList<>();
            for (HashMap.Entry<String, Double> entry : percentageSaved.entrySet()){
                percentageList.add(entry.getValue());
            }
            Collections.sort(percentageList);
            Collections.reverse(percentageList);
            for (double percentage : percentageList){
                for (HashMap.Entry<String, Double> entry : percentageSaved.entrySet()){
                    if (entry.getValue() == percentage){
                        sortedPercentage.put(entry.getKey(), percentage);
                        percentageSaved.remove(entry);
                    }
                }
            }
            
            for (HashMap.Entry<String, Double> survivor : sortedPercentage.entrySet()){
                double value = survivor.getValue();
                String result = df.format(value);
                String entry = survivor.getKey() + ": " + result;
                toReturn = toReturn.concat("\n" + entry);
            }
            
            String endOfList = "\n--";
            double avgAgeValue = averageAge;
            String printAverageAge = df.format(avgAgeValue);
            String ending = endOfList + "\naverage age: " + printAverageAge;
            toReturn = toReturn.concat(ending);
            
        }else{
            toReturn = "no audit available";
        }
        
        return toReturn;
    }
    
    /**
     * Prints the result of inspection.
     */
    public void printStatistics(){
        System.out.print(this.toString());
    }
    
    /**
     * Sums the total age of all survivors of class Person.
     * 
     * @param characterList
     * @return total age of all Person survivors (int)
     */
    private double sumAge(ArrayList<ethicalengine.Character> characterList){
        double ageSum = ZERO_DBL;
        for (ethicalengine.Character character : characterList){
            if (character instanceof Person){
                ageSum += character.getAge();
            }
        }
        
        return ageSum;
    }
    
    /**
     * Populates the HashMap with relevant values for each characteristic present 
     * in the Scenario(s) inspected.
     * 
     * @param map       HashMap to be filled (total or survivors)
     * @param charList  a List of Characters whose characteristics are to be parsed (ArrayList<>)
     */
    private void populateMap(HashMap<String, Double> map, 
            ArrayList<ethicalengine.Character> charList){
        for(ethicalengine.Character character : charList){
            if (character instanceof Person){
                // Gets all characteristics
                String ageCategory = character.getAgeCategory().name().toLowerCase();
                String gender = character.getGender().name().toLowerCase();
                String bodyType = character.getBodyType().name().toLowerCase();
                String profession = character.getProfession().name().toLowerCase();
                       
                boolean personFound = false;
                boolean ageCategoryFound = false;
                boolean genderFound = false;
                boolean bodyTypeFound = false;
                boolean professionFound = false;
                boolean pregnantFound = false;
                boolean isYou = false;
                
                for (HashMap.Entry<String, Double> entry : map.entrySet()){
                    if(entry.getKey().equals("person")){
                        double currentPersonCount = map.get("person");
                        map.replace("person", currentPersonCount += ONE_DBL);
                        personFound = true;
                    }
                    if(entry.getKey().equals(ageCategory)){
                        double currentAgeCatCount = map.get(ageCategory);
                        map.replace(ageCategory, currentAgeCatCount += ONE_DBL);
                        ageCategoryFound = true;
                    }
                    if(entry.getKey().equals(gender)){
                        double currentGenderCount = map.get(gender);
                        map.replace(gender, currentGenderCount += ONE_DBL);
                        genderFound = true;
                    }
                    if(entry.getKey().equals(bodyType)){
                        double currentBodyType = map.get(bodyType);
                        map.replace(bodyType, currentBodyType += ONE_DBL);
                        bodyTypeFound = true;
                    }
                    if(entry.getKey().equals(profession)){
                        if (profession.equalsIgnoreCase("none")){
                            // do nothing
                        }else{
                            double currentProfCount = map.get(profession);
                            map.replace(profession, currentProfCount += ONE_DBL);
                            professionFound = true;
                        }
                        
                    }
                    if(entry.getKey().equals("pregnant")){
                        if(character.isPregnant()){
                            double currentPregnantCount = map.get("pregnant");
                            map.replace("pregnant", currentPregnantCount+=ONE_DBL);
                            pregnantFound = true;
                        }else{
                            // do nothing
                        }
                    }
                    if(entry.getKey().equals("you")){
                        if(character.isYou()){
                            double currentYouCount = map.get("you");
                            map.replace("you", currentYouCount+=ONE_DBL);
                            isYou = true;
                        }else{
                            // do nothing
                        }
                    }
                }
                if (!personFound){
                    map.put("person", ONE_DBL);
                }
                if (!ageCategoryFound){
                    map.put(ageCategory, ONE_DBL);
                }
                if (!genderFound){
                    map.put(gender, ONE_DBL);
                }
                if (!bodyTypeFound){
                    map.put(bodyType, ONE_DBL);
                }
                if (!professionFound){
                    if (profession.equalsIgnoreCase("none")){
                        // do nothing
                    }else{
                        map.put(profession, ONE_DBL);
                    }
                }
                if (!pregnantFound && character.isPregnant()){
                    map.put("pregnant", ONE_DBL);
                }
                if (!isYou && character.isYou()){
                    map.put("you", ONE_DBL);
                }
                
            }else{ // character instanceof Animal
                String species = character.getSpecies();
                
                boolean animalFound = false;
                boolean speciesFound = false;
                boolean petFound = false;
                      
                for (HashMap.Entry<String, Double> entry : map.entrySet()){
                    if (entry.getKey().equals("animal")){
                        double currentAnimalCount = map.get("animal");
                        map.replace("animal", currentAnimalCount += ONE_DBL);
                        animalFound = true;
                    }
                    if(entry.getKey().equals(species)){
                        double currentSpeciesCount = map.get(species);
                        map.replace(species, currentSpeciesCount += ONE_DBL);
                        speciesFound = true;
                    }
                    if(entry.getKey().equals("pet")){
                        if(character.isPet()){
                            double currentPetCount = map.get("pet");
                            map.replace("pet", currentPetCount+=ONE_DBL);
                            petFound = true;
                        }else{
                            // do nothing
                        }
                    }
                }
                        
                if (!animalFound){
                    map.put("animal", ONE_DBL);
                }
                if (!speciesFound){
                    map.put(species, ONE_DBL);
                }
                if (!petFound && character.isPet()){
                    map.put("pet", ONE_DBL);
                }
            }
       }        
    }
    
    /**
     * Populates the HashMap with 'Red' and 'Green' counts.
     * Red = number of survived characters in Red / number of total characters in Red.
     * 
     * @param isGreen       checks whether light isGreen or Red
     * @param map           HashMap to be filled
     * @param character     list of characters (ArrayList<Character>)
     */
    private void countRedGreen(boolean isGreen, HashMap<String, Double> map, 
            ArrayList<ethicalengine.Character> character){
        double numCharacters = character.size();
        boolean found = false;
        if (isGreen){
            for (HashMap.Entry<String, Double> entry : map.entrySet()){
                if(entry.getKey().equals("green")){
                    double currentRedCount = entry.getValue();
                    map.replace("green", currentRedCount += numCharacters);
                    found = true;
                }
            }
            if (!found){
                map.put("green", numCharacters);
            }
        }else{
            for (HashMap.Entry<String, Double> entry : map.entrySet()){
                if(entry.getKey().equals("red")){
                    double currentRedCount = entry.getValue();
                    map.replace("red", currentRedCount += numCharacters);
                    found = true;
                }
            }
            if (!found){
                map.put("red", numCharacters);
            }
        }
    }
      
    /**
     * Prints audit results to a specified file path.
     * 
     * @param filepath      path to which audit log is to be printed
     * @throws NotDirectoryException    when Directory (file path) does not exist
     * @throws IOException              when other error occurs
     */
    public void printToFile(String filepath){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true));
            bw.write(this.toString());
            //System.out.println(this.toString());
            bw.close();
            System.exit(0);
        }catch (NotDirectoryException e){
            System.out.println("ERROR: could not print results. Target directory does not exist.");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    /************************** End of function declaration(s) **************************/
}