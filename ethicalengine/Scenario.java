package ethicalengine;

/**
 * This class contains relevant information regarding a presented scenario,
 * including: the list of passengers in the car, the list of pedestrians on the street, and
 * whether the pedestrians are crossing legally.
 * 
 * @author Nuvi Anggaresti | nanggaresti | 830683
 */
public class Scenario {
    /************************** List of attributes **************************/
    private Character[] passengers;
    private Character[] pedestrians;
    private boolean isLegalCrossing;
    /************************** End of attributes **************************/
    
    
    /************************** Constructor declaration(s) **************************/
    /**
     * A constructor for class Scenario.
     * 
     * @param passengers        a list of passengers in the car (Character[])
     * @param pedestrians       a list of pedestrians on the street (Character[])
     * @param isLegalCrossing   whether the pedestrians crossing legally (boolean)
     */
    public Scenario(Character[] passengers, Character[] pedestrians, 
            boolean isLegalCrossing){
        this.passengers = passengers;
        this.pedestrians = pedestrians;
        this.isLegalCrossing = isLegalCrossing;
    }
    /************************** End of constructor declaration(s) **************************/
    
    
    /************************** Function declaration(s) **************************/
    /**
     * Checks whether 'You' (i.e. the User) is in the 'Passengers' list.
     * 
     * @return 'true' or 'false' (boolean)
     */
    public boolean hasYouInCar(){
        boolean hasYou = false;
        
        for (Character passenger : this.passengers){
            if (passenger instanceof Person){
                if (((Person) passenger).isYou()){
                    hasYou = true;
                }else{
                    // do nothing
                }
            }else{
                //do nothing
            }
        }
        
        return hasYou;
    }
    
    /**
     * Checks whether 'You' (i.e. the User) is the 'Pedestrians' list.
     * 
     * @return 'true' or 'false' (boolean)
     */
    public boolean hasYouInLane(){
        boolean hasYou = false;
        
        for (Character pedestrian : this.pedestrians){
            if (pedestrian instanceof Person){
                if (((Person) pedestrian).isYou()){
                    hasYou = true;
                }else{
                    // do nothing
                }
            }else{
                //do nothing
            }
        }
        
        return hasYou;
    }
    
    /**
     * Gets the list of passengers in this Scenario.
     * 
     * @return this Scenario's Passengers list (Character[])
     */
    public Character[] getPassengers(){
        return this.passengers;
    }
    
    /**
     * Gets the number of Passengers in this Scenario.
     * 
     * @return the length (i.e. number of) this Scenario's Passenger array (int)
     */
    public int getPassengerCount(){
        return this.passengers.length;
    }
    
    /**
     * Gets the list of pedestrians in this Scenario.
     * 
     * @return this Scenario's Pedestrians list (Character[])
     */
    public Character[] getPedestrians(){
        return this.pedestrians;
    }
    
    /**
     * Gets the number of Pedestrians in this Scenario.
     * 
     * @return the length (i.e. number of) this Scenario's Pedestrian array (int)
     */
    public int getPedestrianCount(){
        return this.pedestrians.length;
    }
    
    /**
     * Checks whether Pedestrians are crossing legally.
     * 
     * @return this Scenario's 'isLegalCrossing' attribute (boolean)
     */
    public boolean isLegalCrossing(){
        return this.isLegalCrossing;
    }
    
    /**
     * Modifies the legality of pedestrian crossing in this Scenario.
     * 
     * @param isLegalCrossing   the new legality of pedestrian crossing in this scenario (boolean)
     */
    public void setLegalCrossing(boolean isLegalCrossing){
        this.isLegalCrossing = isLegalCrossing;
    }
    
    /**
     * Gets this Scenario's characteristics (list of passengers, pedestrians
     * (with their characteristics) as well as legality of crossing) and displays them as a String.
     * 
     * @return a String detailing this Scenario's characteristics and all characters involved
     */
    @Override
    public String toString(){
        String toReturn = "";
        
        String border = "======================================";
        String title = "# Scenario";
        toReturn = toReturn.concat(border + "\n" + title + "\n" + border);
        
        if (this.isLegalCrossing()){
            toReturn = toReturn.concat("\nLegal Crossing: yes");
        }else{
            toReturn = toReturn.concat("\nLegal Crossing: no");
        }
        
        toReturn = toReturn.concat("\nPassengers (" + this.getPassengerCount() + ")");
        String passengerList = "";
        for (Character passenger : this.passengers){
            passengerList = passengerList.concat("\n- " + passenger.toString());
        }
        toReturn = toReturn.concat(passengerList);
        
        toReturn = toReturn.concat("\nPedestrians (" + this.getPedestrianCount() + ")");
        String pedestrianList = "";
        for (Character pedestrian : this.pedestrians){
            pedestrianList = pedestrianList.concat("\n- " + pedestrian.toString());
        }
        toReturn = toReturn.concat(pedestrianList);
        
        return toReturn;
    }
    /************************** End of function declaration(s) **************************/
}