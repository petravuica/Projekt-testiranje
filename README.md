# Projekt-testiranje

# Getting started

Development Kit: **JAVA JDK**
Test Framework: **TestNG** (The other popular test runner framework is JUnit)
IDE: **IntelliJ Community Edition**
JDK: **Java 17**
Build System: **Maven**
Local Server: **JSON SERVER**

# How to run tests

1. git clone https://github.com/petravuica/Projekt-testiranje.git

2. cd Tests

3. open IntelliJ Community Edition and import the project

4. Reload project in IntelliJ by Maven

5. Check Node.js and npm version with commands **node -v**, **npm -v**

6. If they are not downloaded, install them from the following link https://nodejs.org/en/download/

7. Check json version with command **json-server --version**, if it is not downloaded, install it with this command **npm install -g json-server@0.16.3**

8. Make a backup copy of the database -> **copy db.json db_backup.json**

9. Start json-server -> **json-server --port 7000 --routes routes.json --watch db.json**

10. Open http://localhost:7000 in your browser

11. Right click on **textng.xml** file in IntelliJ and run the file or open test package, restapi package and RestApiTests class and run it.
