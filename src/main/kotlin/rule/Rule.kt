package rule


interface RuleSubject {
    fun getVariableValue(variable: LinguisticVariable): LinguisticValue?
}

class Rule(private val statements: Set<Statement>, val conclusions: Set<Statement>) {
    fun isTrue(subject: RuleSubject) = statements.all { it.isTrue(subject) }
}
