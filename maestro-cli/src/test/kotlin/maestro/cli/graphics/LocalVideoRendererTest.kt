package maestro.cli.graphics

import com.google.common.truth.Truth.assertThat
import maestro.cli.runner.resultview.AnsiResultView
import org.junit.jupiter.api.Test

class LocalVideoRendererTest {

    private fun frameAt(ms: Long) = AnsiResultView.Frame(ms, "")

    @Test
    fun `lengthens the video to show screens shown after the recording ended`() {
        val duration = LocalVideoRenderer.outputDurationSeconds(10.0, listOf(frameAt(0), frameAt(14_000)))

        assertThat(duration).isEqualTo(16.0)
    }

    @Test
    fun `keeps the recording's length when the last screen leaves time to read it`() {
        val duration = LocalVideoRenderer.outputDurationSeconds(30.0, listOf(frameAt(0), frameAt(20_000)))

        assertThat(duration).isEqualTo(30.0)
    }

    @Test
    fun `keeps the recording's length when there are no screens`() {
        assertThat(LocalVideoRenderer.outputDurationSeconds(30.0, emptyList())).isEqualTo(30.0)
    }
}
