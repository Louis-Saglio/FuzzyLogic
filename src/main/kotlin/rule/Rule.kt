package rule

import sun.tools.jconsole.Messages.IS


interface RuleSubject {
    fun getVariableValue(variable: LinguisticVariable): String?
}


class Car(private val color: String) : RuleSubject {
    override fun getVariableValue(variable: LinguisticVariable): String? {
        return when {
            variable.name == "color" -> this.color
            else -> null
        }
    }
}


class NotFoundValueError(message: String) : RuntimeException(message)


class LinguisticVariable(val name: String, possibleValues: Set<String>) {

    private val possibleValues = possibleValues.map { LinguisticValue(it) }
    private var of: RuleSubject? = null

    fun getValueByName(valueName: String): LinguisticValue {
        return possibleValues.find { it.name == valueName } ?: throw NotFoundValueError("$name cannot be $valueName")
    }

    infix fun OF(car: RuleSubject): LinguisticVariable {
        of = car
        return this
    }

    infix fun IS(s: String) {

    }
}


class LinguisticValue(val name: String) {
    override fun toString(): String {
        return name
    }
}


class Rule(private val variable: LinguisticVariable, value: String, private val isTrue: Boolean) {

    private val value = variable.getValueByName(value)

    fun check(subject: RuleSubject): Boolean {
        return subject.getVariableValue(variable) == value.name
    }
}


fun main() {
    val color = LinguisticVariable("color", setOf("blue", "red", "green"))
    val rule = Rule(color, "green", true)

    val car = Car("green")
    assert(rule.check(car))
    ASSERT THAT color OF car IS "red"
}

object ASSERT {
    infix fun THAT(variable: LinguisticVariable): LinguisticVariable {
        return variable
    }

}
