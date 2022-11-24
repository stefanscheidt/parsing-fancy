package com.tyro.techtalk.parsingfancy

import com.github.h0tk3y.betterParse.grammar.tryParseToEnd
import com.github.h0tk3y.betterParse.parser.ErrorResult
import com.github.h0tk3y.betterParse.parser.Parsed
import com.github.h0tk3y.betterParse.parser.tryParseToEnd
import com.tyro.techtalk.parsingfancy.threatmodel.Scenario
import com.tyro.techtalk.parsingfancy.threatmodel.Target
import com.tyro.techtalk.parsingfancy.threatmodel.ThreadModel
import com.tyro.techtalk.parsingfancy.threatmodel.Threat
import io.kotest.assertions.fail
import io.kotest.matchers.collections.contain
import io.kotest.matchers.collections.containsInOrder
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ThreatModelParserTest {
    private val targetFragment =
        """Targets:
          |    Equifax is undefended
          |    Tyro is defended by types
          |    Earth is defended by [TheDoctor, CaptainPlanet]
          """.trimMargin()

    private val threatFragment =
        """Threats:
          |    GlobalWarming has no weaknesses
          |    JavaScript is weak against types
          |    TheMaster is weak against [TheDoctor, UntemperedSchism]
          """.trimMargin()

    private val scenarioFragment =
        """Scenarios:
          |    TheMaster vs Earth
          |    GlobalWarming vs Earth
          |    JavaScript vs Tyro
          """.trimMargin()

    private val threadModelAsString =
        """$targetFragment
          |
          |$threatFragment
          |
          |$scenarioFragment
          """.trimMargin()

    private val tokenizer = ThreatModelParser.tokenizer

    @Test
    fun `print input`() {
        println(threadModelAsString)
    }

    @Test
    @Disabled
    fun `the identifier parser should parse a single identifier as a list`() {
        when (val result = ThreatModelParser.identifierList.tryParseToEnd(tokenizer.tokenize("abc"), 0)) {
            is Parsed -> {
                result.value should contain("abc")
            }

            is ErrorResult -> {
                fail("Could not parse identifiers: $result")
            }
        }
    }

    @Test
    @Disabled
    fun `the identifier parser should parse a command separated list of tokens`() {
        when (val result = ThreatModelParser.identifierList.tryParseToEnd(tokenizer.tokenize("[abc, def]"), 0)) {
            is Parsed -> {
                result.value should containsInOrder("abc", "def")
            }

            is ErrorResult -> {
                fail("Could not parse identifiers: $result")
            }
        }
    }

    @Test
    @Disabled
    fun `the target parser should parse a whitespace separated list of targets`() {
        when (val result = ThreatModelParser.targetSection.tryParseToEnd(tokenizer.tokenize(targetFragment), 0)) {
            is Parsed -> {
                val targets = result.value

                targets should contain(Target("Equifax"))
                targets should contain(Target("Tyro", "types"))
                targets should contain(Target("Earth", "TheDoctor", "CaptainPlanet"))
            }

            is ErrorResult -> {
                fail("Could not parse targets: $result")
            }
        }
    }

    @Test
    @Disabled
    fun `the threat parser should parse a whitespace separated list of threats`() {
        when (val result = ThreatModelParser.threatSection.tryParseToEnd(tokenizer.tokenize(threatFragment), 0)) {
            is Parsed -> {
                val threats = result.value

                threats should contain(Threat("GlobalWarming"))
                threats should contain(Threat("JavaScript", "types"))
                threats should contain(Threat("TheMaster", "TheDoctor", "UntemperedSchism"))
            }

            is ErrorResult -> {
                fail("Could not parse threats: $result")
            }
        }
    }

    @Test
    @Disabled
    fun `the scenario parser should parse a whitespace separated list of scenarios`() {
        when (val result = ThreatModelParser.scenarioSection.tryParseToEnd(tokenizer.tokenize(scenarioFragment), 0)) {
            is Parsed -> {
                val scenarios = result.value
                scenarios should contain(Scenario("TheMaster", "Earth"))
                scenarios should contain(Scenario("GlobalWarming", "Earth"))
                scenarios should contain(Scenario("JavaScript", "Tyro"))
            }

            is ErrorResult -> {
                fail("Could not parse scenarios: $result")
            }
        }
    }

    @Test
    @Disabled
    fun `the threat model grammar should parse threat model`() {
        when (val result = ThreatModelParser.tryParseToEnd(threadModelAsString)) {
            is Parsed -> {
                result.value shouldBe ThreadModel(
                    targets = setOf(
                        Target("Equifax"),
                        Target("Tyro", "types"),
                        Target("Earth", "TheDoctor", "CaptainPlanet"),
                    ),
                    threads = setOf(
                        Threat("GlobalWarming"),
                        Threat("JavaScript", "types"),
                        Threat("TheMaster", "TheDoctor", "UntemperedSchism"),
                    ),
                    scenarios = setOf(
                        Scenario("TheMaster", "Earth"),
                        Scenario("GlobalWarming", "Earth"),
                        Scenario("JavaScript", "Tyro")
                    )
                )
            }

            is ErrorResult -> {
                fail("Could not parse thread model: $result")
            }
        }

    }
}
