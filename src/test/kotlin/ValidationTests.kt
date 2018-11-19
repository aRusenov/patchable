import models.UserToValidate
import org.junit.Assert
import org.junit.Test
import javax.validation.Validation
import javax.validation.constraints.Email

class ValidationTests {

    @Test
    fun `Validation a Patchable property should correctly trigger validation annotations on T`() {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        // NOTE: We're testing with a Java DTO since Kotlin does not currently support
        // reading annotations from collection containers (see: https://youtrack.jetbrains.com/issue/KT-27049)
        val user = UserToValidate()

        val validationErrors = validator.validate(user)
        Assert.assertEquals(1, validationErrors.size.toLong())
        val failingConstraintAnnotation = validationErrors.first()
                .constraintDescriptor
                .annotation
                .annotationClass

        Assert.assertEquals(Email::class, failingConstraintAnnotation)
    }
}
