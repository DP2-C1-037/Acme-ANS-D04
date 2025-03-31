<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	<acme:input-select code="customer.assigned-to.form.label.locator-code" path="booking" choices="${bookings}"/>
	<acme:input-select code="customer.assigned-to.form.label.passport-number" path="passenger" choices="${passengers}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:submit code="customer.booking.form.button.delete" action="/customer/assigned-to/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/assigned-to/create"/>
		</jstl:when>
	</jstl:choose>

</acme:form>
