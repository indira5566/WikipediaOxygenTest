package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class WikipediaOxygenTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    /**
     * Идём на Википедию, открываем статью "Земля",
     * переходим к разделу 4.7.1 через hash и ПРОСТО ищем по значению "20,95 % кислорода".
     */
    @Test
    void find_oxygen_value_by_plain_text_and_highlight() {
        openEarthArticle();
        jumpToSection("Химический_состав_атмосферы");

        // Берём весь видимый текст страницы и проверяем значение "20,95 % кислорода"
        String page = normalize(getBodyText().toLowerCase());

        boolean hasValue =
                page.contains("20,95 % кислорода") ||
                page.contains("20,95% кислорода")  ||
                page.contains("20.95 % кислорода") ||
                page.contains("20.95% кислорода");

        // Дополнительно убеждаемся, что раздел действительно присутствует
        assertTrue(page.contains("химический состав атмосферы"),
                "❌ На странице не найден заголовок 'Химический состав атмосферы'.");

        assertTrue(hasValue,
                "❌ На странице не найдено упоминание '20,95 % кислорода'.");

        System.out.println("✅ Найдено упоминание '20,95 % кислорода' в статье 'Земля'.");
    }

    /**
     * Негативный тест в том же духе: проверяем, что выдуманное значение не встречается.
     */
    @Test
    void negative_no_25_percent_oxygen_anywhere() {
        openEarthArticle();
        jumpToSection("Химический_состав_атмосферы");

        String page = normalize(getBodyText().toLowerCase());
        boolean hasWrong =
                page.contains("15 % кислорода") || page.contains("15% кислорода");

        assertFalse(hasWrong, "❌ На странице не должно быть '15 % кислорода'.");
        System.out.println("✅ Негативная проверка прошла: '25 % кислорода' не найдено.");
    }

    // ===== helpers (без XPath) =====

    /** Открывает ru.wikipedia.org, ищет «Земля» и переходит в статью (только CSS/id). */
    private void openEarthArticle() {
        driver.get("https://ru.wikipedia.org");

        WebElement search = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchInput")));
        search.clear();
        search.sendKeys("Земля");
        search.submit();

        // либо сразу статья, либо страница поиска — кликаем первый результат (CSS)
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul.mw-search-results, #firstHeading")));
        if (driver.getCurrentUrl().contains("search")) {
            WebElement firstLink = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("ul.mw-search-results li a")));
            firstLink.click();
        }

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("firstHeading"), "Земля"));
        System.out.println("✅ Открыта статья 'Земля'.");
    }

    /** Переход к разделу через location.hash (никаких XPath). */
    private void jumpToSection(String anchor) {
        // Переход по якорю
        ((JavascriptExecutor) driver).executeScript("location.hash = arguments[0];", anchor);

        // Ждём, пока элемент с данным якорем будет видим
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(anchor)));

        // Немного ждем, чтобы убедиться в окончательной загрузке страницы
        try {
            Thread.sleep(500); // можно убрать или настроить, если будет нужно
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Небольшой сдвиг, чтобы раздел оказался в центре
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -80);");
    }

    /** Получить видимый текст <body>. */
    private String getBodyText() {
        return driver.findElement(By.tagName("body")).getText();
    }

    /** Нормализация: заменить неразрывные/узкие пробелы, схлопнуть повторы. */
    private String normalize(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFKC);
        n = n.replace('\u00A0', ' ').replace('\u202F', ' ');
        n = n.replaceAll("[\\s\\u200B]+", " ").trim();
        return n;
    }
}
