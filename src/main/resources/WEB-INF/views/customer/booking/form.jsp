<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	
	<jstl:if test="${_command == 'show' }">
		<acme:input-textbox code="customer.booking.form.label.locator-code" path="locatorCode"/>
		<acme:input-textbox code="customer.booking.form.label.purcharse-moment" path="purcharseMoment"/>
		<acme:input-textbox code="customer.booking.form.label.travel-class" path="travelClass"/>
		<acme:input-textbox code="customer.booking.form.label.price" path="price"/>
		<acme:input-textbox code="customer.booking.form.label.last-nibble" path="lastNibble"/>
		<acme:list>
			<acme:list-column code="customer.booking.list.label.locator-code" path="locatorCode" width="10%"/>
			<acme:list-column code="customer.booking.list.label.purcharse-moment" path="purcharseMoment" width="90%"/>
			<acme:list-column code="customer.booking.list.label.travel-class" path="travelClass" width="90%"/>	
			<acme:list-column code="customer.booking.list.label.price" path="price" width="90%"/>	
			<acme:list-payload path="payload"/>
		</acme:list>
		
	</jstl:if>
	
	<jstl:if test="${_command == 'create' }">
		<acme:input-checkbox code="customer.booking.form.label.confirmation" path="confirmation"/>	
		<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
	</jstl:if>	
</acme:form>
