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

fun clear() {
    variables.clear()
    values.clear()
    truthiness_list.clear()
}

object DECLARE {

    @Suppress("FunctionName")
    infix fun IF(variable: LinguisticVariable): IsObject {
        variables.add(variable)
        return IsObject
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
        for (i in 0 until variables.size) {
            conditions.add(Statement(variables[i], values[i], truthiness_list[i]))
        }
        clear()
        return andThenAction(variable)
    }

    @Suppress("FunctionName", "UNUSED_PARAMETER")
    infix fun END(declare: DECLARE): Rule {
        for (i in 0 until variables.size) {
            conclusions.add(Statement(variables[i], values[i], truthiness_list[i]))
        }
        clear()
        conditions.clear()
        conclusions.clear()
        return Rule(conditions.toSet(), conclusions.toSet())
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
