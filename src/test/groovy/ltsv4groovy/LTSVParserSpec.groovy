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

    @Unroll
    def "should log #line parse into #expected"() {
        expect:
        LTSV.parser.parseLine(line) == expected

        where:
        line                                                                                    | expected
        "time:28/Feb/2013:12:00:00 +0900\thost:192.168.0.1\treq:GET /list HTTP/1.1\tstatus:200" | [time: '28/Feb/2013:12:00:00 +0900', host: '192.168.0.1', req: 'GET /list HTTP/1.1', status: '200']
    }

    @Unroll
    def "should #line parse into #expected with reader"() {
        expect:
        LTSV.parser.parseLines(new StringReader(line)) == expected

        where:
        line                  | expected
        ''                    | []
        '\t'                  | [[:]]
        ':'                   | [[:]]
        'hoge:foo'            | [[hoge: 'foo']]
        'hoge:foo\n'          | [[hoge: 'foo']]
        'hoge:foo\t'          | [[hoge: 'foo']]
        'hoge:foo\t\n'        | [[hoge: 'foo']]
        'hoge:foo\tbar\n'     | [[hoge: 'foo']]
        'hoge:foo\tbar:\n'    | [[hoge: 'foo', bar: '']]
        'hoge:foo\tbar:baz\n' | [[hoge: 'foo', bar: 'baz']]
        """hoge:foo\tbar:baz
fuga:fumo\tpiyo:homu
"""                | [[hoge: 'foo', bar: 'baz'], [fuga: 'fumo', piyo: 'homu']]
    }
}