
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidScore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineManagerDashboard extends AbstractForm {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidNumber(min = 0)
	private Double					ranking;

	@Mandatory
	@ValidNumber(min = 0)
	private Integer					yearsToRetire;

	@Mandatory
	@ValidScore
	private Double					onTimeRatio;

	@Mandatory
	private Map<String, Integer>	mostPopularAirports;

	@Mandatory
	private Map<String, Integer>	leastPopularAirports;

	@Mandatory
	private Map<String, Integer>	legsByStatus;

	@Mandatory
	@ValidMoney(min = 0.00)
	private Double					averageCost;

	@Mandatory
	@ValidMoney(min = 0.00)
	private Double					minCost;

	@Mandatory
	@ValidMoney(min = 0.00)
	private Double					maxCost;

	@Mandatory
	@ValidMoney(min = 0.00)
	private Double					standardDeviationCost;
}
