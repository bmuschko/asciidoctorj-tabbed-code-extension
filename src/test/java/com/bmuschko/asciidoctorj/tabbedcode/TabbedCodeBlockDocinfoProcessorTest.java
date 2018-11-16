package com.bmuschko.asciidoctorj.tabbedcode;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TabbedCodeBlockDocinfoProcessorTest {

    @Disabled
    @Test
    void canCreatedTabbedCodeBlockWithDefaults() {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.javaExtensionRegistry().docinfoProcessor(TabbedCodeBlockDocinfoProcessor.class);
        OptionsBuilder optionsBuilder = OptionsBuilder.options().headerFooter(true);
        String html = asciidoctor.convert(getAsciiDoc(), optionsBuilder);
        System.out.println("----> " + html);

        Document doc = Jsoup.parse(html, "UTF-8");
        Element head = doc.getElementsByTag("head").first();
        Elements scripts = head.getElementsByTag("script");
        assertTrue(scripts.contains("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/zepto/1.2.0/zepto.min.js\"></script>"));
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
}

