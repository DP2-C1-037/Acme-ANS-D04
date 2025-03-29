<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.assigned-to.list.label.locator-code" path="booking.locatorCode" width="50%"/>
	<acme:list-column code="customer.assigned-to.list.label.passport-number" path="passenger.passportNumber" width="50%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="customer.assigned-to.list.button.create" action="/customer/assigned-to/create"/>
