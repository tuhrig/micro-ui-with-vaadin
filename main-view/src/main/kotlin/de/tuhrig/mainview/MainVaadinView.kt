package de.tuhrig.mainview

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.vaadin.flow.component.ClientCallable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.IFrame
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import org.apache.commons.lang3.StringEscapeUtils
import org.slf4j.LoggerFactory

@Route("languages")
class MainVaadinView : VerticalLayout(), HasUrlParameter<String?> {

    private val log = LoggerFactory.getLogger(this::class.java)
    private val JSON = jacksonObjectMapper() // with Kotlin module!
    private val heading = Html("<h1>Choose a programming language!</h1>")
    private val leftIFrame = IFrame("http://localhost:8081/languages")
    private val rightIFrame = IFrame("http://localhost:8082/languages")

    init {

        val splitLayout = SplitLayout(leftIFrame, rightIFrame)
        splitLayout.setSizeFull()

        this.add(heading)
        this.add(splitLayout)
        this.setSizeFull()

        element.executeJs("""
            window.addEventListener("message", (event) => {
                ${'$'}0.${'$'}server.receiveFrontendEvent(event.data);
            });
        """.trimIndent(), element)
    }

    private fun setUrlPathTo(path: String) {
        UI.getCurrent().page.history.pushState(null, path)
    }

    @ClientCallable
    fun receiveFrontendEvent(event: String) {
        log.info("Main-View received event from frontend: {}", event)
        val languageSelectedEvent = JSON.readValue<LanguageSelectedEvent>(event)
        val language = languageSelectedEvent.language
        heading.setHtmlContent("<h1>What is ${language}?</h1>")
        setUrlPathTo("/languages/$language")
        broadcastSelectedLanguage(language)
    }

    private fun broadcastSelectedLanguage(language: String) {
        val languageSelectedEvent = LanguageSelectedEvent(language)
        val asJson = JSON.writeValueAsString(languageSelectedEvent)
        val escapedJson = StringEscapeUtils.escapeJson(asJson)
        element.executeJs("""
            window.frames[0].postMessage("$escapedJson", "*");
            window.frames[1].postMessage("$escapedJson", "*");
        """.trimIndent(), element)
    }

    override fun setParameter(event: BeforeEvent, parameter: String?) {
        if (!parameter.isNullOrBlank()) {
            val language = parameter.trim()
            heading.setHtmlContent("<h1>What is ${language}?</h1>")
            leftIFrame.src = "http://localhost:8081/languages/$language"
            rightIFrame.src = "http://localhost:8082/languages/$language"
        }
    }
}
