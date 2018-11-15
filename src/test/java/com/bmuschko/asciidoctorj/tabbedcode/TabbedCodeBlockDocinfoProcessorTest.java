package com.bmuschko.asciidoctorj.tabbedcode;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.junit.jupiter.api.Test;

public class TabbedCodeBlockDocinfoProcessorTest {

    @Test
    void canCreatedTabbedCodeBlockWithDefaults() {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        Options options = OptionsBuilder.options().get();
        String html = asciidoctor.render(getAsciiDoc(), options);
        System.out.println("----> " + html);
    }

    private static String getAsciiDoc() {
        StringBuilder adoc = new StringBuilder();
        adoc.append("= Hello World");
        adoc.append("\n");
        adoc.append("This is a test");
        return adoc.toString();
    }
}

