package bg.arusenov.patchable.validate;

import bg.arusenov.patchable.Patchable;
import org.hibernate.validator.internal.engine.valueextraction.ValueExtractorDescriptor;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.UnwrapByDefault;
import javax.validation.valueextraction.ValueExtractor;

@UnwrapByDefault
public class PatchableValueExtractor implements ValueExtractor<Patchable<@ExtractedValue ?>> {

    public static final ValueExtractorDescriptor DESCRIPTOR = new ValueExtractorDescriptor(new PatchableValueExtractor());

    @Override
    public void extractValues(Patchable<?> originalValue, ValueReceiver receiver) {
        // By passing the value to the receiver we trigger the wrapped element's validations
        // Therefore, we only proceed if the Patchable has a set value
        if (originalValue.isSet()) {
            receiver.value( null, originalValue.getValue());
        }
    }
}