import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import junit.framework.Assert.*
import models.PatchableDTO
import bg.arusenov.patchable.Patchable
import bg.arusenov.patchable.jackson.PatchableDeserializer
import bg.arusenov.patchable.jackson.PatchableModule
import org.junit.Test

class DeserializerTests {

    @Test
    fun `Missing JSON property should deserialize to Patchable#notSet()`() {
        val mapper = ObjectMapper()
        mapper.registerKotlinModule()
        mapper.registerModule(PatchableModule())

        lateinit var result: PatchableDTO
        var json = """
            { "name" : null }
        """
        result = mapper.readValue<PatchableDTO>(json, PatchableDTO::class.java)
        assertTrue(result.name.isSet)
        assertFalse(result.name.isPresent)
        assertEquals(null, result.name.value)

        json = "{}"
        result = mapper.readValue<PatchableDTO>(json, PatchableDTO::class.java)
        assertFalse(result.name.isSet)
        assertFalse(result.name.isPresent)
        assertEquals(null, result.name.value)
        assertEquals(Patchable.notSet<String>(), result.name)

        val name = "Gosho"
        json = """
            { "name" : "$name" }
        """
        result = mapper.readValue<PatchableDTO>(json, PatchableDTO::class.java)
        assertTrue(result.name.isSet)
        assertTrue(result.name.isPresent)
        assertEquals(name, result.name.value)
    }
}