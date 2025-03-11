
package acme.entities.weather;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WeatherConditions extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "airport_iata") // Relación con el aeropuerto (ej: "MAD", "JFK")
	private Airport				airport;

	private Double				temperature;   // Temperatura en Kelvin (directo de la API)
	private Double				windSpeed;     // Velocidad del viento en m/s (directo de la API)
	private String				conditions;    // Ej: "clear sky", "rain"
	private Double				humidity;     // Porcentaje (0-100)
	private LocalDateTime		timestamp;

	// EJEMPLO RESPUESTA API OpenWeatherMap
	//	{
	//		  "weather": [{"main": "Clear", "description": "clear sky"}],
	//		  "main": {
	//		    "temp": 298.15, // Kelvin (25°C)
	//		    "humidity": 65
	//		  },
	//		  "wind": {
	//		    "speed": 3.6 // m/s (12.96 km/h)
	//		  },
	//		  "name": "Madrid"
	//		}
}
