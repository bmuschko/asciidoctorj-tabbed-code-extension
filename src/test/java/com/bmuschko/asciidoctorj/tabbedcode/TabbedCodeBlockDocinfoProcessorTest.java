package com.bmuschko.asciidoctorj.tabbedcode;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.TempDirectory;
import org.junitpioneer.jupiter.TempDirectory.TempDir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.bmuschko.asciidoctorj.tabbedcode.FileUtils.LINE_SEPARATOR;
import static com.bmuschko.asciidoctorj.tabbedcode.FileUtils.readFileContentsFromClasspath;
import static com.bmuschko.asciidoctorj.tabbedcode.TabbedCodeBlockDocinfoProcessor.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TabbedCodeBlockDocinfoProcessorTest {
    private static final String CUSTOM_CSS_FILE_PATH = "/customStyling.css";
    private static final String CUSTOM_JS_FILE_PATH = "/customBehavior.js";

    @Test
    @DisplayName("can create HTML tabbed code block with default settings")
    void canCreateHtmlTabbedCodeBlockWithDefaultsSettings() {
        String result = convert(createOptionBuilder());
        verifyConvertedHtml(result, DEFAULT_CSS_FILE_PATH, DEFAULT_JS_FILE_PATH);
    }

    @Test
    @DisplayName("can create HTML tabbed code block with custom settings")
    @ExtendWith(TempDirectory.class)
    void canCreateHtmlTabbedCodeBlockWithCustomSettings(@TempDir Path tempDir) {
        Path customCssPath = tempDir.resolve("customStyling.css");
        writeFile(customCssPath, "custom CSS");
        Path customJsPath = tempDir.resolve("customBehavior.js");
        writeFile(customJsPath, "custom JavaScript");
        addPathToClassloader(customCssPath.getParent());

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(TABBED_CODE_CSS_FILE_PATH_ATTRIBUTE, CUSTOM_CSS_FILE_PATH);
        attributes.put(TABBED_CODE_JS_FILE_PATH_ATTRIBUTE, CUSTOM_JS_FILE_PATH);
        OptionsBuilder optionsBuilder = createOptionBuilder().attributes(attributes);
        String result = convert(optionsBuilder);
        verifyConvertedHtml(result, CUSTOM_CSS_FILE_PATH, CUSTOM_JS_FILE_PATH);
    }

    @Test
    @DisplayName("can handle other backends than HTML")
    void canHandleNonHtmlBackend() {
        OptionsBuilder optionsBuilder = createOptionBuilder().backend("docbook");
        String result = convert(optionsBuilder);
        assertNotNull(result);
    }

    private static String convert(OptionsBuilder optionsBuilder) {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.javaExtensionRegistry().docinfoProcessor(TabbedCodeBlockDocinfoProcessor.class);
        return asciidoctor.convert(getAsciiDoc(), optionsBuilder);
    }

    private static OptionsBuilder createOptionBuilder() {
        return OptionsBuilder.options().headerFooter(true).safe(SafeMode.SERVER);
    }

    private static String getAsciiDoc() {
        StringBuilder adoc = new StringBuilder();
        adoc.append("= Hello World");
        adoc.append("\n");
        adoc.append("This is a test");
        adoc.append("\n");
        adoc.append("[source,groovy,indent=0,subs=\"verbatim,attributes\",role=\"primary\"]");
        adoc.append("\n");
        adoc.append(".Groovy");
        adoc.append("\n");
        adoc.append("----");
        adoc.append("\n");
        adoc.append("def s = 'My String'");
        adoc.append("\n");
        adoc.append("----");
        adoc.append("\n");
        adoc.append("[source,kotlin,indent=0,subs=\"verbatim,attributes\",role=\"secondary\"]");
        adoc.append("\n");
        adoc.append(".Kotlin");
        adoc.append("\n");
        adoc.append("----");
        adoc.append("\n");
        adoc.append("val s = \"My String\"");
        adoc.append("\n");
        adoc.append("----");
        return adoc.toString();
    }

    private static String expectedZepto() {
        return "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/zepto/1.2.0/zepto.min.js\"></script>";
    }

    private static String expectedCss(String path) {
        String cssInstructions = readFileContentsFromClasspath(path);
        StringBuilder css = new StringBuilder();
        css.append("<style>").append(LINE_SEPARATOR);
        css.append(cssInstructions).append(LINE_SEPARATOR);
        css.append("</style>");
        return css.toString();
    }

    private static String expectedJs(String path) {
        String jsInstructions = readFileContentsFromClasspath(path);
        StringBuilder js = new StringBuilder();
        js.append("<script type=\"text/javascript\">").append(LINE_SEPARATOR);
        js.append(jsInstructions).append(LINE_SEPARATOR);
        js.append("</script>");
        return js.toString();
    }

    private static Optional<Element> findElement(Elements elements, String expected) {
        return elements.stream()
                .filter(element -> expected.equals(element.toString()))
                .findAny();
    }

    private static void verifyConvertedHtml(String html, String cssFilePath, String jsFilePath) {
        Document doc = Jsoup.parse(html, "UTF-8");
        Element head = doc.getElementsByTag("head").first();
        Elements styles = doc.getElementsByTag("style");
        Elements scripts = head.getElementsByTag("script");
        Optional<Element> cssStyleElement = findElement(styles, expectedCss(cssFilePath));
        Optional<Element> zeptoScriptElement = findElement(scripts, expectedZepto());
        Optional<Element> jsScriptElement = findElement(scripts, expectedJs(jsFilePath));
        assertTrue(cssStyleElement.isPresent());
        assertTrue(zeptoScriptElement.isPresent());
        assertTrue(jsScriptElement.isPresent());
    }

    private void writeFile(Path path, String content) {
        try {
            Files.write(path, content.getBytes("UTF-8"));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addPathToClassloader(Path path) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), new Object[] { path.toUri().toURL() });
        } catch (NoSuchMethodException | MalformedURLException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

