/**
 * Throws an exception in case of invalid number of data fields per row, i.e. not equal to 10
 * 
 * @author Nuvi Anggaresti
 */
public class InvalidDataFormatException extends Exception{
    public InvalidDataFormatException(){
        super("Invalid data format present in a line");
    }
    public InvalidDataFormatException(String message){
        super(message);
    }
}
