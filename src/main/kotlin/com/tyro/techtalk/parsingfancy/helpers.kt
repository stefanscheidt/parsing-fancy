package com.tyro.techtalk.parsingfancy

import com.github.h0tk3y.betterParse.lexer.TokenMatchesSequence
import com.github.h0tk3y.betterParse.parser.NoMatchingToken
import com.github.h0tk3y.betterParse.parser.ParseResult
import com.github.h0tk3y.betterParse.parser.Parser

fun <T> todo() = object : Parser<T> {
    override fun tryParse(tokens: TokenMatchesSequence, fromPosition: Int): ParseResult<T> {
        return NoMatchingToken(tokens.first())
    }
}
