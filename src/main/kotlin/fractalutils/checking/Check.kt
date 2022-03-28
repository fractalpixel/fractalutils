package fractalutils.checking

import fractalutils.strings.isJavaIdentifier
import fractalutils.strings.isStrictIdentifier

/**
 * Utility functions for checking parameter validity.
 * They throw a descriptive IllegalArgumentException if they fail.
 */
object Check {

    private val CHECK_CLASS_NAME = Check::class.java.name

    private val epsilon = 0.00000001
    private val epsilonFloat = 0.000001f

    /**
     * Checks that the specified condition is fulfilled.
     * @param condition true if condition acceptable, false if an error should be thrown.
     * @param description description of the invariant, added to exception if invariant failed.
     * @throws IllegalArgumentException if the check fails.
     */
    fun invariant(condition: Boolean, description: String) {
        if (!condition) {
            fail(description)
        }
    }

    /**
     * Checks that the specified parameter is not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun normalNumber(parameter: Double, parameterName: String) {
        if (parameter.isInfinite()) {
            fail(parameter, parameterName, "be a normal, non-infinite number")
        }
        if (parameter.isNaN()) {
            fail(parameter, parameterName, "be a normal number")
        }
    }


    /**
     * Checks that the specified parameter is not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun normalNumber(parameter: Float, parameterName: String) {
        if (parameter.isInfinite()) {
            fail(parameter, parameterName, "be a normal, non-infinite number")
        }
        if (parameter.isNaN()) {
            fail(parameter, parameterName, "be a normal number")
        }
    }


    /**
     * Checks that the specified parameter is positive and not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun positiveOrZero(parameter: Double, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter < 0) {
            fail(parameter, parameterName, "be a normal positive number")
        }
    }

    /**
     * Checks that the specified parameter is positive and not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun positiveOrZero(parameter: Float, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter < 0) {
            fail(parameter, parameterName, "be a normal positive number")
        }
    }


    /**
     * Checks that the specified parameter is positive, not zero, not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun positive(parameter: Double, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter <= 0) {
            fail(parameter, parameterName, "be a normal positive non-zero number")
        }
    }

    /**
     * Checks that the specified parameter is positive, not zero, not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun positive(parameter: Float, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter <= 0) {
            fail(parameter, parameterName, "be a normal positive non-zero number")
        }
    }

    /**
     * Checks that the specified parameter is positive and not zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun positive(parameter: Long, parameterName: String) {
        if (parameter <= 0) {
            fail(parameter, parameterName, "be a normal positive non-zero number")
        }
    }


    /**
     * Checks that the specified parameter is negative and not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun negativeOrZero(parameter: Double, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter > 0) {
            fail(parameter, parameterName, "be a normal negative or zero number")
        }
    }

    /**
     * Checks that the specified parameter is negative and not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun negativeOrZero(parameter: Float, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter > 0) {
            fail(parameter, parameterName, "be a normal negative or zero number")
        }
    }


    /**
     * Checks that the specified parameter is negative, not zero, not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun negative(parameter: Double, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter >= 0) {
            fail(parameter, parameterName, "be a normal negative non-zero number")
        }
    }

    /**
     * Checks that the specified parameter is negative, not zero, not infinite and not NaN (not a number).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun negative(parameter: Float, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter >= 0) {
            fail(parameter, parameterName, "be a normal negative non-zero number")
        }
    }


    /**
     * Checks that the specified parameter is not zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param epsilon +- range around zero that is regarded as zero.
     * @throws IllegalArgumentException if the check fails.
     */
    fun notZero(parameter: Double, parameterName: String, e: Double = epsilon) {
        normalNumber(parameter, parameterName)

        if (parameter >= -e && parameter <= e) {
            fail(parameter, parameterName, "be a normal non-zero number")
        }
    }

    /**
     * Checks that the specified parameter is not zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param epsilon +- range around zero that is regarded as zero.
     * @throws IllegalArgumentException if the check fails.
     */
    fun notZero(parameter: Float, parameterName: String, e: Float = epsilonFloat) {
        normalNumber(parameter, parameterName)

        if (parameter >= -e && parameter <= e) {
            fail(parameter, parameterName, "be a normal non-zero number")
        }
    }

    /**
     * Checks that the specified parameter is positive and not zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun positive(parameter: Int, parameterName: String) {
        if (parameter <= 0) {
            fail(parameter, parameterName, "be a positive non-zero number")
        }
    }


    /**
     * Checks that the specified parameter is positive or zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun positiveOrZero(parameter: Int, parameterName: String) {
        if (parameter < 0) {
            fail(parameter, parameterName, "be a positive or zero number")
        }
    }

    /**
     * Checks that the specified parameter is positive or zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun positiveOrZero(parameter: Long, parameterName: String) {
        if (parameter < 0) {
            fail(parameter, parameterName, "be a positive or zero number")
        }
    }

    /**
     * Checks that the specified parameter is negative and not zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun negative(parameter: Int, parameterName: String) {
        if (parameter >= 0) {
            fail(parameter, parameterName, "be a negative non-zero number")
        }
    }


    /**
     * Checks that the specified parameter is negative or zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun negativeOrZero(parameter: Int, parameterName: String) {
        if (parameter > 0) {
            fail(parameter, parameterName, "be a negative or zero number")
        }
    }

    /**
     * Checks that the specified parameter not zero.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun notZero(parameter: Int, parameterName: String) {
        if (parameter == 0) {
            fail(parameter, parameterName, "be a non-zero number")
        }
    }


    /**
     * Checks that the specified parameter is a valid Java style identifier (starts with unicode letter or underscore or $,
     * contains unicode letters, underscores, numbers, and $).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun identifier(parameter: String?, parameterName: String) {
        if (parameter == null || !parameter.isJavaIdentifier()) {
            fail(parameter, parameterName, "be a valid Java style identifier")
        }
    }

    /**
     * Checks that the specified parameter is a strict identifier (starts with a-z, A-Z, or _,
     * contains a-z, A-Z, _, or 0-9).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun strictIdentifier(parameter: String?, parameterName: String) {
        if (parameter == null || !parameter.isStrictIdentifier()) {
            fail(parameter, parameterName, "be a valid identifier")
        }
    }

    /**
     * Checks that the specified parameter is not an empty string nor null nor a string with just whitespace.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun nonEmptyString(parameter: String?, parameterName: String) {
        if (parameter == null || parameter.trim { it <= ' ' }.isEmpty()) {
            fail(parameter, parameterName, "be a non-empty string value")
        }
    }

    /**
     * Checks that the parameter is in the range 0..1 (inclusive).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRangeZeroToOne(parameter: Double, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter < 0 || parameter > 1) {
            fail(parameter, parameterName, "be in the range 0 to 1 inclusive")
        }
    }

    /**
     * Checks that the parameter is in the range 0..1 (inclusive).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRangeZeroToOne(parameter: Float, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter < 0 || parameter > 1) {
            fail(parameter, parameterName, "be in the range 0 to 1 inclusive")
        }
    }

    /**
     * Checks that the parameter is in the range -1..1 (inclusive).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRangeMinusOneToOne(parameter: Double, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter < -1 || parameter > 1) {
            fail(parameter, parameterName, "be in the range -1 to 1 inclusive")
        }
    }

    /**
     * Checks that the parameter is in the range -1..1 (inclusive).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRangeMinusOneToOne(parameter: Float, parameterName: String) {
        normalNumber(parameter, parameterName)

        if (parameter < -1 || parameter > 1) {
            fail(parameter, parameterName, "be in the range -1 to 1 inclusive")
        }
    }


    /**
     * Checks that the parameter is in the specified range
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRange(parameter: Double,
                parameterName: String,
                minimumValueInclusive: Double,
                maximumValueExclusive: Double) {
        normalNumber(parameter, parameterName)

        if (parameter < minimumValueInclusive || parameter >= maximumValueExclusive) {
            fail(parameter, parameterName,
                "be in the range $minimumValueInclusive (inclusive) to $maximumValueExclusive (exclusive)")
        }
    }

    /**
     * Checks that the parameter is in the specified range
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRange(parameter: Float,
                parameterName: String,
                minimumValueInclusive: Float,
                maximumValueExclusive: Float) {
        normalNumber(parameter, parameterName)

        if (parameter < minimumValueInclusive || parameter >= maximumValueExclusive) {
            fail(parameter, parameterName,
                "be in the range $minimumValueInclusive (inclusive) to $maximumValueExclusive (exclusive)")
        }
    }

    /**
     * Checks that the parameter is in the specified range
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRangeInclusive(parameter: Double,
                         parameterName: String,
                         minimumValueInclusive: Double,
                         maximumValueInclusive: Double) {
        normalNumber(parameter, parameterName)

        if (parameter < minimumValueInclusive || parameter > maximumValueInclusive) {
            fail(parameter, parameterName,
                "be in the range $minimumValueInclusive (inclusive) to $maximumValueInclusive (inclusive)")
        }
    }

    /**
     * Checks that the parameter is in the specified range
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRangeInclusive(parameter: Float,
                         parameterName: String,
                         minimumValueInclusive: Float,
                         maximumValueInclusive: Float) {
        normalNumber(parameter, parameterName)

        if (parameter < minimumValueInclusive || parameter > maximumValueInclusive) {
            fail(parameter, parameterName,
                "be in the range $minimumValueInclusive (inclusive) to $maximumValueInclusive (inclusive)")
        }
    }


    /**
     * Checks that the parameter is in the specified range
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRange(parameter: Int,
                parameterName: String,
                minimumValueInclusive: Int,
                maximumValueExclusive: Int) {
        if (parameter < minimumValueInclusive || parameter >= maximumValueExclusive) {
            fail(parameter, parameterName,
                "be in the range $minimumValueInclusive (inclusive) to $maximumValueExclusive (exclusive)")
        }
    }

    /**
     * Checks that the parameter is in the specified range
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun inRangeInclusive(parameter: Int,
                         parameterName: String,
                         minimumValueInclusive: Int,
                         maximumValueInclusive: Int) {
        if (parameter < minimumValueInclusive || parameter > maximumValueInclusive) {
            fail(parameter, parameterName,
                "be in the range $minimumValueInclusive (inclusive) to $maximumValueInclusive (inclusive)")
        }
    }


    /**
     * Checks that the parameter is smaller than the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueExclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun less(parameter: Int,
             parameterName: String,
             maximumValueExclusive: Int,
             valueName: String? = null) {
        if (parameter >= maximumValueExclusive) {
            fail(parameter, parameterName, "smaller than", maximumValueExclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is smaller or equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueInclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun lessOrEqual(parameter: Int,
                    parameterName: String,
                    maximumValueInclusive: Int,
                    valueName: String? = null) {
        if (parameter > maximumValueInclusive) {
            fail(parameter, parameterName, "smaller or equal to", maximumValueInclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is greater than the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueExclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun greater(parameter: Int,
                parameterName: String,
                minimumValueExclusive: Int,
                valueName: String? = null) {
        if (parameter <= minimumValueExclusive) {
            fail(parameter, parameterName, "larger than", minimumValueExclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is greater or equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueInclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun greaterOrEqual(parameter: Int,
                       parameterName: String,
                       minimumValueInclusive: Int,
                       valueName: String? = null) {
        if (parameter < minimumValueInclusive) {
            fail(parameter, parameterName, "larger or equal to", minimumValueInclusive, valueName)
        }
    }


    /**
     * Checks that the integer parameter is equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param requiredValue the value that the parameter should be equal to
     * @param valueName description of the value to be equal to, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun equal(parameter: Int,
              parameterName: String,
              requiredValue: Int,
              valueName: String? = null) {
        if (parameter != requiredValue) {
            fail(parameter, parameterName, "equal to", requiredValue, valueName)
        }
    }

    /**
     * Checks that the parameter is smaller than the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueExclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun less(parameter: Double,
             parameterName: String,
             maximumValueExclusive: Double,
             valueName: String? = null) {
        if (parameter >= maximumValueExclusive) {
            fail(parameter, parameterName, "smaller than", maximumValueExclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is smaller than the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueExclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun less(parameter: Float,
             parameterName: String,
             maximumValueExclusive: Float,
             valueName: String? = null) {
        if (parameter >= maximumValueExclusive) {
            fail(parameter, parameterName, "smaller than", maximumValueExclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is smaller or equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueInclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun lessOrEqual(parameter: Double,
                    parameterName: String,
                    maximumValueInclusive: Double,
                    valueName: String? = null) {
        if (parameter > maximumValueInclusive) {
            fail(parameter, parameterName, "smaller or equal to", maximumValueInclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is smaller or equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueInclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun lessOrEqual(parameter: Float,
                    parameterName: String,
                    maximumValueInclusive: Float,
                    valueName: String? = null) {
        if (parameter > maximumValueInclusive) {
            fail(parameter, parameterName, "smaller or equal to", maximumValueInclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is greater than the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueExclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun greater(parameter: Double,
                parameterName: String,
                minimumValueExclusive: Double,
                valueName: String? = null) {
        if (parameter <= minimumValueExclusive) {
            fail(parameter, parameterName, "larger than", minimumValueExclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is greater than the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueExclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun greater(parameter: Float,
                parameterName: String,
                minimumValueExclusive: Float,
                valueName: String? = null) {
        if (parameter <= minimumValueExclusive) {
            fail(parameter, parameterName, "larger than", minimumValueExclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is greater or equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueInclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun greaterOrEqual(parameter: Double,
                       parameterName: String,
                       minimumValueInclusive: Double,
                       valueName: String? = null) {
        if (parameter < minimumValueInclusive) {
            fail(parameter, parameterName, "larger or equal to", minimumValueInclusive, valueName)
        }
    }

    /**
     * Checks that the parameter is greater or equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueInclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    fun greaterOrEqual(parameter: Float,
                       parameterName: String,
                       minimumValueInclusive: Float,
                       valueName: String? = null) {
        if (parameter < minimumValueInclusive) {
            fail(parameter, parameterName, "larger or equal to", minimumValueInclusive, valueName)
        }
    }


    /**
     * Checks that the floating point parameter is equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param requiredValue the value that the parameter should be equal to
     * @param valueName description of the value to be equal to, or null if no description is desired.
     * @param maximumAllowedDifference the epsilon, or maximum allowed difference, between the parameter value and the required value.
     * *                                 Needed for floating point values as they are not precise.
     * @throws IllegalArgumentException if the check fails.
     */
    fun equal(parameter: Double,
              parameterName: String,
              requiredValue: Double,
              valueName: String? = null,
              maximumAllowedDifference: Double = epsilon
    ) {
        if (parameter < requiredValue - maximumAllowedDifference || parameter > requiredValue + maximumAllowedDifference) {
            fail(parameter, parameterName, "equal to (with a max difference of $maximumAllowedDifference)", requiredValue, valueName)
        }
    }

    /**
     * Checks that the floating point parameter is equal to the specified value.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param requiredValue the value that the parameter should be equal to
     * @param valueName description of the value to be equal to, or null if no description is desired.
     * @param maximumAllowedDifference the epsilon, or maximum allowed difference, between the parameter value and the required value.
     * *                                 Needed for floating point values as they are not precise.
     * @throws IllegalArgumentException if the check fails.
     */
    fun equal(parameter: Float,
              parameterName: String,
              requiredValue: Float,
              valueName: String? = null,
              maximumAllowedDifference: Float = epsilonFloat
    ) {
        if (parameter < requiredValue - maximumAllowedDifference || parameter > requiredValue + maximumAllowedDifference) {
            fail(parameter, parameterName, "equal to (with a max difference of $maximumAllowedDifference)", requiredValue, valueName)
        }
    }


    /**
     * Checks that the parameter equals the specified value.
     * Uses the equals method.
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun equals(parameter: Any?,
               parameterName: String,
               requiredValue: Any?,
               otherName: String) {
        if (parameter != null && parameter != requiredValue || parameter == null && requiredValue != null) {
            fail(parameter, parameterName,
                "be equal to $otherName which is '$requiredValue'")
        }
    }

    /**
     * Checks that the parameter equals the specified value.
     * Uses reference equality ( == ).
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    fun equalRef(parameter: Any,
                 parameterName: String,
                 requiredValue: Any,
                 otherName: String) {
        if (parameter !== requiredValue) {
            fail(parameter, parameterName,
                "be the same object as $otherName which is '$requiredValue'")
        }
    }


    /**
     * Checks that the specified element is contained in the specified collection.
     * @param element element to check for.
     * @param collection collection to check in
     * @param collectionName  name to use for collection in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    fun contained(element: Any?,
                  collection: Collection<*>,
                  collectionName: String = "collection") {
        if (!collection.contains(element)) {
            fail("The " + collectionName + " does not contain " + describeElementType(element))
        }
    }

    /**
     * Checks that the specified element is not contained in the specified collection.
     * @param element element to check for.
     * @param collection collection to check in
     * @param collectionName  name to use for collection in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    fun notContained(element: Any?, collection: Collection<*>, collectionName: String = "collection") {
        if (collection.contains(element)) {
            fail("The " + collectionName + " already contains " + describeElementType(element))
        }
    }


    /**
     * Checks that the specified key is contained in the specified map.
     * @param key key to check for.
     * @param map map to check in
     * @param mapName name to use for map in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    fun contained(key: Any?, map: Map<*, *>, mapName: String = "map") {
        if (!map.containsKey(key)) {
            fail("The $mapName does not contain the key '$key'")
        }
    }

    /**
     * Checks that the specified key is contained in the specified map.
     * @param key key to check for.
     * @param map map to check in
     * @param keyName name to use for the key in any error message.
     * @param mapName name to use for map in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    fun contained(key: Any?, map: Map<*, *>, keyName: String, mapName: String) {
        if (!map.containsKey(key)) {
            fail("The $mapName does not contain the $keyName '$key'")
        }
    }


    /**
     * Checks that the specified key is not contained in the specified map.
     * @param key key to check for.
     * @param map map to check in
     * @param mapName name to use for map in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    fun notContained(key: Any?, map: Map<*, *>, mapName: String = "map") {
        if (map.containsKey(key)) {
            fail("The $mapName already contains the key '$key'")
        }
    }

    /**
     * Checks that the specified key is not contained in the specified map.
     * @param key key to check for.
     * @param map map to check in
     * @param mapName name to use for map in any error message.
     * @param keyName name to use for the key in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    fun notContained(key: Any?, map: Map<*, *>, keyName: String, mapName: String) {
        if (map.containsKey(key)) {
            fail("The $mapName already contains the $keyName '$key'")
        }
    }

    /**
     * Checks that the specified collection is empty.
     * @param collection collection to check.
     * @param collectionName name to use for collection in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    fun empty(collection: Collection<*>, collectionName: String = "collection") {
        if (!collection.isEmpty()) {
            fail("The $collectionName was not empty")
        }
    }

    /**
     * Checks that the specified collection is not empty.
     * @param collection collection to check.
     * @param collectionName name to use for collection in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    fun notEmpty(collection: Collection<*>, collectionName: String = "collection") {
        if (collection.isEmpty()) {
            fail("The $collectionName was empty")
        }
    }


    /**
     * Checks that the parameter is an instance of the specified type.
     * @param parameter parameter to check
     * @param parameterName name to use for parameter
     * @param expectedParameterType type to check for.
     * @throws IllegalArgumentException if the check fails.
     */
    fun instanceOf(parameter: Any,
                   parameterName: String,
                   expectedParameterType: Class<*>) {
        if (!expectedParameterType.isInstance(parameter)) {
            fail("of type '" + parameter.javaClass + "'", parameterName,
                "be of type '$expectedParameterType'")
        }
    }

    /**
     * Checks that the parameter is not an instance of the specified type.
     * @param parameter parameter to check
     * @param parameterName name to use for parameter
     * @param expectedParameterType type to check for.
     * @throws IllegalArgumentException if the check fails.
     */
    fun notInstanceOf(parameter: Any,
                      parameterName: String,
                      expectedParameterType: Class<*>) {
        if (expectedParameterType.isInstance(parameter)) {
            fail("of type '" + parameter.javaClass + "'", parameterName,
                "be of type '$expectedParameterType'")
        }
    }


    /**
     * Throws an IllegalArgumentException, using the specified parameters to compose the error message,
     * and including information on the method that it was thrown from.
     * @throws IllegalArgumentException with message.
     */
    fun fail(parameter: Any?,
             parameterName: String,
             expectedCondition: String) {
        fail("The parameter '$parameterName' should $expectedCondition, but it was '$parameter'")
    }

    /**
     * Throws an IllegalArgumentException, using the specified parameters to compose the error message,
     * and including information on the method that it was thrown from.
     * @param parameter parameter value
     * @param parameterName name of parameter
     * @param failureType type of failure of the parameter
     * @param value value that the parameter failed to relate to
     * @param valueName description of the value that the parameter failed to relate to
     * @throws IllegalArgumentException with message.
     */
    private fun fail(parameter: Any?,
                     parameterName: String,
                     failureType: String,
                     value: Any,
                     valueName: String? = null) {
        val valueDesc = if (valueName == null) " " + value else " $valueName, which is $value"
        fail("The parameter '$parameterName' was '$parameter', but it should be $failureType$valueDesc")
    }


    /**
     * Throws an IllegalArgumentException with the specified description, including information on the method that
     * it was thrown from.
     * @throws IllegalArgumentException with message.
     */
    fun fail(desc: String) {
        throw IllegalArgumentException(desc + determineContext())
    }

    private fun describeElementType(element: Any?): String {
        val elementDesc: String
        if (element == null) {
            elementDesc = "a null element."
        } else {
            elementDesc = "the " + element.javaClass + "  '" + element + "'."
        }

        return elementDesc
    }


    /**
     * @return method and class that this method was called from, excluding any calls from within this class.
     */
    private fun determineContext(): String {
        // Get call stack
        val trace = Thread.currentThread().stackTrace

        // Iterate to first method not in Check class (= the originator of the failed check)
        // Skip over the first entry, as it is the getStackTrace method call.
        for (i in 1 until trace.size) {
            if (trace[i].className != CHECK_CLASS_NAME) {
                val methodName = trace[i].methodName
                val className = trace[i].className
                return ", in method $methodName of class $className"
            }
        }

        // Normally we wouldn't get here
        return "."
    }

}