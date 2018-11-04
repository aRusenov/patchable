package bg.arusenov.patchable.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.Serializers
import com.fasterxml.jackson.databind.type.ReferenceType
import bg.arusenov.patchable.Patchable

class PatchableModule : Module() {

    override fun getModuleName(): String {
        return "Patchable"
    }

    override fun version(): Version {
        return Version.unknownVersion()
    }

    override fun setupModule(context: Module.SetupContext) {
        context.addSerializers(object : Serializers.Base() {
            override fun findReferenceSerializer(config: SerializationConfig, type: ReferenceType, beanDesc: BeanDescription,
                                                 contentTypeSerializer: TypeSerializer?, contentValueSerializer: JsonSerializer<Any>?): JsonSerializer<*>? {
                val raw = type.rawClass
                val staticTyping = contentTypeSerializer == null && config.isEnabled(MapperFeature.USE_STATIC_TYPING)
                if (Patchable::class.java.isAssignableFrom(raw)) {
                    return PatchableSerializer(type, staticTyping, contentTypeSerializer, contentValueSerializer)
                }

                return null
            }
        })
        context.addDeserializers(object : Deserializers.Base() {
            override fun findReferenceDeserializer(refType: ReferenceType, config: DeserializationConfig?,
                                                   beanDesc: BeanDescription?, contentTypeDeserializer: TypeDeserializer?, contentDeserializer: JsonDeserializer<*>?): JsonDeserializer<*>? {
                if (refType.hasRawClass(Patchable::class.java)) {
                    return PatchableDeserializer()
                }

                return null
            }
        })

        context.addTypeModifier(PatchableTypeModifier())
        context.configOverride(Patchable::class.java)
                .setInclude(JsonInclude.Value.construct(JsonInclude.Include.NON_EMPTY, null))
    }
}
