package rule.dsl

import rule.LinguisticValue
import rule.LinguisticVariable
import rule.Rule
import rule.Statement

private val variables = mutableListOf<LinguisticVariable>()
private val values = mutableListOf<LinguisticValue>()
private val truthiness_list = mutableListOf<Boolean>()
private val conditions = mutableListOf<Statement>()
private val conclusions = mutableListOf<Statement>()

object DECLARE {

    @Suppress("FunctionName")
    infix fun IF(variable: LinguisticVariable): IsObject {
        variables.add(variable)
        return IsObject
    }

    fun clear() {
        variables.clear()
        values.clear()
        truthiness_list.clear()
        conditions.clear()
        conclusions.clear()
    }
}

object AndThenObject {
    private fun andThenAction(variable: LinguisticVariable): IsObject {
        variables.add(variable)
        return IsObject
    }

    @Suppress("FunctionName")
    infix fun AND(variable: LinguisticVariable): IsObject {
        return andThenAction(variable)
    }

    @Suppress("FunctionName")
    infix fun THEN(variable: LinguisticVariable): IsObject {
        return andThenAction(variable)
    }

    fun getRule(): Rule {
        for (i in 0 until variables.size) {
            conclusions.add(Statement(variables[i], values[i], truthiness_list[i]))
        }
        val rule = Rule(conditions.toSet(), conclusions.toSet())
        DECLARE.clear()
        return rule
    }
}

object IsObject {
    private fun isAction(value: LinguisticValue): AndThenObject {
        values.add(value)
        return AndThenObject
    }

    @Suppress("FunctionName")
    infix fun IS(value: LinguisticValue): AndThenObject {
        truthiness_list.add(true)
        return isAction(value)
    }

    @Suppress("FunctionName")
    infix fun IS_NOT(value: LinguisticValue): AndThenObject {
        truthiness_list.add(false)
        return isAction(value)
    }
}
