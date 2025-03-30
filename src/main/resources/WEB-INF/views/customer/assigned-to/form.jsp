<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	
	<jstl:if test="${_command == 'show' }">
		<acme:input-textbox code="customer.assigned-to.form.label.locator-code" path="booking.locatorCode"/>
		<acme:input-textbox code="customer.assigned-to.form.label.passport-number" path="passenger.passportNumber"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'create' }">
		<acme:input-select code="customer.assigned-to.form.label.locator-code" path="booking" choices="${bookings}"/>
		<acme:input-select code="customer.assigned-to.form.label.passport-number" path="passenger" choices="${passengers}"/>
		
		<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
	</jstl:if>

</acme:form>
