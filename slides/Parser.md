theme: Simple

# Parsing Fancy

## Parser Combinators in Kotlin

---

# Credits

This is an updated and slightly modified version of [parsing-fancy_kotlin](https://github.com/sigerber/parsing-fancy_kotlin) by [Simon Gerber](https://github.com/sigerber).

See also the YouTube talk [Parsing Fancy - Parser Combinators in Kotlin](https://www.youtube.com/watch?v=lHRC84sc4P0) by Simon.

---

# Source Code

The code for this talk can be found [on GitHub](https://github.com/stefanscheidt/parsing-fancy).

---

# The Example - Input

```
    Targets:
        Equifax is undefended
        Tyro is defended by types
        Earth is defended by [TheDoctor, CaptainPlanet]
    
    Threats:
        GlobalWarming has no weaknesses
        JavaScript is weak against types
        TheMaster is weak against [TheDoctor, UntemperedSchism]
    
    Scenarios:
        TheMaster vs Earth
        GlobalWarming vs Earth
        JavaScript vs Tyro
```

---

# The Example - Model

```kotlin
    data class CounterMeasure constructor(
        val name: String
    )
    
    data class Target(
        val name: String,
        val defendedBy: Set<CounterMeasure>
    )
    
    data class Threat(
        val name: String,
        val counteredBy: Set<CounterMeasure>
    )
    
    data class ThreadModel(
        val targets: Set<Target>,
        val threads: Set<Threat>,
        val scenarios: Set<Scenario>,
    )
```

---

# Our Goal

```kotlin
fun tryParseThreadModel(input: String): ParseResult<ThreadModel>
```

---

# Parser in Kotlin better-parse[^1]

```kotlin
interface Parser<out T> {
    fun tryParse(tokens: TokenMatchesSequence, fromPosition: Int): ParseResult<T>
}
```

[^1]: [github.com/h0tk3y/better-parse](https://github.com/h0tk3y/better-parse/blob/master/src/commonMain/kotlin/com/github/h0tk3y/betterParse/parser/Parser.kt)

---

# Tokens in Kotlin better-parse[^2]

> Represents a basic detectable part of the input that may be ignored during parsing. Parses to `TokenMatch`.

Token types: `LiteralToken`, `RegexToken`

[^2]: [github.com/h0tk3y/better-parse](https://github.com/h0tk3y/better-parse/blob/master/src/commonMain/kotlin/com/github/h0tk3y/betterParse/lexer/Token.kt)

---

# Tokenizer in Kotlin better-parse[^3]

```kotlin
interface Tokenizer {
    fun tokenize(input: String): TokenMatchesSequence
}
```

[^3]: [github.com/h0tk3y/better-parse](https://github.com/h0tk3y/better-parse/blob/master/src/commonMain/kotlin/com/github/h0tk3y/betterParse/lexer/Tokenizer.kt)

---

# Grammar in Kotlin better-parse[^4]

> A language grammar represented by a list of tokens and one or more parsers, with one specific root parser that accepts the words of this grammar.

```kotlin
fun <T> Grammar<T>.tryParseToEnd(input: String): ParseResult<T> =
    rootParser.tryParseToEnd(tokenizer.tokenize(input), 0)
```

[^4]: [github.com/h0tk3y/better-parse](https://github.com/h0tk3y/better-parse/blob/master/src/commonMain/kotlin/com/github/h0tk3y/betterParse/grammar/Grammar.kt)

---

# Combinators in Kotlin better-parse[^5]

`map`:

```kotlin
val a: Parser<TokenMatch> = regexToken("a+")

// returns the matched text from the input sequence
val aText: Parser<String> = a map { it.text }
```

[^5]: [github.com/h0tk3y/better-parse](https://github.com/h0tk3y/better-parse#combinators)

---

# Combinators in Kotlin better-parse[^5]

`and`, `skip`:

```kotlin
val a: Parser<A> = ...
val b: Parser<B> = ...

val aAndB: Parser<Tuple2<A, B>> = a and b

val bAndBAndA: Parser<Tuple3<B, B, A>> = b and b and a

val bbWithoutA: Parser<Tuple2<B, B>> = skip(a) and b and skip(a) and b and skip(a)
```

[^5]: [github.com/h0tk3y/better-parse](https://github.com/h0tk3y/better-parse#combinators)

---

# Combinators in Kotlin better-parse[^5]

and more:

* `or`
* `zeroOrMore(...)`, `oneOrMore(...)`
* `N times`, `N timesOrMore`, `N..M times`
* `separated(term, separator)`, `separatedTerms(term, separator)`
* `leftAssociative(...)`, `rightAssociative(...)`

[^5]: [github.com/h0tk3y/better-parse](https://github.com/h0tk3y/better-parse#combinators)

---

# Demo
