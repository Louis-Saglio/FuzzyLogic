package rule


interface RuleSubject {

    fun getVariableValue(variable: LinguisticVariable): LinguisticValue?

}


class LinguisticValueNotFound(message: String) : RuntimeException(message)


class LinguisticVariable private constructor(
    val name: String,
    private val possibleValues: Collection<LinguisticValue>
) {

    fun getValue(value: String) = possibleValues.find { it.name == value } ?: throw LinguisticValueNotFound(name)

    companion object {

        private val allLinguisticVariablesByName = mutableMapOf<String, LinguisticVariable>()

        fun get(name: String) = allLinguisticVariablesByName[name] ?: throw Exception("LinguisticVariable $name not found")

        fun build(name: String, possibleValues: Collection<LinguisticValue>) {
            if (name in allLinguisticVariablesByName) {
                throw Exception("A variable with this name ($name) already exists")
            }
            val variable = LinguisticVariable(name, possibleValues)
            allLinguisticVariablesByName[name] = variable
        }

    }

}


class LinguisticValue(val name: String) {

    override fun toString() = name

}


class Statement(private val variable: LinguisticVariable, private val value: LinguisticValue, private val isTrue: Boolean) {

    fun isTrue(subject: RuleSubject) = (subject.getVariableValue(variable) == value) == isTrue

}
