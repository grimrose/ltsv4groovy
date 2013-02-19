/**
 *    Copyright 2013 grimrose
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package ltsv4groovy

/**
 * Manipulate LTSV
 *
 * @author grimrose
 */
class LTSV {

    /**
     * ¥t
     */
    static final String TAB = '\t'

    /**
     * ¥n
     */
    static final char LF = '\n'

    /**
     * ¥r
     */
    static final char CR = '\r'

    /**
     * :
     */
    static final String SEPARATOR = ':'

    /**
     * UTF-8
     */
    static final String DEFAULT_CHARSET = 'UTF-8'

    /**
     * @return formatter
     */
    static LTSVFormatter getFormatter() {
        new LTSVFormatter()
    }

    /**
     * formatter class
     */
    static class LTSVFormatter {

        /**
         * format
         * @param lineData
         * @return formatted data
         */
        String formatLine(Map<String, String> lineData) {
            if (!lineData) return ""
            lineData.collect { "${it.key}${SEPARATOR}${it.value}" }.join TAB
        }

        /**
         * format
         * @param lines
         * @param writer
         */
        void formatLines(List<Map<String, String>> lines, Writer writer) {
            new BufferedWriter(writer).withPrintWriter { pw ->
                (lines ?: []).each { line ->
                    pw.println formatLine(line)
                }
            }
        }

        /**
         * format
         * @param lines
         * @param outputStream
         * @param charset default charset is 'UTF-8'
         */
        void formatLines(List<Map<String, String>> lines, OutputStream outputStream, String charset = DEFAULT_CHARSET) {
            outputStream.withWriter(charset) { writer ->
                formatLines(lines, writer)
            }
        }

        /**
         * format
         * @param lines
         * @param file
         * @param charset default charset is 'UTF-8'
         */
        void formatLines(List<Map<String, String>> lines, File file, String charset = DEFAULT_CHARSET) {
            if (!file.exists()) {
                assert file.createNewFile()
            }
            formatLines(lines, new FileOutputStream(file, true), charset)
        }

        /**
         * format
         * @param lines
         * @param filePath
         * @param charset default charset is 'UTF-8'
         */
        void formatLines(List<Map<String, String>> lines, String filePath, String charset = DEFAULT_CHARSET) {
            formatLines(lines, new File(filePath), charset)
        }

    }

    /**
     * @return parser
     */
    static LTSVParser getParser() {
        new LTSVParser()
    }

    /**
     * parser class
     */
    static class LTSVParser {

        /**
         * parse
         * @param line
         * @return map
         */
        Map<String, String> parseLine(String line) {
            (line ?: '').tokenize(TAB).collectEntries { String string ->
                int first = (string ?: '').indexOf(SEPARATOR)
                if (first <= 0) return [:]

                String key = string.substring(0, first)
                String value = string.substring(first).replaceFirst(SEPARATOR, '').replaceAll(/$CR|$LF/, '')

                [("$key" as String): value]
            }
        }

        /**
         * parse
         * @param reader
         * @return line data
         */
        List<Map<String, String>> parseLines(Reader reader) {
            new BufferedReader(reader).readLines().collectNested { parseLine(it) }
        }

        /**
         * parse
         * @param inputStream
         * @param charset default charset is 'UTF-8'
         * @return result
         */
        List<Map<String, String>> parseLines(InputStream inputStream, String charset = DEFAULT_CHARSET) {
            inputStream.withReader(charset) { reader ->
                parseLines(reader)
            }
        }

        /**
         * parse
         * @param file
         * @param charset default charset is 'UTF-8'
         * @return result
         */
        List<Map<String, String>> parseLines(File file, String charset = DEFAULT_CHARSET) {
            parseLines(new FileInputStream(file), charset)
        }

        /**
         * parse
         * @param filePath
         * @param charset default charset is 'UTF-8'
         * @return result
         */
        List<Map<String, String>> parseLines(String filePath, String charset = DEFAULT_CHARSET) {
            parseLines(new File(filePath), charset)
        }

    }

}
