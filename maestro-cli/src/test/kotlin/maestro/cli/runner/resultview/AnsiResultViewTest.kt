package maestro.cli.runner.resultview

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.Instant

class AnsiResultViewTest {

    private lateinit var originalOut: PrintStream

    @BeforeEach
    fun setUp() {
        originalOut = System.out
        System.setOut(PrintStream(ByteArrayOutputStream()))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
    }

    @Test
    fun `times frames from when the recording started, not when the view was created`() {
        // Given
        val resultView = AnsiResultView()
        Thread.sleep(200)
        val recordingStartedAt = Instant.now()

        // When
        resultView.setState(UiState.Error("failed"))
        val shownWithin = Instant.now().toEpochMilli() - recordingStartedAt.toEpochMilli()

        // Then
        assertThat(resultView.getFrames(startedAt = recordingStartedAt).single().timestamp).isIn(0L..shownWithin)
    }

    @Test
    fun `puts a frame shown before the recording started at the start of the video`() {
        // Given
        val resultView = AnsiResultView()
        resultView.setState(UiState.Error("failed"))

        // When
        val frames = resultView.getFrames(startedAt = Instant.now().plusSeconds(5))

        // Then
        assertThat(frames.single().timestamp).isEqualTo(0)
    }
}
