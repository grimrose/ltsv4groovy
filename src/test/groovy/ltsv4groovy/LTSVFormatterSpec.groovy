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

}