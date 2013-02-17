package ltsv4groovy

import org.junit.Rule
import org.junit.rules.TemporaryFolder;
import spock.lang.Specification
import spock.lang.Unroll

class LTSVFormatterSpec extends Specification {

    @Unroll
    def "should #line is formatted into #expected"() {

        expect:
        LTSV.formatter.formatLine(line) == expected

        where:
        line                      | expected
        null                      | ''
        [:]                       | ''
        [hoge: 'foo', bar: 'baz'] | 'hoge:foo\tbar:baz'
    }

    @Unroll
    def "should #lines is formatted into #expected"() {

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
    def "should #lines is formatted into #expected using stream"() {
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

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Unroll
    def "should #lines is formatted into #expected using file"() {
        given:
        def file = temporaryFolder.newFile()

        when:
        LTSV.formatter.formatLines(lines, file)

        then:
        file.text == expected

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
    def "should #lines is formatted into #expected using file path"() {
        given:
        def path = temporaryFolder.newFile().absolutePath

        when:
        LTSV.formatter.formatLines(lines, path)

        then:
        new File(path).text == expected

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