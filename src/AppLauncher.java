import javax.swing.SwingUtilities;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override

            public void run() { 
                // display the weather GUI

                new WeatherAppGUI().setVisible(true);;

                // System.out.println(WeatherApp.getLocationData("Tokyo"));

                // System.out.println(WeatherApp.getCurrentTime());
            }
        });
    }
}
