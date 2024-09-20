import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {

    public static JSONObject getWeatherData(String locationName) { 
        JSONArray locationData = getLocationData(locationName);

        if (locationData == null || locationData.isEmpty()) {
            System.out.println("Error: Location data not found.");
            return null;
        }

        JSONObject location = (JSONObject) locationData.get(0);

        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?" + 
        "latitude=" + latitude + "&longitude=" + longitude + 
        "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

        try { 
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) { 
                System.out.println("Error: Could not connect to API");
                return null;
            }

            StringBuilder resultJSON = new StringBuilder();
            Scanner sc = new Scanner(conn.getInputStream());
            while(sc.hasNextLine()) { 
                resultJSON.append(sc.nextLine());
            }

            sc.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultsJSONobj = (JSONObject) parser.parse(String.valueOf(resultJSON));

            JSONObject hourly = (JSONObject) resultsJSONobj.get("hourly");

            JSONArray time = (JSONArray) hourly.get("time");

            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));

            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windSpeed = (double) windSpeedData.get(index);

            JSONObject weatherData = new JSONObject();

            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("wind_speed", windSpeed);

            return weatherData;
        } 
        catch(Exception e) { 
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getLocationData(String locationName) { 
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try { 
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) { 
                System.out.println("Error: Could not connect to API");
                return null;
            }

            StringBuilder resultJSON = new StringBuilder();
            Scanner sc = new Scanner(conn.getInputStream());

            while (sc.hasNext()) { 
                resultJSON.append(sc.nextLine());
            }

            sc.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJSON));

            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            return locationData;
        } 
        catch(Exception e) { 
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) { 
        try { 
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        }
        catch(IOException e) { 
            e.printStackTrace();
            return null;
        }
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        for (int i = 0; i < timeList.size(); i++) { 
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) { 
                return i;
            }
        }

        return 0;
    }

    public static String getCurrentTime() { 
        LocalDateTime currentDateTime = LocalDateTime.now(); 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00");
        return currentDateTime.format(formatter);
    }


private static String convertWeatherCode(long weathercode) { 
    String weatherCondition = "";

    if (weathercode == 0L) { 
        weatherCondition = "Clear";
    } 

    else if (weathercode <= 3L && weathercode > 0L) { 
        weatherCondition = "Cloudy";
    }

    else if ((weathercode >= 51L && weathercode <=67L) || (weathercode >= 80L && weathercode<=99L)) { 
          
       weatherCondition = "Rain";
    }

    else if ((weathercode >= 71L && weathercode<=77L)) { 
        weatherCondition = "Snow";
    }

    return weatherCondition;


}

}
