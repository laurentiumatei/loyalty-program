package com.exercise.loyalty.component

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class PointsCalculatorSpec extends Specification{

    def 'calculatePendingPoints returns #result when amount of #amount cash is spent'()
    {
        given:
        PointsCalculator calculator = new PointsCalculator()

        expect:
        calculator.calculatePendingPoints(amount) == result

        where:
        amount | result
        4000.5 | 4000.0
        4500.0 | 4500.0
        5000.0 | 5000.0
        5000.9 | 5000.0
        5001.0 | 5002.0
        7000.0 | 9000.0
        7500.0 | 10000.0
        7500.9 | 10000.0
        7800.0 | 10900.0
    }

    def 'calculateAvailablePointsSpent returns #result when amount of #amount cash is spent'()
    {
        given:
        PointsCalculator calculator = new PointsCalculator()

        expect:
        calculator.calculateAvailablePointsSpent(amount) == result

        where:
        amount | result
        50.0   | 5000.0
        10.75  | 1075.0
        30.5   | 3050.0
    }
}
