package com.alexholmes.json.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


public class PartitionedJsonParserNoSeekTest {

    @Test
    public void testNoSeek() throws IOException {
        File testsDir = new File("src/test/resources/parser-tests/noseek");
        File[] jsonFiles = testsDir.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                return s.endsWith(".json");
            }
        });

        for(File jsonFile: jsonFiles) {
            runTest(jsonFile);
        }
    }

    public void runTest(final File jsonFile) throws IOException {
        InputStream jsonInputStream = new FileInputStream(jsonFile);

        try {
            PartitionedJsonParser parser = new PartitionedJsonParser(jsonInputStream);


            File[] jsonOjbectFiles = jsonFile.getParentFile().listFiles(new FilenameFilter() {
                public boolean accept(File file, String s) {
                    return s.contains(jsonFile.getName()) && s.contains("expected");
                }
            });

            for(File jsonObjectFile: jsonOjbectFiles) {
                String expected = trimWhitespaces(FileUtils.readFileToString(jsonObjectFile));
                String result = parser.nextObjectContainingMember("name");
                assertNotNull(jsonFile.getName() + "/" + jsonObjectFile.getName(), result);
                assertEquals(jsonFile.getName() + "/" + jsonObjectFile.getName(), expected, trimWhitespaces(result));
                System.out.println("File " + jsonFile.getName() + "/" + jsonObjectFile.getName() + " passed");
            }

        } finally {
            IOUtils.closeQuietly(jsonInputStream);
        }
    }

    public String trimWhitespaces(String s) {
        return s.replaceAll("[\\n\\t\\r \\t]+", " ").trim();
    }
}
