package com.bmuschko.asciidoctorj.tabbedcode;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.DocinfoProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class TabbedCodeBlockDocinfoProcessor extends DocinfoProcessor {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String TABBED_CODE_CSS_FILE_PATH = "tabbed-code-css-path";
    private static final String TABBED_CODE_JS_FILE_PATH = "tabbed-code-js-path";

    @Override
    public String process(Document document) {
        if (document.isBasebackend("html")) {
            Map<String, Object> attributes = document.getAttributes();
            URL cssURL = getURLForResource(getCssPath(attributes, TABBED_CODE_CSS_FILE_PATH, "/codeBlockSwitch.css"));
            URL javascriptURL = getURLForResource(getCssPath(attributes, TABBED_CODE_JS_FILE_PATH, "/codeBlockSwitch.js"));
            String css = readText(cssURL);
            String javascript = readText(javascriptURL);
            return modifyHeadHtml(css, javascript);
        }

        return null;
    }

    private String getCssPath(Map<String, Object> attributes, String attributeKey, String defaultPath) {
        if (attributes.containsKey(attributeKey)) {
            return (String) attributes.get(attributeKey);
        }

        return defaultPath;
    }

    private URL getURLForResource(String path) {
        return TabbedCodeBlockDocinfoProcessor.class.getResource(path);
    }

    private String readText(URL url) {
        StringBuilder text = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                text.append(currentLine);
                text.append(LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        return text.toString();
    }

    private String modifyHeadHtml(String css, String javascript) {
        StringBuilder htmlHead = new StringBuilder();
        htmlHead.append("<style>").append(LINE_SEPARATOR);
        htmlHead.append(css).append(LINE_SEPARATOR);
        htmlHead.append("</style>").append(LINE_SEPARATOR);
        htmlHead.append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/zepto/1.2.0/zepto.min.js\"></script>").append(LINE_SEPARATOR);
        htmlHead.append("<script type=\"text/javascript\">").append(LINE_SEPARATOR);
        htmlHead.append(javascript).append(LINE_SEPARATOR);
        htmlHead.append("</script>").append(LINE_SEPARATOR);
        return htmlHead.toString();
    }
}
