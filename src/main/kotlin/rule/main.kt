package rule


class Car(private val color: String) : RuleSubject {

    override fun getVariableValue(variable: LinguisticVariable): LinguisticValue? {
        return when {
            variable.name == "color" -> variable.getValue(color)
            else -> null
        }
    }

}


fun main() {
    LinguisticVariable.build(
        "color",
        setOf(
            LinguisticValue("blue"), LinguisticValue("red"), LinguisticValue("green")
        )
    )
    LinguisticVariable.build(
        "size",
        setOf(
            LinguisticValue("large"), LinguisticValue("small"), LinguisticValue("medium")
        )
    )
    LinguisticVariable.build(
        "aesthetic",
        setOf(
            LinguisticValue("beautiful"), LinguisticValue("ugly"), LinguisticValue("ordinary")
        )
    )

    val color = LinguisticVariable.get("color")
    val green = color.getValue("green")

    val size = LinguisticVariable.get("size")
    val large = color.getValue("green")

    val aesthetic = LinguisticVariable.get("aesthetic")
    val ugly = aesthetic.getValue("ugly")

    val statement = Statement(color, green, true)

    val car = Car("green")

    assert(statement.isTrue(car))

    val rule: Rule = (
        DECLARE IF color IS green AND size IS large AND color IS green THEN aesthetic IS ugly
    ).getRule()
    assert(rule.isTrue(car))
}

object DECLARE {

    private enum class NextKeyWord {
        IF, AND, IS, THEN
    }

    private enum class DeclarationPart {
        CONDITIONS, CONCLUSIONS
    }

    private val variables = mutableListOf<LinguisticVariable>()
    private val values = mutableListOf<LinguisticValue>()
    private val truthiness_list = mutableListOf<Boolean>()
    private var nextKeyWords = setOf(NextKeyWord.IF)
    private var declarationPart = DeclarationPart.CONDITIONS
    private val conditions = mutableListOf<Statement>()
    private val conclusions = mutableListOf<Statement>()

    infix fun IF(linguisticVariable: LinguisticVariable): DECLARE {
        if (NextKeyWord.IF in nextKeyWords) {
            variables.add(linguisticVariable)
            nextKeyWords = setOf(NextKeyWord.IS)
            return this
        }
        throw Exception("$nextKeyWords expected, not IF")
    }

    infix fun IS(value: LinguisticValue): DECLARE {
        if (NextKeyWord.IS in nextKeyWords) {
            truthiness_list.add(true)
            values.add(value)
            nextKeyWords = setOf(NextKeyWord.AND, NextKeyWord.THEN)
            return this
        }
        throw Exception("$nextKeyWords expected, not IS")
    }

    infix fun AND(variable: LinguisticVariable): DECLARE {
        if (NextKeyWord.AND in nextKeyWords) {
            variables.add(variable)
            nextKeyWords = setOf(NextKeyWord.IS)
            return this
        }
        throw Exception("$nextKeyWords expected, not AND")
    }

    infix fun THEN(linguisticVariable: LinguisticVariable): DECLARE {
        if (NextKeyWord.THEN in nextKeyWords) {
            for (i in 0 until variables.size) {
                conditions.add(Statement(variables.get(i), values.get(i), truthiness_list.get(i)))
            }
            variables.clear()
            values.clear()
            truthiness_list.clear()
            nextKeyWords = setOf(NextKeyWord.IS)
            declarationPart = DeclarationPart.CONCLUSIONS
            return this
        }
        throw Exception("$nextKeyWords expected, not THEN")
    }

    fun getRule(): Rule {
        if (NextKeyWord.AND in nextKeyWords) {
            for (i in 0 until variables.size) {
                conclusions.add(Statement(variables[i], values[i], truthiness_list[i]))
            }
            val rule = Rule(conditions.toSet(), conclusions.toSet())
            variables.clear()
            values.clear()
            truthiness_list.clear()
            nextKeyWords = setOf(NextKeyWord.IF)
            declarationPart = DeclarationPart.CONDITIONS
            conditions.clear()
            conclusions.clear()
            return rule
        }
        throw Exception("$nextKeyWords expected, not getRule method")
    }

}
