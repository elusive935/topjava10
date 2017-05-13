<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <base href="${pageContext.request.contextPath}/"/>
    <h2><a href="index.html"><spring:message code="app.home"/></a></h2>
    <h2>
        <c:choose>
            <c:when test="${meal.id == null}">
                <spring:message code="meal.titlecreate"/>
            </c:when>
            <c:otherwise>
                <spring:message code="meal.titleupdate"/>
            </c:otherwise>
        </c:choose>
    </h2>

    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals/save">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="common.datetime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime"></dd>
        </dl>
        <dl>
            <dt><spring:message code="common.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description"></dd>
        </dl>
        <dl>
            <dt><spring:message code="common.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories"></dd>
        </dl>
        <button type="submit"><spring:message code="meal.save"/></button>
        <button onclick="window.history.back()"><spring:message code="meal.cancel"/></button>
    </form>
</section>
</body>
<jsp:include page="fragments/footer.jsp"/>
</html>
