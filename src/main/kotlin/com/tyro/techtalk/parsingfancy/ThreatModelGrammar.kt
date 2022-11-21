package com.tyro.techtalk.parsingfancy

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.asJust
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import com.tyro.techtalk.parsingfancy.threatmodel.CounterMeasure
import com.tyro.techtalk.parsingfancy.threatmodel.Scenario
import com.tyro.techtalk.parsingfancy.threatmodel.Target
import com.tyro.techtalk.parsingfancy.threatmodel.ThreadModel
import com.tyro.techtalk.parsingfancy.threatmodel.Threat

object ThreatModelParser : Grammar<ThreadModel>() {
    private val TARGET_HEADER by literalToken("Targets:")
    private val THREAT_HEADER by literalToken("Threats:")
    private val SCENARIO_HEADER by literalToken("Scenarios:")

    private val COMMA by literalToken(",")
    private val LBRACE by regexToken("\\[")
    private val RBRACE by literalToken("]")

    private val VERSUS by literalToken("vs")

    private val NO_DEFENCES by literalToken("is undefended")
    private val DEFENSES_PREFIX by literalToken("is defended by")

    private val NO_WEAKNESSES by literalToken("has no weaknesses")
    private val WEAKNESSES_PREFIX by literalToken("is weak against")

    private val IDENT by regexToken("\\w+")

    private val WHITESPACE by regexToken("\\s+", ignore = true)

    private val oneIdentifier: Parser<List<String>> =
        IDENT map { listOf(it.text) }
    private val manyIdentifiers: Parser<List<String>> =
        skip(LBRACE) and separatedTerms(IDENT, COMMA) and skip(RBRACE) map { terms ->
            terms.map { it.text }
        }
    val identifierList: Parser<List<String>> =
        oneIdentifier or manyIdentifiers

    private val noDefences: Parser<Set<CounterMeasure>> =
        NO_DEFENCES asJust emptySet()
    private val someDefences: Parser<Set<CounterMeasure>> =
        skip(DEFENSES_PREFIX) and identifierList map { names ->
            names.map(::CounterMeasure).toSet()
        }
    private val defences: Parser<Set<CounterMeasure>> =
        noDefences or someDefences
    private val target: Parser<Target> =
        IDENT and defences map { (tokenMatch, defences) ->
            Target(tokenMatch.text, defences)
        }
    val targetSection: Parser<Set<Target>> =
        skip(TARGET_HEADER) and separatedTerms(target, WHITESPACE) map {
            it.toSet()
        }

    private val noWeaknesses: Parser<Set<CounterMeasure>> =
        NO_WEAKNESSES asJust emptySet()
    private val someWeaknesses: Parser<Set<CounterMeasure>> =
        skip(WEAKNESSES_PREFIX) and identifierList map { names ->
            names.map(::CounterMeasure).toSet()
        }
    private val weaknesses: Parser<Set<CounterMeasure>> =
        noWeaknesses or someWeaknesses
    private val threat: Parser<Threat> =
        IDENT and weaknesses map { (tokenMatch, weakness) ->
            Threat(tokenMatch.text, weakness)
        }
    val threatSection: Parser<Set<Threat>> =
        skip(THREAT_HEADER) and separatedTerms(threat, WHITESPACE) map {
            it.toSet()
        }

    private val scenario: Parser<Scenario> =
        IDENT and skip(VERSUS) and IDENT map { (thread, target) ->
            Scenario(thread.text, target.text)
        }
    val scenarioSection: Parser<Set<Scenario>> =
        skip(SCENARIO_HEADER) and separatedTerms(scenario, WHITESPACE) map {
            it.toSet()
        }

    override val rootParser: Parser<ThreadModel> by
    targetSection and threatSection and scenarioSection map { (targets, threats, scenarios) ->
        ThreadModel(targets, threats, scenarios)
    }
}

