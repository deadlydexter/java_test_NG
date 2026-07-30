# Java 17 Development and CI Setup

This guide standardizes the Java version used by:

- Windows and the command line
- Eclipse
- The Eclipse project
- Maven
- GitHub Actions

The project standard is **Java 17**.

## Why all environments should match

Using Java 17 everywhere reduces differences between local development and CI. A test that compiles in Eclipse should compile the same way when Maven runs locally and when GitHub Actions runs remotely.

The four important settings are:

| Environment | Expected version |
| --- | --- |
| Installed JDK | 17 |
| Eclipse default JDK | 17 |
| Maven compiler release | 17 |
| GitHub Actions JDK | 17 |

## 1. Install JDK 17 on Windows

Download Eclipse Temurin JDK 17:

<https://adoptium.net/temurin/releases/?version=17>

Choose:

- Operating system: Windows
- Architecture: x64
- Package type: JDK
- JVM: HotSpot
- Installer: MSI

During installation, enable the options to add Java to `PATH` and set `JAVA_HOME`.

Close existing terminals, open a new Command Prompt, and run:

```bat
java -version
javac -version
echo %JAVA_HOME%
```

Both version commands should report Java 17. `JAVA_HOME` should point to the JDK 17 installation directory, not a JRE directory.

Example:

```text
C:\Program Files\Eclipse Adoptium\jdk-17...
```

## 2. Configure Eclipse to use JDK 17

1. Open **Window > Preferences**.
2. Select **Java > Installed JREs**.
3. Click **Add**.
4. Select **Standard VM**, and click **Next**.
5. For **JRE home**, select the installed JDK 17 directory.
6. Click **Finish**.
7. Select the checkbox beside JDK 17 to make it the workspace default.
8. Click **Apply and Close**.

Although Eclipse labels the page "Installed JREs," select the full **JDK**, not a standalone JRE.

## 3. Configure the Eclipse project

### Set the JRE System Library

1. Right-click the project and select **Properties**.
2. Open **Java Build Path > Libraries**.
3. Remove the old JRE System Library if it points to Java 8 or Java 12.
4. Click **Add Library > JRE System Library > Next**.
5. Select either:
   - **Workspace default JRE**, if the default is JDK 17; or
   - **Execution environment: JavaSE-17**.
6. Click **Finish**, then **Apply and Close**.

### Set compiler compliance

1. Right-click the project and select **Properties**.
2. Open **Java Compiler**.
3. Enable project-specific settings if necessary.
4. Set **Compiler compliance level** to `17`.
5. Apply the change and allow Eclipse to rebuild the project.

### Verify `.classpath`

If `.classpath` is tracked in Git, its JRE entry should be portable:

```xml
<classpathentry kind="con"
    path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-17"/>
```

It should not contain a developer-specific absolute path such as:

```text
C:\Program Files\Eclipse Adoptium\...
```

Commit `.classpath` when it contains the portable `JavaSE-17` setting.

## 4. Configure Maven

Use these properties in `pom.xml`:

```xml
<properties>
    <maven.compiler.release>17</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>
```

Remove older compiler settings such as:

```xml
<maven.compiler.source>1.8</maven.compiler.source>
<maven.compiler.target>1.8</maven.compiler.target>
```

The `release` setting controls both the Java language level and the Java API level used for compilation.

### Verify the Java used by Maven

From the project directory, run:

```bat
mvn -version
```

The output should show:

```text
Java version: 17
```

If `java -version` reports 17 but `mvn -version` reports another version, correct `JAVA_HOME`, restart the terminal, and restart Eclipse.

### Refresh and test in Eclipse

1. Right-click the project.
2. Select **Maven > Update Project**.
3. Select the project.
4. Enable **Force Update of Snapshots/Releases** when dependencies need refreshing.
5. Click **OK**.
6. Select **Project > Clean**.
7. Right-click the project and select **Run As > Maven test**.

You can also test from a terminal:

```bat
mvn clean test
```

## 5. Configure GitHub Actions

The workflow file should be stored under:

```text
.github/workflows/maven.yml
```

Example:

```yaml
name: Java CI with Maven

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven

      - name: Display Java and Maven versions
        run: |
          java -version
          javac -version
          mvn -version

      - name: Install Google Chrome
        run: |
          chmod +x ./scripts/InstallChrome.sh
          ./scripts/InstallChrome.sh

      - name: Build and run tests
        run: mvn clean test
```

The version-display step makes Java configuration problems visible in the Actions log.

If `testng.xml` is already configured through `maven-surefire-plugin` in `pom.xml`, `mvn clean test` will use it automatically.

## 6. Configure Selenium for GitHub Actions

GitHub-hosted runners do not have a visible desktop. Configure Chrome to run headlessly:

```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless=new");
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");

driver = new ChromeDriver(options);
```

If the same test must run visibly on a laptop and headlessly in CI, control headless mode through a system property or environment variable instead of maintaining two tests.

## 7. Verify the complete setup

### Local verification

Run:

```bat
java -version
javac -version
mvn -version
mvn clean test
```

All Java version output should show 17, and the Maven build should pass.

### Eclipse verification

Confirm:

- **Window > Preferences > Java > Installed JREs** has JDK 17 selected.
- **Project > Properties > Java Build Path** uses JavaSE-17.
- **Project > Properties > Java Compiler** uses compliance level 17.
- **Run Configurations > JRE** uses the project or workspace JDK 17.

### GitHub Actions verification

1. Commit and push the changes.
2. Open the repository on GitHub.
3. Select the **Actions** tab.
4. Open the latest workflow run.
5. Inspect **Display Java and Maven versions**.
6. Confirm Java and Maven use Java 17.
7. Confirm **Build and run tests** passes.

## 8. Files to commit

Normally commit:

- `pom.xml`
- `.github/workflows/maven.yml`
- `.classpath`, when it contains the portable `JavaSE-17` setting
- Test source changes
- `testng.xml`
- This setup guide

Do not commit:

- `target/`
- `.metadata/`
- IDE caches
- Local logs and reports
- Personal access tokens
- Passwords, API keys, or other secrets

Recommended `.gitignore` entries:

```gitignore
target/
.metadata/
test-output/
*.log
```

## 9. GitHub authentication note

A token used from Eclipse needs:

- **Contents: Read and write** to push project files.
- **Workflows: Read and write** to create or update files under `.github/workflows/`.

Never store a personal access token in source code, `pom.xml`, workflow YAML, or Git history.

## 10. Common errors

### `class file has wrong version 55.0, should be 52.0`

Java 8 is attempting to read a class compiled for Java 11. Ensure Eclipse, Maven, and GitHub Actions use JDK 17.

### `No compiler is provided in this environment`

Maven is running with a JRE rather than a JDK. Check:

```bat
javac -version
mvn -version
```

Both must resolve through JDK 17.

### Eclipse still uses the previous Java version

Restart Eclipse after changing `JAVA_HOME`. Then check the JRE selected in the applicable Eclipse **Run Configuration**, because a saved run configuration can override the workspace default.

### GitHub Actions still uses Java 8

Confirm that the workflow uses:

```yaml
uses: actions/setup-java@v4
```

with:

```yaml
distribution: temurin
java-version: '17'
```

Also inspect the version-display step in the workflow log.

## Final consistency checklist

- [ ] JDK 17 is installed locally.
- [ ] `JAVA_HOME` points to JDK 17.
- [ ] `java -version` reports 17.
- [ ] `javac -version` reports 17.
- [ ] `mvn -version` reports Java 17.
- [ ] Eclipse workspace default is JDK 17.
- [ ] Eclipse project library is JavaSE-17.
- [ ] Eclipse compiler compliance is 17.
- [ ] `pom.xml` uses `<maven.compiler.release>17</maven.compiler.release>`.
- [ ] GitHub Actions uses Temurin JDK 17.
- [ ] Local `mvn clean test` passes.
- [ ] The GitHub Actions build passes.

