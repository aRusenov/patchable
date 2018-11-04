package bg.arusenov.patchable.jackson

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.ReferenceType
import com.fasterxml.jackson.databind.type.TypeBindings
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.databind.type.TypeModifier
import bg.arusenov.patchable.Patchable

import java.lang.reflect.Type

class PatchableTypeModifier : TypeModifier() {
    override fun modifyType(type: JavaType, jdkType: Type, bindings: TypeBindings, typeFactory: TypeFactory): JavaType {
        if (type.isReferenceType || type.isContainerType) {
            return type
        }

        val refType = if (type.rawClass == Patchable::class.java) {
            type.containedTypeOrUnknown(0)
        } else {
            return type
        }

        return ReferenceType.upgradeFrom(type, refType)
    }
}