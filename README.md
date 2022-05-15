# covid-tracker

Simple SpringBootApplication which loads CSV data from remote source, parses and displays COVID data.


<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">

<head>
    <title>Corona Virus Tracker Application</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" media="all"
          href="../../css/gtvg.css" th:href="@{/css/gtvg.css}"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
          crossorigin="anonymous">
</head>

<body>
<div class="container">
    <h1>Corona Virus Tracker Application</h1>
    <p>This application reports corona virus cases across the globe.</p>


    <div class="bg-light p-5 rounded-lg m-3">
        <h1 class="display-4" th:text="${totalReportedCases}"></h1>
        <p class="lead">Total cases reported as of today</p>
        <hr class="my-4">
        <p>
            <span>New cases reported since previous day</span>
            <span th:text="${totalNewCases}"></span>
        </p>
    </div>


    <table class="table">
        <tr>
            <th>Country</th>
            <th>State</th>
            <th>Total Cases Reported</th>
            <th>New Cases</th>
        </tr>
        <tr th:each="stat : ${locationStats}">
            <td th:text="${stat.country}"></td>
            <td th:text="${stat.state}"></td>
            <td th:text="${stat.latestTotalCases}">0</td>
            <td th:text="${stat.newCases}">0</td>
        </tr>
    </table>
</div>
</body>

</html>
