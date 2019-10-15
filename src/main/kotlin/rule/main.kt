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
    val color = LinguisticVariable.get("color")
    val rule = Statement(color, color.getValue("green"), true)

    val car = Car("green")

    assert(rule.isTrue(car))
}
