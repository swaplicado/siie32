package erp.redis;

public class SLockException extends Exception{
    public SLockException(String message){
        super(message);
    }
}
