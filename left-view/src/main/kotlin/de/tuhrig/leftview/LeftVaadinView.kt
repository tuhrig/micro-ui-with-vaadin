package de.tuhrig.leftview

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.vaadin.flow.component.ClientCallable
import com.vaadin.flow.component.listbox.ListBox
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import org.apache.commons.lang3.StringEscapeUtils
import org.slf4j.LoggerFactory

@Route("languages")
class LeftVaadinView : VerticalLayout(), HasUrlParameter<String?> {

    private val log = LoggerFactory.getLogger(this::class.java)
    private val JSON = jacksonObjectMapper() // with Kotlin module!
    private val selectLanguage = ListBox<String>().apply {
        setItems(
            "Java",
            "Kotlin",
            "SQL",
            "JavaScript"
        )
    }

    init {

        selectLanguage.addValueChangeListener {
            val languageSelectedEvent = LanguageSelectedEvent(it.value)
            val asJson = JSON.writeValueAsString(languageSelectedEvent)
            val escapedJson = StringEscapeUtils.escapeJson(asJson)
            element.executeJs(""""
                window.top.postMessage("$escapedJson", "http://localhost:8080");
            """.trimIndent())
        }

        element.executeJs("""
            window.addEventListener("message", (event) => {
                ${'$'}0.${'$'}server.receiveFrontendEvent(event.data);
            });
        """.trimIndent(), element)

        this.add(selectLanguage)
    }

    @ClientCallable
    fun receiveFrontendEvent(event: String) {
        log.info("Right-View received event from frontend: {}", event)
        val languageSelectedEvent = JSON.readValue<LanguageSelectedEvent>(event)
        val language = languageSelectedEvent.language
        selectLanguage.value = language
    }

    override fun setParameter(event: BeforeEvent, parameter: String?) {
        if (!parameter.isNullOrBlank()) {
            val language = parameter.trim()
            selectLanguage.value = language
        }
    }
}