<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.lastUpdateMoment" path="lastUpdateMoment" width="34%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.status" path="status" width="33%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.flightCrewDuty" path="flightCrewDuty" width="33%"/>
	<acme:list-payload path="payload"/>	
	
</acme:list>

<jstl:if test="${acme:anyOf(_command, 'list-mine-planned|list-mine-completed')}">
	<acme:button code="flight-crew-member.flight-assignment.list.button.create" action="/flight-crew-member/flight-assignment/create"/>
</jstl:if>	
