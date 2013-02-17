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
                "${it.key}${LTSV.SEPARATOR}${it.value}"
            }.join LTSV.TAB
        }

        void formatLines(List<Map<String, String>> lines = [], Writer writer) {
            if (!writer) return

            new BufferedWriter(writer).withPrintWriter { pw ->
                for (line in lines) {
                    pw.println formatLine(line)
                }
            }
        }

        void formatLines(List<Map<String, String>> lines, OutputStream outputStream, String charset = DEFAULT_CHARSET) {
            if (!outputStream) return
            outputStream.withWriter(charset) { writer ->
                formatLines(lines, writer)
            }
        }

    }

}
