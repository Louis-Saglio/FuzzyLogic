package rule

import rule.dsl.DECLARE


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
    val red = color.getValue("red")

    val size = LinguisticVariable.get("size")
    val large = color.getValue("green")

    val aesthetic = LinguisticVariable.get("aesthetic")
    val ugly = aesthetic.getValue("ugly")

    val statement = Statement(color, green, true)

    val car = Car("green")

    assert(statement.isTrue(car))

    val rule0 = (
        DECLARE IF color IS green AND size IS large AND color IS_NOT red THEN aesthetic IS ugly
    ).getRule()
    assert(rule0.isTrue(car))

}
