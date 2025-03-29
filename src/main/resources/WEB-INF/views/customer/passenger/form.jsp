<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${_command == 'show' }">
		<acme:input-textbox code="customer.passenger.form.label.full-name" path="fullName"/>
		<acme:input-textbox code="customer.passenger.form.label.email" path="email"/>
		<acme:input-textbox code="customer.passenger.form.label.passport-number" path="passportNumber"/>
		<acme:input-moment code="customer.passenger.form.label.birth-date" path="birthDate"/>
		<acme:input-textbox code="customer.passenger.form.label.special-needs" path="specialNeeds"/>
	</jstl:if>
	<jstl:if test="${_command == 'create' }">
		<acme:input-textbox code="customer.passenger.form.label.full-name" path="fullName"/>
		<acme:input-textbox code="customer.passenger.form.label.email" path="email"/>
		<acme:input-textbox code="customer.passenger.form.label.passport-number" path="passportNumber"/>
		<acme:input-moment code="customer.passenger.form.label.birth-date" path="birthDate"/>
		<acme:input-textbox code="customer.passenger.form.label.special-needs" path="specialNeeds"/>
		
		<acme:submit code="customer.booking.form.button.create" action="/customer/passenger/create"/>
	</jstl:if>
	
	
	
</acme:form>



