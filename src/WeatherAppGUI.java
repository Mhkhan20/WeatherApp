import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;

import javax.imageio.ImageIO;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.SwingPropertyChangeSupport;

import org.json.simple.JSONObject;

public class WeatherAppGUI extends JFrame {

        private JSONObject weatherData;

        public WeatherAppGUI() { 
            
            // setting title and adding a title 

            super("WeatherApp");

            // configuire GUI to end program once intended use is complete 
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            // set the sizing of the window 
            setSize(450, 650);
            setLocationRelativeTo(null);
            setLayout(null);
            setResizable(false);

            addGuiComponents();

        }

        private void addGuiComponents() { 
            // adding a search field 
            JTextField searchField = new JTextField();
            searchField.setFont(new Font("Dialog", Font.PLAIN, 24));

            // adding a clear Button 
            JButton clearButton = new JButton("Clear");
            // function for clearing the textField 
            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { 
                    searchField.setText("");
                }
            });

            clearButton.setFont(new Font("Dialog", Font.PLAIN, 18));
            clearButton.setBackground(Color.WHITE);
            clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            

            
             
            // Weather image
             JLabel weatherCondImage = new JLabel(loadImage("src\\assets\\cloudy.png"));

             // Temperature Text

             JLabel tempText = new JLabel("10 C");
             tempText.setFont(new Font("Dialog", Font.BOLD, 48));
             tempText.setHorizontalAlignment(SwingConstants.CENTER);;

             // Weather Conditions Description 

             JLabel WeatherDesc = new JLabel("Cloudy");
             WeatherDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
             WeatherDesc.setHorizontalAlignment(SwingConstants.CENTER);

             //Humidity Image 

             JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
           


             //Humudity Text

             JLabel humudityText = new JLabel("<html> <b>Humudity</b> 100% </html>");
             humudityText.setFont(new Font("Dialog", Font.PLAIN, 16));
            

             // Windspeed Image 
             JLabel windSpeedImage = new JLabel(loadImage("src/assets/windspeed.png"));

             // Windspeed Text

             JLabel windSpeedDesc = new JLabel("<html> <b> WindSpeed </b> 15km/h </html>");
             windSpeedDesc.setFont(new Font("Dialog", Font.PLAIN, 16));
    
                // setting the location of the componoents
            searchField.setBounds(15, 15, 351, 45);
            weatherCondImage.setBounds(0, 125, 450, 217);
            tempText.setBounds(0, 350, 450, 54);
            WeatherDesc.setBounds(0, 405, 450, 36);
            humidityImage.setBounds(15, 500, 74, 66);
            humudityText.setBounds(90, 500, 85, 55);
            windSpeedImage.setBounds(220, 500, 74, 66);
            windSpeedDesc.setBounds(310, 500, 85, 55);
            clearButton.setBounds(15, 70, 100, 35);
            



            // adding the components 
            add(searchField);
            add(weatherCondImage);
            add(tempText);
            add(WeatherDesc);
            add(humidityImage);
            add(humudityText);
            add(windSpeedImage);
            add(windSpeedDesc);
            add(clearButton);

            // Search Button
             JButton searchButton = new JButton(loadImage("C:\\Users\\Muham\\OneDrive\\Desktop\\Education\\Projects\\mainW\\src\\assets\\search.png"));
             searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
             searchButton.setBounds(375, 13, 47, 45);
             searchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { 
                    String userInput = searchField.getText();
                
                    if (userInput.replaceAll("\\s", "").length() <=0) {
                        return;
                    }
                
                    JSONObject weatherData = WeatherApp.getWeatherData(userInput);
                
                    // Check if weatherData is null
                    if (weatherData == null) {
                        // Handle the case where no weather data is returned
                        // For example, display an error message to the user
                        return;
                    }
                
                    // update GUI
                    String weatherCondition = (String) weatherData.get("weather_condition");
                    if (weatherCondition != null) {
                        switch (weatherCondition) {
                            case "Clear": 
                                weatherCondImage.setIcon(loadImage("src/assets/clear.png"));
                                break;
                            case "Cloudy": 
                                weatherCondImage.setIcon(loadImage("src/assets/cloudy.png"));
                                break;
                            case "Rain": 
                                weatherCondImage.setIcon(loadImage("src/assets/rain.png"));
                                break;
                            case "Snow": 
                                weatherCondImage.setIcon(loadImage("src/assets/snow.png"));
                                break;
                        }
                        WeatherDesc.setText(weatherCondition);
                    }
                
                    Number temperatureObj = (Number) weatherData.get("temperature");
                    if (temperatureObj != null) {
                        double temperature = temperatureObj.doubleValue();
                        tempText.setText(temperature + " C");
                    }
                
                    Number humidityObj = (Number) weatherData.get("humidity");
                    if (humidityObj != null) {
                        long humidity = humidityObj.longValue();
                        humudityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");
                    }
                
                    Number windSpeedObj = (Number) weatherData.get("windspeed");
                    if (windSpeedObj != null) {
                        double windSpeed = windSpeedObj.doubleValue();
                        windSpeedDesc.setText("<html><b>WindSpeed</b> " + windSpeed + "km/h</html>");
                    }
                }
                
             });
             add(searchButton);
        }

        private ImageIcon loadImage ( String resourcePath) { 
            try { 
                BufferedImage image = ImageIO.read(new File(resourcePath));

                return new ImageIcon(image);
            }

            catch(IOException e) { 
                e.printStackTrace();

            }

            System.out.println("Could not find Image Source");
            return null;

            
        }
 
}

