package com.dejim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import webapi.Raml10;
import webapi.WebApiBaseUnit;
import webapi.Oas30;

public final class RAMLMerge {
    private RAMLMerge() {
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        String file = args[0];
        //String file = "/Users/djuang/Desktop/prism-process-api/prism-process-api.raml";
        String basePath = new File(file).getParent();

        // Output RAML 1.0 YAML
        String raml10 = doInclude(file, basePath, "");
        System.out.println(raml10);

        // Output OAS 3.0 JSON
        WebApiBaseUnit result = Raml10.parse(raml10).get();
        final String output = Oas30.generateString(result).get();
        System.out.println(output);
    }

    private static String doInclude(String file, String basePath, String tabSpacing) throws IOException {
        String contents = readFile(file, tabSpacing);

        Pattern pattern = Pattern.compile("(([\\s\\t]*)([a-z0-9_\\/\\-]+):[\\s]+\\!include ([^\\s]+))",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(contents);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String property = matcher.group(3);
            String spacing = matcher.group(2);
            String subFile = matcher.group(4);
            if (!subFile.matches("^((https?://)|/)")) {
                subFile = basePath + "/" + subFile;
            }
            int i = 0;
            String cap = ": | \n";
            String subContent = doInclude(subFile, basePath, spacing + "    ");
            String[] subLines = subContent.split("\n");
            while (i < subLines.length && subLines[i].matches("^\\s*$")) {
                i++;
            }
            if (subLines[i].indexOf(':') != -1 && subLines[i].matches("(:\\s*('|\")(.+)('|\"))*")) {
                cap = ":\n";
            }
            matcher.appendReplacement(sb, spacing + property + cap + subContent);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String readFile(String file, String spacing) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(spacing).append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }
}
