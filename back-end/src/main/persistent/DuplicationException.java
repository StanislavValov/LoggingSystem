package main.persistent;

/**
 * Created by Stan on 30.12.2016 г..
 */
public class DuplicationException extends Throwable {
    public DuplicationException() {
    }

    public DuplicationException(String s) {
        super(s);
    }
}
