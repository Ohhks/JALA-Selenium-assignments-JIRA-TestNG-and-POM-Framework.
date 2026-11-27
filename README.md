# JALA Academy Magnus — Selenium, TestNG & POM Automation Framework

Complete end-to-end QA Automation Suite built strictly on **JALA Academy Magnus** (`https://magnus.jalatechnologies.com/`) following all 6 JALA Selenium Assignments and all 20 Selenium POM Framework Steps.

## Project Structure
```text
magnus-jala-automation/
├── pom.xml
├── testng.xml                     # Master TestNG Suite (Modules + Assignments 1-6)
├── testng-login.xml               # Module Suite: Login (10 tests)
├── testng-employee.xml            # Module Suite: Employee Create & Search (10 tests)
├── testng-more.xml                # Module Suite: More Features (10 tests)
├── testng-parallel.xml            # Parallel Group Execution Suite
├── docs/
│   ├── Manual_Test_Cases_Magnus.csv                      # 34 Manual Test Cases & Scenarios
│   ├── JIRA_Defects_Magnus.md                            # 5 Verified JIRA Defect Reports (MAG-101..105)
│   └── Magnus_Selenium_TestNG_JIRA_POM_Master_Guide.pdf  # 15-Page Master Guide (Screenshots & Walkthrough)
├── src/main/java/com/jala/magnus/
│   ├── actions/PageActions.java   # Framework Step 7: All WebElement, Alert, Window, Actions wrappers
│   ├── driver/DriverFactory.java  # Framework Step 16: Multi-browser ThreadLocal Factory
│   ├── pages/                     # Framework Step 6: PageObjects classes
│   │   ├── LoginPage.java
│   │   ├── HomePage.java
│   │   ├── EmployeeCreatePage.java
│   │   ├── EmployeeSearchPage.java
│   │   └── MoreFeaturesPage.java
│   └── utils/                     # ConfigReader, LocatorReader, ExcelUtil, ExtentManager, LoggerUtil, ScreenshotUtil
└── src/test/
    ├── java/com/jala/magnus/
    │   ├── assignments/           # Assignments 1-6 + Step 1 main() classes
    │   ├── base/BaseTest.java     # Framework Step 12: Common setup & teardown
    │   ├── listeners/ExtentTestListener.java
    │   └── tests/                 # Modular POM Test Suites (30 tests total)
    └── resources/
        ├── config.properties      # Framework Step 14
        ├── locators.properties    # Framework Step 5
        ├── log4j2.xml             # Framework Step 13
        └── testdata/MagnusTestData.xlsx # Framework Step 17
```

## Running from Command Prompt (TestNG Step 13)
```bash
# Run Master Suite (Chrome headless)
mvn test -DsuiteXmlFile=testng.xml -Dbrowser=chrome -Dheadless=true

# Run Individual Module Suites
mvn test -DsuiteXmlFile=testng-login.xml
mvn test -DsuiteXmlFile=testng-employee.xml
mvn test -DsuiteXmlFile=testng-more.xml

# Run Parallel Groups Suite on Edge or Firefox
mvn test -DsuiteXmlFile=testng-parallel.xml -Dbrowser=edge
mvn test -DsuiteXmlFile=testng-parallel.xml -Dbrowser=firefox
```

## Reports & Logs
- **Extent HTML Report:** `reports/ExtentReport.html`
- **Log4j2 Execution Logs:** `logs/magnus-automation.log`
- **Failure / Verification Screenshots:** `screenshots/`

## Verified Execution Results
- **Master Suite (`testng.xml`):** 62/62 tests passed (0 failures, 0 errors, 0 skipped)
- **Parallel Groups Suite (`testng-parallel.xml`):** 14/14 tests passed across 2 parallel threads

## Repository
- **GitHub:** `https://github.com/Ohhks/JALA-Selenium-assignments-JIRA-TestNG-and-POM-Framework.`
