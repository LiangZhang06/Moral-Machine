import ethicalengine.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.util.Scanner;

/**
 * Contains the main function and coordinates the program flow.
 * Contains a static method decide(Scenario scenario) which lets the machine Decides on a Scenario.
 * 
 * @author Nuvi Anggaresti | nanggaresti | 830683
 */
public class EthicalEngine {   
    /************************** List of enumerations **************************/
    public enum Decision {PEDESTRIANS, PASSENGERS}
    /************************** End of enumerations **************************/
    
    
    /************************** List of constants **************************/
    // Column indexes for csv parsing
    final private int HEADER_CLASS = 0;
    final private int HEADER_GENDER = 1;
    final private int HEADER_AGE = 2;
    final private int HEADER_BODYTYPE =3;
    final private int HEADER_PROFESSION = 4;
    final private int HEADER_PREGNANT = 5;
    final private int HEADER_ISYOU = 6;
    final private int HEADER_SPECIES = 7;
    final private int HEADER_ISPET = 8;
    final private int HEADER_ROLE = 9;
    final private int DATA_FIELD = 10;
    
    // Default human age if not specified
    final private String DEFAULT_AGE = "21";
    
    final private int ZERO = 0;
    final private int ONE = 1;
    
    // Number of Scenarios displayed each audit round
    final private int SCENARIO_DISPLAYED = 3;
        
    // Lists of possible flags
    final private String[] CONFIG = {"--config", "-c"};
    final private String[] HELP = {"--help", "-h"};
    final private String[] RESULTS = {"--results", "-r"};
    final private String[] INTERACTIVE = {"--interactive", "-i"};
    
    final private String NO_CONFIG_PATH = "No config path specified.";
    final private String DEFAULT_RESULT_PATH = "logs/results.log";
    
    Scanner scanner = new Scanner(System.in);
    /************************** End of constants **************************/
    
    
    /************************** Constructor declaration(s) **************************/
    // no Custom Constructor
    /************************** End of constructor declaration(s) **************************/

    /**
     * Main function that controls the flow of the program.
     * 
     * @param args      may contain any of the flags and paths listed above in the constants
     */
    public static void main(String[] args){
        EthicalEngine ethicalEngine = new EthicalEngine();
        
        // Gets arguments
        ArrayList<String> arguments = new ArrayList<>();
        for (int i = 0; i < args.length; i++){
            arguments.add(args[i]);
        }
        
        // Parsing flags
        boolean configFound = false;
        boolean resultFound = false;
        boolean interactiveFound = false;
        int configIndex = 0;
        int resultIndex = 0;
        for (int k=0; k < arguments.size(); k++){
            if (Arrays.asList(ethicalEngine.CONFIG).contains(arguments.get(k))){
                configFound = true;
                configIndex = k;
            }
            
            if (Arrays.asList(ethicalEngine.HELP).contains(arguments.get(k))){
                ethicalEngine.printHelp();
            }
            
            if (Arrays.asList(ethicalEngine.RESULTS).contains(arguments.get(k))){
                resultFound = true;
                resultIndex = k;
            }
            
            if (Arrays.asList(ethicalEngine.INTERACTIVE).contains(arguments.get(k))){
                interactiveFound = true;
            }
        }
        
        // Checks if config and/or result paths are present and parse them, or insert default
        String configPath = ethicalEngine.getConfigPath(arguments, configIndex, configFound, 
                ethicalEngine.NO_CONFIG_PATH);
        String resultPath = ethicalEngine.getResultPath(arguments, resultIndex, resultFound, 
                ethicalEngine.DEFAULT_RESULT_PATH);
        
        // Parsing Scenarios for configPath, if available
        ArrayList<Scenario> scenarioList = new ArrayList<>();
        if (configFound){
            ethicalEngine.parseCsv(scenarioList, configPath);
        }else{
            ethicalEngine.readWelcomeMessage();
        }
        
        // if one of interactive flags are present in the command line, run program as interactive
        if (interactiveFound){
            // do interactive
            ethicalEngine.runInteractive(configPath, resultPath, scenarioList);
        }else{ // lets the machine (i.e. Algorithm) decides on the outcomes of random Scenarios
            // generates random Scenarios
            ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
            Scenario scenario = scenarioGenerator.generate();
            scenarioList.add(scenario);
            
            // convert ArrayList<Scenario> into Scenario[]
            Scenario[] scenarioArray = new Scenario[scenarioList.size()];
            ethicalEngine.scenarioArrayListToArray(scenarioList, scenarioArray, 
                    scenarioArray.length);
            
            // Audits Scenario(s) in Scenario[]
            Audit auditRandomScenario = new Audit(scenarioArray);
            auditRandomScenario.setAuditType("Algorithm");
            auditRandomScenario.decideScenarios();
            auditRandomScenario.printStatistics(); // prints Audit results to interface
            auditRandomScenario.printToFile(resultPath); // prints Audit results to result path
        }
        System.exit(0);
    }
    
    /************************** Function declaration(s) **************************/
    /**
     * Reads welcome message in "welcome.ascii".
     * File may be changed accordingly.
     */
    public void readWelcomeMessage(){
        try (BufferedReader br = new BufferedReader(new FileReader("welcome.ascii"))){
            String line;
            while ((line = br.readLine()) != null){
                System.out.println(line);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Parses path for Scenario config file
     * @param arguments     command line arguments (ArrayList<String>)
     * @param k             index at which one of the config flags was found, if found (int)
     * @param found         was config flag found (boolean)
     * @param noPath        default "path" String if no path was found
     * @return              config Path from which Scenarios are read from, if exists (String)
     */
    public String getConfigPath(ArrayList<String> arguments, int k, boolean found, String noPath){
        if (found){
            if ((k == arguments.size() - ONE) || (arguments.get(k+ONE).charAt(ZERO) == '-')){
                String path = noPath;
                return path;
            }else{
                String path = arguments.get(k+ONE);
                return path;
            }
        }else{
            String path = noPath;
            return path;
        }
    }
    
    /**
     * Parses path for Scenario result file
     * @param arguments     command line arguments (ArrayList<String>)
     * @param k             index at which one of the result flags was found, if found (int)
     * @param found         was result flag found (boolean)
     * @param noPath        default "path" String if no path was found
     * @return              result Path from which Scenarios are read from, if exists (String)
     */
    public String getResultPath(ArrayList<String> arguments, int k, boolean found, String noPath){
        if (found){
            if ((k == arguments.size() - ONE) || (arguments.get(k+ONE).charAt(ZERO) == ('-'))){
                String path = noPath;
                return path;
            }else{
                String path = arguments.get(k+ONE);
                System.out.println(path);
                return path;
            }
        }else{
            String path = noPath;
            return path;
        }
    }
    
    /**
     * Algorithm to Decide the outcome of a Scenario based on its 'Characteristic scores'
     * @param scenario      Scenario which outcome is to be decided by the machine
     * @return              this machine's Decision (enum Decision)
     */
    public static Decision decide(Scenario scenario){
        // Scores gained (or lost) for each relevant characteristic
        double LEGAL_CROSSING_PED = 9;
        double LEGAL_CROSSING_PAS = -15;
        double ILLEGAL_CROSSING_PED = -11;
        double ILLEGAL_CROSSING_PAS = 7;
        double MORE_HEAD_COUNT = 8;
        double PREGNANT = 5;
        double IS_PERSON = 4;
        double IS_ANIMAL = 3.5;
        double IS_MINOR = 5;
        double FEMALE = 2;
        double STUDENT = 1;
        double YOU = 1;
        double AVERAGE_BODYTYPE = 1;
        
        // Initialises both Passenger and Pedestrian scores
        double passengerScore = 0;
        double pedestrianScore = 0;
        Decision decision;
        
        // Checks scores for Scenario-based Characteristics (e.g. crossing legality, etc)
        if(scenario.isLegalCrossing()){
            pedestrianScore += LEGAL_CROSSING_PED;
            passengerScore += LEGAL_CROSSING_PAS;
        }else{
            pedestrianScore += ILLEGAL_CROSSING_PED;
            passengerScore += ILLEGAL_CROSSING_PAS;
        }
        
        if(scenario.getPassengerCount() > scenario.getPedestrianCount()){
            passengerScore += MORE_HEAD_COUNT;
        }else{
            pedestrianScore += MORE_HEAD_COUNT;
        }
        
        if(scenario.hasYouInCar()){
            passengerScore += YOU;
        }else if(scenario.hasYouInLane()){
            pedestrianScore += YOU;
        }else{
            // do nothing
        }
        
        // Creates ArrayList<Character> to iterate over
        ArrayList<ethicalengine.Character> passengerList = new ArrayList<>();
        for (int i=0; i < scenario.getPassengerCount(); i++){
            passengerList.add(scenario.getPassengers()[i]);
        }
        ArrayList<ethicalengine.Character> pedestrianList = new ArrayList<>();
        for (int i=0; i < scenario.getPedestrianCount(); i++){
            pedestrianList.add(scenario.getPedestrians()[i]);
        }
        
        // Iterates over all Passengers and sums their total scores
        for(ethicalengine.Character passenger: passengerList){
            if (passenger instanceof Person){
                passengerScore += IS_PERSON;
                if (passenger.isPregnant()){
                    passengerScore += PREGNANT;
                }
                if (passenger.getAgeCategory() == Person.AgeCategory.CHILD ||
                        passenger.getAgeCategory() == Person.AgeCategory.BABY){
                    passengerScore += IS_MINOR;
                }
                if (passenger.getGender() == Person.Gender.FEMALE){
                    passengerScore += FEMALE;
                }
                if (passenger.getProfession() == Person.Profession.STUDENT){
                    passengerScore += STUDENT;
                }
                if (passenger.getBodyType() == Person.BodyType.AVERAGE){
                    passengerScore += AVERAGE_BODYTYPE;
                }
            }else{
                passengerScore += IS_ANIMAL;
            }
        }
        
        // Iterates over all Pedestrians and sums their total scores
        for(ethicalengine.Character pedestrian: pedestrianList){
            if (pedestrian instanceof Person){
                pedestrianScore += IS_PERSON;
                if (pedestrian.isPregnant()){
                    pedestrianScore += PREGNANT;
                }
                if (pedestrian.getAgeCategory() == Person.AgeCategory.CHILD ||
                        pedestrian.getAgeCategory() == Person.AgeCategory.BABY){
                    pedestrianScore += IS_MINOR;
                }
                if (pedestrian.getGender() == Person.Gender.FEMALE){
                    pedestrianScore += FEMALE;
                }
                if (pedestrian.getProfession() == Person.Profession.STUDENT){
                    pedestrianScore += STUDENT;
                }
                if (pedestrian.getBodyType() == Person.BodyType.AVERAGE){
                    pedestrianScore += AVERAGE_BODYTYPE;
                }
            }else{
                pedestrianScore += IS_ANIMAL;
            }
        }
        
        // Checks whose score is higher (Passenger or Pedestrian) and decides accordingly
        if(passengerScore > pedestrianScore){
            decision = Decision.PASSENGERS;
        }else if(pedestrianScore > passengerScore){
            decision = Decision.PEDESTRIANS;
        }else{ // in case of tie, Decision is based on whether crossing is legal
            if (!scenario.isLegalCrossing()){ // if Pedestrians crossed illegally, save Passengers
                decision = Decision.PASSENGERS;
            }else{
                decision = Decision.PEDESTRIANS; // if Pedestrians crossed legally, save Pedestrians
            }
        }
        
        return decision;
    }
    
    /**
     * Gets gender for a Character in config file.
     * 
     * @param gender        Character's gender as String
     * @param lineCount     the line(or row) index of the Character
     * @return              Character's gender (enum Gender)
     * @throws              InvalidCharacteristicException.java when invalid field values are detected
     */
    public ethicalengine.Character.Gender parseGenderString(String gender, int lineCount){
        ethicalengine.Character.Gender returnGender;
        
        try{
            if (gender.equalsIgnoreCase("female")){
                returnGender = ethicalengine.Character.Gender.FEMALE;
            }else if (gender.equalsIgnoreCase("male")){
                returnGender = ethicalengine.Character.Gender.MALE;
            }else if (gender.equalsIgnoreCase("queer")){
                returnGender = ethicalengine.Character.Gender.QUEER;
            }else if (gender.equalsIgnoreCase("unknown") || gender.equalsIgnoreCase("")) {
                returnGender = ethicalengine.Character.Gender.UNKNOWN;
            }else{
                throw new InvalidCharacteristicException();
            }
        }catch (InvalidCharacteristicException e){
            System.out.printf("WARNING: invalid characteristic in config file "
                    + "in line %d\n", lineCount);
            returnGender = ethicalengine.Character.Gender.UNKNOWN;
        }
        
        return returnGender;
    }
    
    // javadoc similar as above
    public ethicalengine.Character.BodyType parseBodyTypeString(String bodytype, int lineCount){
        ethicalengine.Character.BodyType returnBodytype;
        try{
            if (bodytype.equalsIgnoreCase("AVERAGE")){
                returnBodytype = ethicalengine.Character.BodyType.AVERAGE;
            }else if (bodytype.equalsIgnoreCase("ATHLETIC")){
                returnBodytype = ethicalengine.Character.BodyType.ATHLETIC;
            }else if (bodytype.equalsIgnoreCase("OVERWEIGHT")){
                returnBodytype = ethicalengine.Character.BodyType.OVERWEIGHT;
            }else if (bodytype.equalsIgnoreCase("UNSPECIFIED") || bodytype.equalsIgnoreCase("")) {
                returnBodytype = ethicalengine.Character.BodyType.UNSPECIFIED;
            }else{
                throw new InvalidCharacteristicException();
            }
        }catch(InvalidCharacteristicException e){
            System.out.printf("WARNING: invalid characteristic in config file "
                    + "in line %d\n", lineCount);
            returnBodytype = ethicalengine.Character.BodyType.UNSPECIFIED;
        }
        return returnBodytype;
    }
    
    // javadoc similar as above
    public ethicalengine.Person.Profession parseProfessionString(String profession, int lineCount){
        ethicalengine.Person.Profession returnProfession;
        try{
            if (profession.equalsIgnoreCase("DOCTOR")){
                returnProfession = ethicalengine.Person.Profession.DOCTOR;
            }else if (profession.equalsIgnoreCase("CEO")){
                returnProfession = ethicalengine.Person.Profession.CEO;
            }else if (profession.equalsIgnoreCase("CRIMINAL")){
                returnProfession = ethicalengine.Person.Profession.CRIMINAL;
            }else if (profession.equalsIgnoreCase("HOMELESS")){
                returnProfession = ethicalengine.Person.Profession.HOMELESS;
            }else if (profession.equalsIgnoreCase("STUDENT")){
                returnProfession = ethicalengine.Person.Profession.STUDENT;
            }else if (profession.equalsIgnoreCase("ACCOUNTANT")){
                returnProfession = ethicalengine.Person.Profession.ACCOUNTANT;
            }else if (profession.equalsIgnoreCase("LAWYER")){
                returnProfession = ethicalengine.Person.Profession.LAWYER;
            }else if (profession.equalsIgnoreCase("CHEF")){
                returnProfession = ethicalengine.Person.Profession.CHEF;
            }else if (profession.equalsIgnoreCase("UNEMPLOYED")){
                returnProfession = ethicalengine.Person.Profession.UNEMPLOYED;
            }else if (profession.equalsIgnoreCase("NONE")){
                returnProfession = ethicalengine.Person.Profession.NONE;
            }else if (profession.equalsIgnoreCase("UNKNOWN") || profession.equalsIgnoreCase("")){
                returnProfession = ethicalengine.Person.Profession.UNKNOWN;
            }else{
                throw new InvalidCharacteristicException();
            }
        }catch(InvalidCharacteristicException e){
            System.out.printf("WARNING: invalid characteristic in config file "
                    + "in line %d\n", lineCount);
            returnProfession = ethicalengine.Person.Profession.NONE;
        }
        
        return returnProfession;
    }
    
    /**
     * Identifies if Character in config is Person or Animal and parses their characteristics.
     * 
     * @param data          data containing relevant information of the Person
     * @param lineCount     line index of the Person in the config file
     * @return              a new Person object created based on the parsed characteristics.
     */
    public ethicalengine.Person parsePerson(String[] data, int lineCount){
        String genderString = data[HEADER_GENDER];
        ethicalengine.Character.Gender gender = parseGenderString(genderString, lineCount);
        int age = Integer.valueOf(data[HEADER_AGE]);
        String bodyTypeString = data[HEADER_BODYTYPE];
        ethicalengine.Character.BodyType bodytype = parseBodyTypeString(bodyTypeString, lineCount);
        String professionString = data[HEADER_PROFESSION];
        ethicalengine.Person.Profession profession = 
                parseProfessionString(professionString, lineCount);
        boolean isPregnant = Boolean.parseBoolean(data[HEADER_PREGNANT]);
        boolean isYou = Boolean.parseBoolean(data[HEADER_ISYOU]);
                                    
        ethicalengine.Person person = new Person(age, profession, gender, bodytype, isPregnant);
        person.setAsYou(isYou);
        return person;
    }
    
    // javadoc similar as above
    public ethicalengine.Animal parseAnimal(String[] data, int lineCount){
        String genderString = data[HEADER_GENDER];
        ethicalengine.Character.Gender gender = parseGenderString(genderString, lineCount);
        int age = Integer.valueOf(data[HEADER_AGE]);
        boolean isPet = Boolean.parseBoolean(data[HEADER_ISPET]);
        
        ethicalengine.Animal animal = new ethicalengine.Animal(data[HEADER_SPECIES]);
        animal.setGender(gender);
        animal.setAge(age);
        animal.setPet(isPet);
        
        return animal;
    }
    
    public void insertCharacter(String[] data, ArrayList<ethicalengine.Character> passengerList, 
            ArrayList<ethicalengine.Character> pedestrianList, 
            ethicalengine.Character character){
        if (data[HEADER_ROLE].equals("passenger")){
            passengerList.add(character);
        }else{
            pedestrianList.add(character);
        }
    }
    
    /**
     * Checks if there is an invalid number of data fields in a line/row.
     * 
     * @param data          Character data being parsed
     * @param lineCount     line index of the Character
     * @return              boolean
     * @throws              InvalidDataFormatException
     */
    public boolean throwInvalidDataFormat(String[] data, int lineCount){
        boolean thrown = false;
        try{
            if (data.length != DATA_FIELD){
                thrown = true;
                throw new InvalidDataFormatException();
            }
        }catch(InvalidDataFormatException e){
            System.out.printf("WARNING: invalid data format in config file "
                    + "in line %d\n", lineCount);
        }
        return thrown;
    }
    
    /**
     * Checks if a value can not be cast into an existing data type.
     * 
     * @param data          Character data being parsed
     * @param lineCount     line index of the Character
     * @return              boolean
     * @throws              NumberFormatException
     */
    public boolean throwNumberFormat(String[] data, int lineCount){
        boolean thrown = false;
        try{
            Integer.parseInt(data[HEADER_AGE]);
        }catch(NumberFormatException e){
            thrown = true;
            System.out.printf("WARNING: invalid number format in config file"
                + "in line %d\n", lineCount);
        }
        return thrown;
    }
    
    /**
     * Parses the config file: gets Scenarios and for each, a list of Passengers and Pedestrians.
     * For each Passenger and Pedestrians, gets their characteristics.
     * 
     * @param scenarioList      A list of Scenario(s) (ArrayList<Scenario>)
     * @param pathfile          config path file
     */
    public void parseCsv(ArrayList<Scenario> scenarioList, String pathfile){
        try{
            File file = new File(pathfile);
            if (file.isFile()){
                FileReader openFile = new FileReader(file);
                BufferedReader reader = new BufferedReader(openFile);
                String row;
                int lineCount = ZERO;
                try{
                    reader.readLine(); // ignoring the zeroth row
                    lineCount += ONE;
                    
                    String[] firstRow = reader.readLine().split(","); // config file is csv
                    lineCount += ONE;
                    
                    String[] scenarioLegality = firstRow[HEADER_CLASS].split(":");
                    boolean legality;
                    if (scenarioLegality[ONE].equalsIgnoreCase("red")){
                        legality = false;
                    }else{
                        legality = true;
                    }
                    ArrayList<ethicalengine.Character> passengerList = new ArrayList<>();
                    ArrayList<ethicalengine.Character> pedestrianList = new ArrayList<>();
                    
                    while ((row = reader.readLine()) != null){
                        lineCount += ONE;
                        String[] data = row.split(",");
                                             
                        if (data[HEADER_CLASS].equals("scenario:red") || 
                                data[HEADER_CLASS].equals("scenario:green")){
                            
                            // Adds previous Passengers and Pedestrian into two Scenario[]
                            ethicalengine.Character[] passengerArr = new 
                                    ethicalengine.Character[passengerList.size()];
                            ethicalengine.Character[] pedestrianArr = new 
                                    ethicalengine.Character[pedestrianList.size()];
                            passengerList.toArray(passengerArr);
                            pedestrianList.toArray(pedestrianArr);
                            
                            // Adds a new Scenario to scenarioList based on the above arrays
                            scenarioList.add(new Scenario(passengerArr, pedestrianArr, legality));
                            
                            // Clears ArrayLists for reuse
                            passengerList.clear();
                            pedestrianList.clear();
                            
                            // now update the new legality
                            scenarioLegality = data[HEADER_CLASS].split(":");
                            if (scenarioLegality[ONE].equalsIgnoreCase("red")){
                                legality = false;
                            }else{
                                legality = true;
                            }
                        }else{
                            if (throwInvalidDataFormat(data, lineCount)){
                                continue;
                            }
                            
                            if (throwNumberFormat(data, lineCount)){
                                data[HEADER_AGE] = DEFAULT_AGE;
                            }
                            
                            if (data[HEADER_CLASS].equals("person")){
                                Person person = parsePerson(data, lineCount);
                                insertCharacter(data, passengerList, pedestrianList, person);
                            }else{
                                Animal animal = parseAnimal(data, lineCount);
                                insertCharacter(data, passengerList, pedestrianList, animal);
                            }
                        }
                    }
                    // Creates the final Passenger and Pedestrian lists
                    ethicalengine.Character[] passengerArr = new 
                            ethicalengine.Character[passengerList.size()];
                    ethicalengine.Character[] pedestrianArr = new 
                            ethicalengine.Character[pedestrianList.size()];
                    passengerList.toArray(passengerArr);
                    pedestrianList.toArray(pedestrianArr);

                    // Adds the last Scenario to scenarioList
                    scenarioList.add(new Scenario(passengerArr, pedestrianArr, legality));
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
                reader.close();
                openFile.close();
            }else{
                throw new FileNotFoundException("ERROR: could not find config file.");
            }
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        readWelcomeMessage();
    }
    
    public void scenarioArrayListToArray(ArrayList<Scenario> scenarioList, 
        Scenario[] scenarioArray, int maxSize){
        scenarioArray = scenarioList.toArray(scenarioArray);
    }
    
    /**
     * Prints help documentation when either: help flag present, or config flag followed by no path
     */
    public void printHelp(){
        System.out.println("EthicalEngine - COMP90041 - Final Project");
        System.out.println();
        System.out.println("Usage: java EthicalEngine [arguments]");
        System.out.println();
        System.out.println("Arguments:");
        
        int FLAGS = 4;
        int CONFIG = 0;
        int HELP = 1;
        int RESULTS = 2;
        int INTERACTIVE = 3;
        int ADDITIONAL_SPACE = 1;
        String spaceBefore = "   ";
        String[] flags = new String[FLAGS];
        flags[CONFIG] = spaceBefore + "-c or --config";
        flags[HELP] = spaceBefore + "-h or --help";
        flags[RESULTS] = spaceBefore + "-r or --results";
        flags[INTERACTIVE] = spaceBefore + "-i or --interactive";
        int maxLength = flags[INTERACTIVE].length() + ADDITIONAL_SPACE;
        String[] descriptions = new String[FLAGS];
        descriptions[CONFIG] = "Optional: path to config file";
        descriptions[HELP] = "Print Help (this message) and exit";
        descriptions[RESULTS] = "Optional: path to results log file";
        descriptions[INTERACTIVE] = "Optional: launches interactive mode";
        
        for (int i = 0; i< FLAGS; i++){
            System.out.printf("%-" + maxLength + "s[%s]\n", flags[i], descriptions[i]);
        }
        
        System.exit(0);
    }
    
    /**
     * Runs the engine in Interactive mode
     * 
     * @param configPath        path to config file
     * @param resultPath        path to result file
     * @param scenarioList      ArrayList<Scenario> to store Scenarios to be displayed to the User
     * @throws InvalidInputException when User input to Consent question is invalid
     */
    public void runInteractive(String configPath, String resultPath, 
            ArrayList<Scenario> scenarioList){
        boolean continueProgram = true;
        System.out.print("Do you consent to have your decisions saved to a file? (yes/no)");
        boolean consentValidity = false;
        boolean userConsents = false;
        while (!consentValidity){
            try{
                String consent = scanner.nextLine();
                if (!(consent.equals("yes") || consent.equals("no"))){
                    throw new InvalidInputException();
                }else{
                    if (consent.equals("yes")){
                        userConsents = true;
                    }else{
                        userConsents = false;
                    }
                    consentValidity = true;
                }
            }catch (InvalidInputException e){
                System.out.println(e.getMessage());
            }
        }
        System.out.print("\n");
        
        Audit audit = new Audit();
        audit.setAuditSimulation(true);
        audit.setAuditType("User");
        if (configPath.equals(NO_CONFIG_PATH)){
            Scenario[] scenarioArray = new Scenario[SCENARIO_DISPLAYED];
            while (continueProgram){
                ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
                for (int i=0; i < SCENARIO_DISPLAYED; i++){
                    Scenario scenario = scenarioGenerator.generate();
                    scenarioList.add(scenario);
                }
                for (int i=0; i < SCENARIO_DISPLAYED; i++){
                    scenarioArray[i] = scenarioList.get(i);
                }
                scenarioList.clear();
                audit.setScenario(scenarioArray);
                userDecide(audit, scenarioArray);
                userConsent(audit, userConsents, resultPath);
                audit.printStatistics();
                System.out.print("\n");
                    System.out.print("Would you like to continue? (yes/no)");
                    String continueDisplayScenario = scanner.nextLine();
                    
                    if (continueDisplayScenario.equals("no")){
                        continueProgram = false;
                    }else if(continueDisplayScenario.equals("yes")){
                        continueProgram = true;
                        System.out.println("\n");
                    }else{
                        System.out.print("Invalid response, program terminates.");
                        continueProgram = false;
                    }
            }
            System.exit(0);
        }else{
            Scenario[] scenarioArray = new Scenario[SCENARIO_DISPLAYED];
            
            while (continueProgram){
                if (scenarioList.size() >= SCENARIO_DISPLAYED){
                    for (int i=0; i < SCENARIO_DISPLAYED; i++){
                        scenarioArray[i] = scenarioList.get(i);
                    }
                    audit.setScenario(scenarioArray);
                    userDecide(audit, scenarioArray);
                    audit.printStatistics();
                                       
                    for (int i=0; i < SCENARIO_DISPLAYED; i++){
                        scenarioList.remove(ZERO);
                    }
                    if (scenarioList.size() > ZERO){
                        System.out.print("\n");
                        System.out.print("Would you like to continue? (yes/no)");
                        String continueDisplayScenario = scanner.nextLine();
                        
                        if (continueDisplayScenario.equals("no")){
                            continueProgram = false;
                        }else if(continueDisplayScenario.equals("yes")){
                            continueProgram = true;
                            System.out.print("\n");
                        }else{
                            continueProgram = false;
                        }
                    }else{
                        System.out.print("\n");
                        System.out.print("That's all. Press Enter to quit.");
                        if (scanner.nextLine().equals("")){
                            System.exit(0);
                        }else{
                            System.exit(0);
                        }
                    }
                }else{
                    for (int i=0; i < scenarioList.size(); i++){
                        scenarioArray = new Scenario[scenarioList.size()];
                        scenarioArray[i] = scenarioList.get(i);
                    }
                    audit.setScenario(scenarioArray);
                    userDecide(audit, scenarioArray);
                    audit.printStatistics();
                    System.out.print("\n");
                    System.out.print("That's all. Press Enter to quit.");
                    if (scanner.nextLine().equals("")){
                        System.exit(0);
                    }else{
                        System.exit(0);
                    }
                }
            }
            userConsent(audit, userConsents, resultPath);
            System.exit(0);
        }
    }
    
    /**
     * A function to prompt user to input their Decision.
     * 
     * @param audit             Audit object in which User's Decision will be saved and inspected
     * @param scenarioArray     a list of Scenarios that the User must decide for (Scenario[])
     */
    public void userDecide(Audit audit, Scenario[] scenarioArray){
        for (int i=0; i < scenarioArray.length; i++){
            System.out.print(scenarioArray[i].toString());
            System.out.print("\nWho should be saved? (passenger(s) [1] or pedestrian(s) [2])");
            String survivor = scanner.nextLine();
            System.out.print("\n");
            if (survivor.equalsIgnoreCase("passenger") || survivor.equalsIgnoreCase("1") 
                    || survivor.equalsIgnoreCase("passengers")){
                audit.insertDecision(Decision.PASSENGERS, scenarioArray[i]);
            }else if(survivor.equalsIgnoreCase("pedestrian") || survivor.equalsIgnoreCase("2") 
                    || survivor.equalsIgnoreCase("pedestrians")){
                audit.insertDecision(Decision.PEDESTRIANS, scenarioArray[i]);
            }else{
                System.out.println("Invalid respose, please try again with the next scenario.");
                continue;
            }
        }
    }
    
    /**
     * Checks if User consents for their Decisions to be saved into the log file.
     * 
     * @param audit         Audit object in which the User's Decision is saved and inspected
     * @param consent       the User's consent
     * @param resultPath    the path to result file
     */
    public void userConsent(Audit audit, boolean consent, String resultPath){
        if (consent){
            audit.printToFile(resultPath);
        }
    }
    /************************** End of function declaration(s) **************************/
}