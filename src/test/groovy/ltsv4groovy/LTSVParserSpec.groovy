package ltsv4groovy;

import spock.lang.Specification
import spock.lang.Unroll

class LTSVParserSpec extends Specification {

    @Unroll
    def "should #line parse into #expected"() {
        expect:
        LTSV.parser.parseLine(line) == expected

        where:
        line                  | expected
        null                  | [:]
        ''                    | [:]
        '\t'                  | [:]
        ':'                   | [:]
        'hoge:foo'            | [hoge: 'foo']
        'hoge:foo\n'          | [hoge: 'foo']
        'hoge:foo\t'          | [hoge: 'foo']
        'hoge:foo\t\n'        | [hoge: 'foo']
        'hoge:foo\tbar\n'     | [hoge: 'foo']
        'hoge:foo\tbar:\n'    | [hoge: 'foo', bar: '']
        'hoge:foo\tbar:baz\n' | [hoge: 'foo', bar: 'baz']
    }

}