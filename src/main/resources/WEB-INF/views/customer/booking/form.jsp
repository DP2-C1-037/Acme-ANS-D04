<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	<acme:input-textbox code="customer.booking.form.label.header" path="header"/>
	<acme:input-textarea code="customer.booking.form.label.description" path="description"/>
	<jstl:if test="${_command == 'show' }">
		<acme:input-textarea code="customer.booking.form.label.redress" path="redress"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'create' }">
		<acme:input-checkbox code="customer.booking.form.label.confirmation" path="confirmation"/>	
		<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
	</jstl:if>	
</acme:form>
