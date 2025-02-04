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

8. It creates a directory for json-server -> **C:\Users\user\AppData\Roaming\npm**
9. Create db.json file with starting data on that path **C:\Users\user\AppData\Roaming\npm\db.json**
   (db.json file can be found in the project folder)

10. Create routes.json file with starting routes on that path **C:\Users\user\AppData\Roaming\npm\routes.json**
   (routes.json file can be found in the project folder)

10. Make a backup copy of the database -> **copy db.json db_backup.json**

11. Start json-server -> **json-server --port 7000 --routes routes.json --watch db.json**

12. Open http://localhost:7000 in your browser

13. Right click on **textng.xml** file in IntelliJ and run the file or open test package, restapi package and RestApiTests class and run it.
