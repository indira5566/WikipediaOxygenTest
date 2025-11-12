<<<<<<< HEAD
# Wikipedia Oxygen Test

A Maven project with Selenium and JUnit5 for testing Wikipedia articles.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser installed

## Project Structure

```
WikipediaOxygenTest/
├── pom.xml
├── README.md
└── src/
    └── test/
        └── java/
            └── com/
                └── example/
                    └── WikipediaOxygenTest.java
```

## Running Tests

### From IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. IntelliJ should automatically detect the Maven project and download dependencies
3. Right-click on `WikipediaOxygenTest.java` and select "Run 'WikipediaOxygenTest'"
4. Or run individual test methods by right-clicking on them

### From Terminal

Run all tests:
```bash
mvn test
```

Run a specific test:
```bash
mvn test -Dtest=WikipediaOxygenTest#testAtmosphereContainsCorrectOxygenPercentage
```

## Tests

1. **testAtmosphereContainsCorrectOxygenPercentage**: 
   - Opens Wikipedia
   - Searches for "Земля" (Earth in Russian)
   - Verifies the article contains "20,95% кислорода" (20.95% oxygen)

2. **testAtmosphereDoesNotContainIncorrectOxygenPercentage**:
   - Opens Wikipedia
   - Searches for "Земля"
   - Verifies the article does NOT contain "25% кислорода"

## Dependencies

- Selenium WebDriver 4.15.0
- JUnit 5.10.0
- WebDriverManager 5.6.2 (automatically manages ChromeDriver)



=======
# WikipediaOxygenTest
Тестовое задание по автоматизации тестирования с использованием Selenium для проверки содержания кислорода в атмосфере Земли.
>>>>>>> 17b8360311cb19456b329c540ec65445eac02303
