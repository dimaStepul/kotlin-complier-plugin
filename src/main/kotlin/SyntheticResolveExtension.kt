import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.builtIns
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

@Suppress("DEPRECATION")
class ShallowSizeSyntheticResolveExtension : SyntheticResolveExtension {
    override fun getSyntheticFunctionNames(thisDescriptor: ClassDescriptor) =
        if (thisDescriptor.isShallowSizeApplicable) listOf(ShallowSizeIdentifier.name)
        else emptyList()

    override fun generateSyntheticMethods(
        thisDescriptor: ClassDescriptor,
        name: Name,
        bindingContext: BindingContext,
        fromSupertypes: List<SimpleFunctionDescriptor>,
        result: MutableCollection<SimpleFunctionDescriptor>
    ) {
        if (name == ShallowSizeIdentifier.name) {
            val descriptor = SimpleFunctionDescriptorImpl.create(
                thisDescriptor,
                Annotations.EMPTY,
                ShallowSizeIdentifier.name,
                CallableMemberDescriptor.Kind.DECLARATION,
                thisDescriptor.source
            ).initialize(
                null,
                thisDescriptor.thisAsReceiverParameter,
                emptyList(),
                emptyList(),
                thisDescriptor.builtIns.intType,
                Modality.OPEN,
                DescriptorVisibilities.PUBLIC
            )
            result += descriptor
        }
    }

    companion object {
        val ClassDescriptor.isShallowSizeApplicable: Boolean
            get() = this.isData
    }
}
