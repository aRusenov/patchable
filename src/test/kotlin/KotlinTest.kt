import bg.arusenov.patchable.Patchable
import org.junit.Assert
import org.junit.Test
import java.util.*

class KotlinTest {

    @Test
    fun `Not set Patchable should not call ifSet consumer`() {
        val patchableA = Patchable.of("Gosho")
        val patchableB = Patchable.notSet<String>()

        val presetLastname = UUID.randomUUID().toString()
        val bean = Bean().apply { lastName = presetLastname }
        patchableA.ifSetK(bean::firstName::set)
        patchableB.ifSetK(bean::lastName::set)

        Assert.assertEquals("Gosho", bean.firstName)
        Assert.assertEquals(presetLastname, bean.lastName)
    }

    class Bean {
        var firstName: String? = null
        var lastName: String? = null
    }
}