import org.apache.log4j.Logger;

public class Main {
    static Logger logger = Logger.getLogger (Main.class.getName ());
    public static void main(String[] args) {
//        System.out.println (" == Program Started == " );
        logger.info ("== Program Started ==");

//        System.out.println (" == Program Finished == " );
        logger.info ("== Program Finished ==");
    }
}
