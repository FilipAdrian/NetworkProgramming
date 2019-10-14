import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        DataManager dataManager = new DataManager ();
        List <String >list = new ArrayList <>();
        list.add ("[{\"email\":\"dtyas0@github.com\",\"organization\":\"Zoonder\",\"full_name\":\"Druci Tyas\",\"employee_id\":\"36-0066196\"},\n" +
                "{\"email\":\"edonaghy1@networkadvertising.org\",\"organization\":\"Babbleblab\",\"full_name\":\"Evvy Donaghy\",\"employee_id\":\"12-2365264\"},\n" +
                "{\"email\":\"gagerskow5@adobe.com\",\"organization\":\"Meemm\",\"full_name\":\"Gary Agerskow\",\"employee_id\":\"11-7548723\"},\n" +
                "{\"email\":\"iolenane6@imageshack.us\",\"organization\":\"Pixope\",\"full_name\":\"Isidore O'Lenane\",\"employee_id\":\"64-8427054\"},\n" +
                "{\"email\":\"vwynett7@ed.gov\",\"organization\":\"Dazzlesphere\",\"full_name\":\"Vikky Wynett\",\"employee_id\":\"01-7700844\"},\n" +
                "{\"email\":\"ssagee@amazonaws.com\",\"organization\":\"Jaloo\",\"full_name\":\"Shawn Sage\",\"employee_id\":\"55-0926932\"},\n" +
                "{\"email\":\"fblaslif@people.com.cn\",\"organization\":\"Realcube\",\"full_name\":\"Fabian Blasli\",\"employee_id\":\"30-9035208\"},\n" +
                "{\"email\":\"jswanng@eventbrite.com\",\"organization\":\"Yotz\",\"full_name\":\"Jacquenetta Swann\",\"employee_id\":\"03-4127236\"},\n" +
                "{\"email\":\"ashopcotth@ifeng.com\",\"organization\":\"Voonder\",\"full_name\":\"Antin Shopcott\",\"employee_id\":\"73-4525498\"},]");
        dataManager.getJsonObjectByKeyAndValue ("email","dtyas0@github.com",list);
    }
}
