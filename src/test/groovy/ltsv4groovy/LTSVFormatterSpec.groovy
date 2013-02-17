package ltsv4groovy;

import spock.lang.Specification
import spock.lang.Unroll

class LTSVFormatterSpec extends Specification {

    @Unroll
    def "should format #line to #expected"() {

        expect:
        LTSV.formatter.formatLine(line) == expected

        where:
        line                      | expected
        null                      | ''
        [:]                       | ''
        [hoge: 'foo', bar: 'baz'] | 'hoge:foo\tbar:baz'
    }

    @Unroll
    def "should format #lines to #expected"() {

        given:
        StringWriter writer = new StringWriter()

        when:
        LTSV.formatter.formatLines(lines, writer)

        then:
        writer.toString() == expected

        where:
        lines                         | expected
        null                          | ''
        []                            | ''
        [[hoge: 'foo', bar: 'baz']]   | 'hoge:foo\tbar:baz\n'
        [[hoge: 'foo'], [bar: 'baz']] | """hoge:foo
bar:baz
"""
    }

    @Unroll
    def "should format with stream #lines to #expected"() {
        given:
        OutputStream stream = new ByteArrayOutputStream()

        when:
        LTSV.formatter.formatLines(lines, stream)

        then:
        new String(stream.toByteArray()) == expected

        where:
        lines                         | expected
        null                          | ''
        []                            | ''
        [[hoge: 'foo', bar: 'baz']]   | 'hoge:foo\tbar:baz\n'
        [[hoge: 'foo'], [bar: 'baz']] | """hoge:foo
bar:baz
"""

    }

}