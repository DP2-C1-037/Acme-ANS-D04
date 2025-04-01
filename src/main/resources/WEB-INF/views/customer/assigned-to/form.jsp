<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:input-select code="customer.assigned-to.form.label.locator-code" path="booking" choices="${bookings}" readonly="true"/>
			<acme:input-select code="customer.assigned-to.form.label.passport-number" path="passenger" choices="${passengers}" readonly="true"/>
			<acme:submit code="customer.assigned-to.form.button.delete" action="/customer/assigned-to/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="customer.assigned-to.form.label.locator-code" path="booking" choices="${bookings}"/>
			<acme:input-select code="customer.assigned-to.form.label.passport-number" path="passenger" choices="${passengers}"/>
			<acme:submit code="customer.assigned-to.form.button.create" action="/customer/assigned-to/create"/>
		</jstl:when>
	</jstl:choose>

</acme:form>
