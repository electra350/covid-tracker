# covid-tracker

Simple SpringBootApplication which loads CSV data from remote source, parses and displays COVID data.

- Uses Thymeleaf template for UI stuff
- Reports are classified by Country, State, Total Cases Reported, New Cases
- The data is fetched as soon as application is started and then reloaded automatically every hour

# Data Source:
https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/
