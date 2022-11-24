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

    val identifierList: Parser<List<String>> = todo()

    val targetSection: Parser<Set<Target>> = todo()

    val threatSection: Parser<Set<Threat>> = todo()

    val scenarioSection: Parser<Set<Scenario>> = todo()

    override val rootParser: Parser<ThreadModel> = todo()
}

