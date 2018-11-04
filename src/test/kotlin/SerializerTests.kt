import com.fasterxml.jackson.databind.ObjectMapper
import junit.framework.Assert
import bg.arusenov.patchable.Patchable
import bg.arusenov.patchable.jackson.PatchableModule
import org.junit.Test

class SerializerTests {

    @Test
    fun `Not set Patchables should not be present in the output JSON`() {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(PatchableModule())
        val dto = DTO()
        val result = objectMapper.writeValueAsString(dto)
        Assert.assertEquals("{\"age\":5}", result)
    }

    class DTO {
        var name = Patchable.notSet<String>()
        var age = Patchable.of(5)
    }
}