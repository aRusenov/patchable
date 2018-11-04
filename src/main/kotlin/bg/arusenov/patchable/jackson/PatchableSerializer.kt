package bg.arusenov.patchable.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.ReferenceTypeSerializer
import com.fasterxml.jackson.databind.type.ReferenceType
import com.fasterxml.jackson.databind.util.NameTransformer
import bg.arusenov.patchable.Patchable

class PatchableSerializer : ReferenceTypeSerializer<Patchable<*>> {

    constructor(fullType: ReferenceType, staticTyping: Boolean,
                vts: TypeSerializer?, ser: JsonSerializer<Any>?) : super(fullType, staticTyping, vts, ser)


    constructor(base: PatchableSerializer, property: BeanProperty,
                vts: TypeSerializer?, valueSer: JsonSerializer<*>?, unwrapper: NameTransformer?,
                contentIncl: JsonInclude.Include?) : super(base, property, vts, valueSer, unwrapper, contentIncl)


    override fun _isValueEmpty(value: Patchable<*>?): Boolean {
        return value == null || !value.isSet
    }

    override fun _getReferenced(value: Patchable<*>?): Any? {
        return value?.value
    }

    override fun _getReferencedIfPresent(value: Patchable<*>?): Any? {
        return value?.value
    }

    override fun withResolved(prop: BeanProperty, vts: TypeSerializer?, valueSer: JsonSerializer<*>?, unwrapper: NameTransformer?, contentIncl: JsonInclude.Include?): ReferenceTypeSerializer<Patchable<*>> {
        return PatchableSerializer(this, prop, vts, valueSer, unwrapper, contentIncl)
    }

    override fun serialize(ref: Patchable<*>?, g: JsonGenerator?, provider: SerializerProvider?) {
        if (ref?.isSet == true) {
            super.serialize(ref, g, provider)
        }
    }

    override fun isEmpty(provider: SerializerProvider?, value: Patchable<*>?): Boolean {
        return value == null || !value.isSet
    }
}