<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="flightCrewMember.flightAssignment.form.label.lastUpdateMoment" path="lastUpdateMoment"/>
	<acme:input-select code="flightCrewMember.flightAssignment.form.label.flightCrewMember" path="member" choices= "${members}"/>
	<acme:input-select code="flightCrewMember.flightAssignment.form.label.flightCrewDuty" path="duty" choices= "${duties}"/>	
	<acme:input-select code="flightCrewMember.flightAssignment.form.label.leg" path="leg" choices= "${legs}"/>
	<acme:input-select code="flightCrewMember.flightAssignment.form.label.status" path="status" choices= "${statuses}"/>
	<acme:input-textarea code="flightCrewMember.flightAssignment.form.label.remarks" path="remarks"/>
	
	
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="flightCrewMember.flightAssignment.form.button.update" action="/flightCrewMember/flightAssignment/update"/>
			<acme:submit code="flightCrewMember.flightAssignment.form.button.delete" action="/flightCrewMember/flightAssignment/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="flightCrewMember.flightAssignment.form.label.confirmation" path="confirmation"/>
			<acme:submit code="flightCrewMember.flightAssignment.form.button.create" action="/flightCrewMember/flightAssignment/create"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>
