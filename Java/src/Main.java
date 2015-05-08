public class Main {
	public static void main(String[] args) {
        try {
            DataHandler dataHandler = new DataHandler();
            new QiuShiClient(dataHandler);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}