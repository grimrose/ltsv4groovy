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

    @Unroll
    def "should #line parse into #expected with stream"() {
        expect:
        LTSV.parser.parseLines(new ByteArrayInputStream(line.bytes)) == expected

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

    static File resources = new File(".", 'src/test/resources')

    @Unroll
    def "should #file parse into #expected"() {
        given:

        expect:
        LTSV.parser.parseLines(file) == expected

        where:
        file                        | charset | expected
        new File(resources, 'test.ltsv') | _       | [[a: '1', b: '2', c: '3'], [a: '4', b: '5', c: '6'], [a: '7', b: '8', c: '9']]
        new File(resources, 'hoge.ltsv') | 'UTF-8' | [[hoge: 'foo', bar: 'baz'], [perl: '5.17.8', ruby: '2.0', python: '2.6'], [sushi: '寿司', tennpura: '天ぷら', ramen: 'ラーメン', gyoza: '餃子']]
    }

    @Unroll
    def "should #filepath parse into #expected"() {
        given:

        expect:
        LTSV.parser.parseLines(filepath) == expected

        where:
        filepath                                 | charset | expected
        new File(resources, 'test.ltsv').absolutePath | _       | [[a: '1', b: '2', c: '3'], [a: '4', b: '5', c: '6'], [a: '7', b: '8', c: '9']]
        new File(resources, 'hoge.ltsv').absolutePath | 'UTF-8' | [[hoge: 'foo', bar: 'baz'], [perl: '5.17.8', ruby: '2.0', python: '2.6'], [sushi: '寿司', tennpura: '天ぷら', ramen: 'ラーメン', gyoza: '餃子']]
    }

}