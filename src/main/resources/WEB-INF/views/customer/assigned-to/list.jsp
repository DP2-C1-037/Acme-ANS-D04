<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.assigned-to.list.label.passport-number" path="passenger.fullName" width="40%"/>
	<acme:list-column code="customer.assigned-to.list.label.passport-number" path="passenger.email" width="40%"/>
	<acme:list-column code="customer.assigned-to.list.label.passport-number" path="passenger.passportNumber" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="customer.assigned-to.list.button.create" action="/customer/assigned-to/create"/>
