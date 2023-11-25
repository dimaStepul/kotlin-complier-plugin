import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar


@OptIn(ExperimentalCompilerApi::class)
class ShallowSizeCompilerPluginRegistrar(override val supportsK2: Boolean) : CompilerPluginRegistrar() {

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        SyntheticResolveExtension.registerExtension(ShallowSizeSyntheticResolveExtension())
        IrGenerationExtension.registerExtension(ShallowSizeIrGenerationExtension())
    }

}
