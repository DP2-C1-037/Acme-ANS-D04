
package acme.entities.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.airports.Airport;

@Service
public class WeatherService extends AbstractService {

	private static final String	API_KEY			= "478a54206224dd3e0153ae2c8570045d";
	private static final String	URL_TEMPLATE	= "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";


	public WeatherConditions getWeatherForAirport(final Airport airport) {
		try {
			String urlString = String.format(WeatherService.URL_TEMPLATE, airport.getCity(), WeatherService.API_KEY);
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// Leer respuesta
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
				response.append(line);
			reader.close();

			// Parsear JSON (usa una librería como org.json o Jackson)
			JSONObject json = new JSONObject(response.toString());
			JSONObject main = json.getJSONObject("main");
			JSONObject wind = json.getJSONObject("wind");
			JSONArray weather = json.getJSONArray("weather");

			WeatherConditions weatherConditions = new WeatherConditions();
			weatherConditions.setAirport(airport);
			weatherConditions.setTemperature(main.getDouble("temp"));
			weatherConditions.setWindSpeed(wind.getDouble("speed"));
			weatherConditions.setConditions(weather.getJSONObject(0).getString("description"));
			weatherConditions.setHumidity(main.getDouble("humidity"));
			weatherConditions.setTimestamp(LocalDateTime.now());

			return weatherConditions;
		} catch (Exception e) {
			throw new RuntimeException("Error fetching weather data", e);
		}
	}
}
