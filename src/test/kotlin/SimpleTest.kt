import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test
import kotlin.test.assertEquals

class ShallowCopyTest {

    @Test
    fun `Data class with sum of fields`() {

        val result = compileWithPlugin(
            sourceFile = SourceFile.kotlin(
                "Main.kt", """
                    import kotlin.test.assertEquals

                    data class Player(
                        val x: Int,
                        val y: Int,
                        val z: Int,
                        val description: String,
                        val timestamp: Long
                    )
                    
                    fun main() {
                        val player = Player(100, 50, 89, "Point in space", System.currentTimeMillis())
                        println("Result ShallowCopy size: " + player.shallowSize())
                        assertEquals(28, player.shallowSize())
                    }
                """.trimIndent()
            )
        )
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun `Data class with nullable types`() {
        val result = compileWithPlugin(
            sourceFile = SourceFile.kotlin(
                "Main.kt", """
                    import kotlin.test.assertEquals
                    data class Person(
                        val name: String?,
                        val age: Int?
                    )
                    
                    fun main() {
                        val person = Person("John", 25)
                        println("Result: " + person.shallowSize())
                        assertEquals(8, person.shallowSize())
                    }
                """.trimIndent()
            )
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun `Data class with arrays and lists`() {
        val result = compileWithPlugin(
            sourceFile = SourceFile.kotlin(
                "Main.kt", """
                    import kotlin.test.assertEquals

                    data class ComplexData(
                        val numbers: IntArray,
                        val names: List<String>
                    )
                    
                    fun main() {
                        val data = ComplexData(intArrayOf(1, 2, 3), listOf("Alice", "Bob"))
                        println("Result: " + data.shallowSize())
                        assertEquals(16, data.shallowSize())
                    }
                """.trimIndent()
            )
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun `Nested data classes`() {
        val result = compileWithPlugin(
            sourceFile = SourceFile.kotlin(
                "Main.kt", """
                    import kotlin.test.assertEquals

                    data class Outer(
                        val inner: Inner
                    )

                    data class Inner(
                        val value: String
                    )
                    
                    fun main() {
                        val outer = Outer(Inner("Nested"))
                        println("Result: " + outer.shallowSize())
                        assertEquals(16, outer.shallowSize())
                    }
                """.trimIndent()
            )
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun `Data class with generics`() {
        val result = compileWithPlugin(
            sourceFile = SourceFile.kotlin(
                "Main.kt", """
                    import kotlin.test.assertEquals

                    data class Box<T>(
                        val item: T
                    )
                    
                    fun main() {
                        val box = Box("Content")
                        println("Result: " + box.shallowSize())
                        assertEquals(8, box.shallowSize())
                    }
                """.trimIndent()
            )
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }


    companion object {
        @OptIn(ExperimentalCompilerApi::class)
        private fun compileWithPlugin(sourceFile: SourceFile) = KotlinCompilation().apply {
            sources = listOf(sourceFile)
            compilerPluginRegistrars = listOf(ShallowSizeCompilerPluginRegistrar(true))
            inheritClassPath = true
            messageOutputStream = System.out
        }.compile()
    }
}
