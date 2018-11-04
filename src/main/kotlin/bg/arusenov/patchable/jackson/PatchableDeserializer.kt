package bg.arusenov.patchable.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import bg.arusenov.patchable.Patchable

class PatchableDeserializer(vc: Class<*>?) : StdDeserializer<Patchable<*>?>(vc), ContextualDeserializer {

    private var type: JavaType? = null

    constructor() : this(null)

    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer<*> {
        val wrapperType = property.getType()
        val valueType = wrapperType.containedType(0)
        val deserializer = PatchableDeserializer()
        deserializer.type = valueType
        return deserializer
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Patchable<*>? {
        return Patchable.of(ctxt.readValue<Any>(p, type))
    }

    override fun getValueType(): JavaType? {
        return type
    }

    override fun getNullValue(ctxt: DeserializationContext): Patchable<*>? {
        // If node#currentName is null it means the node is missing
        if (ctxt.parser.currentName == null) {
            return Patchable.notSet<Any>()
        }

        return Patchable.of(null)
    }
}