/**
 * Throws an exception when invalid field values are detected, for example 'skinny' as a bodyType.
 * 
 * @author Nuvi Anggaresti
 */
public class InvalidCharacteristicException extends Exception{
    public InvalidCharacteristicException(){
        super("Invalid data format present in a line");
    }
    public InvalidCharacteristicException(String message){
        super(message);
    }
}
