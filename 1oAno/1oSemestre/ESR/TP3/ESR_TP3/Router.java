
import Node.RouterData;

// Nó responsável por redirecionar o tráfego na rede overlay
public class Router {
    private static RouterData rt;
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Ending connection...");
                rt.endConnection();
            }
        });
        try {
			rt = new RouterData(args);
            rt.start();
            
		} catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
		}   
    }
}
