<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	
	<jstl:if test="${_command == 'show' }">
		<acme:input-textbox code="customer.booking.form.label.locator-code" path="locatorCode"/>
		<acme:input-textbox code="customer.booking.form.label.purcharse-moment" path="purcharseMoment"/>
		<acme:input-textbox code="customer.booking.form.label.travel-class" path="travelClass"/>
		<acme:input-money code="customer.booking.form.label.price" path="price"/>
		<acme:input-textbox code="customer.booking.form.label.last-nibble" path="lastNibble"/>
		
		<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?masterId=${id}"/>			
	</jstl:if>
	
	<jstl:if test="${_command == 'create' }">
		<acme:input-textbox code="customer.booking.form.label.locator-code" path="locatorCode"/>
		<acme:input-select code="customer.booking.form.label.travel-class" path="travelClass" choices="${travelClasses}"/>
		<acme:input-textbox code="customer.booking.form.label.last-nibble" path="lastNibble"/>
		<acme:input-money code="customer.booking.form.label.price" path="price"/>
		<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flights}"/>
		
				
		<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
	</jstl:if>

</acme:form>
