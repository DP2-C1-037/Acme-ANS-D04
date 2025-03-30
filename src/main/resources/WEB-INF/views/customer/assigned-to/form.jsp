<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	<acme:input-select code="customer.assigned-to.form.label.locator-code" path="booking" choices="${bookings}"/>
	<acme:input-select code="customer.assigned-to.form.label.passport-number" path="passenger" choices="${passengers}"/>
	
	<jstl:if test="${_command == 'create' }">
		<acme:submit code="customer.booking.form.button.create" action="/customer/assigned-to/create"/>
	</jstl:if>

</acme:form>
