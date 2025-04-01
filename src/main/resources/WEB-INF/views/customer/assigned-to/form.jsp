<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:input-textbox code="customer.assigned-to.form.label.full-name" path="passenger.fullName"/>
			<acme:input-textbox code="customer.assigned-to.form.label.email" path="passenger.email"/>
			<acme:input-textbox code="customer.assigned-to.form.label.passport-number" path="passenger.passportNumber"/>
			<acme:input-moment code="customer.assigned-to.form.label.birth-date" path="passenger.birthDate"/>
			<acme:input-textbox code="customer.assigned-to.form.label.special-needs" path="passenger.specialNeeds"/>			
			<acme:submit code="customer.assigned-to.form.button.delete" action="/customer/assigned-to/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="customer.assigned-to.form.label.passport-number" path="passenger" choices="${passengers}"/>
			<acme:submit code="customer.assigned-to.list.button.add" action="/customer/assigned-to/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>

</acme:form>
