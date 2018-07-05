/**
 * @author zxj5470
 * @date 2018/7/6
 */
import com.github.zxj5470.js2dts.doRec
import com.intellij.codeInsight.completion.LightFixtureCompletionTestCase
import com.intellij.lang.javascript.structureView.JSStructureViewElement
import org.junit.Test

class TestParseTree : LightFixtureCompletionTestCase() {
	override fun getTestDataPath(): String {
		return this::class.java.getResource("").path.removeSuffix("/")
	}

	@Test
	fun testParse() {
		myFixture.configureByFile("zondyClient.js")
		val jsFile = myFixture.file
		val jsStructureViewElement = JSStructureViewElement(jsFile, false)
		jsStructureViewElement.children.forEach {
			(it as JSStructureViewElement).doRec(0)
		}
	}
}
