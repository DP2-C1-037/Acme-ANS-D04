<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.booking.list.label.locator-code" path="locator-code" width="10%"/>
	<acme:list-column code="customer.booking.list.label.purcharse-moment" path="purcharse-moment" width="90%"/>	
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="customer.booking.list.button.create" action="/customer/booking/create"/>
