package ltsv4groovy

class LTSV {

    static final String TAB = '\t'

    static final char LF = '\n'

    static final char CR = '\r'

    static final String SEPARATOR = ':'

    static final String DEFAULT_CHARSET = 'UTF-8'

    static LTSVFormatter getFormatter() {
        new LTSVFormatter()
    }

    static class LTSVFormatter {

        String formatLine(Map<String, String> line) {
            if (!line) return ""
            line.collect {
                "${it.key}${SEPARATOR}${it.value}"
            }.join TAB
        }

        void formatLines(List<Map<String, String>> lines = [], Writer writer) {
            new BufferedWriter(writer).withPrintWriter { pw ->
                for (line in lines) {
                    pw.println formatLine(line)
                }
            }
        }

        void formatLines(List<Map<String, String>> lines, OutputStream outputStream, String charset = DEFAULT_CHARSET) {
            outputStream.withWriter(charset) { writer ->
                formatLines(lines, writer)
            }
        }

        void formatLines(List<Map<String, String>> lines, File file, String charset = DEFAULT_CHARSET) {
            if (!file.exists()) {
                assert file.createNewFile()
            }
            formatLines(lines, new FileOutputStream(file, true), charset)
        }

        void formatLines(List<Map<String, String>> lines, String filePath, String charset = DEFAULT_CHARSET) {
            formatLines(lines, new File(filePath), charset)
        }

    }

    static LTSVParser getParser() {
        new LTSVParser()
    }

    static class LTSVParser {

        Map<String, String> parseLine(String line) {
            (line ?: '').tokenize(TAB).collectEntries { String string ->
                int first = (string ?: '').indexOf(SEPARATOR)
                if (first <= 0) return [:]

                String key = string.substring(0, first)
                String value = string.substring(first).replaceFirst(SEPARATOR, '').replaceAll(/$CR|$LF/, '')

                [("$key" as String): value]
            }
        }

    }

}
