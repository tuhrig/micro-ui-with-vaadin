package de.tuhrig.rightview

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.vaadin.flow.component.ClientCallable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import org.slf4j.LoggerFactory

@Route("languages")
class RightVaadinView : VerticalLayout(), HasUrlParameter<String?> {

    private val log = LoggerFactory.getLogger(this::class.java)
    private val JSON = jacksonObjectMapper() // with Kotlin module!
    private val infos = mapOf(
        "Java" to "Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible.",
        "Kotlin" to "Kotlin (/ˈkɒtlɪn/)[2] is a cross-platform, statically typed, general-purpose high-level programming language with type inference.",
        "SQL" to "Structured Query Language (SQL) (/ˌɛsˌkjuːˈɛl/ (listen) S-Q-L, sometimes /ˈsiːkwəl/ \"sequel\" for historical reasons),[4][5] is a domain-specific language used in programming and designed for managing data held in a relational database management system (RDBMS), or for stream processing in a relational data stream management system (RDSMS).",
        "JavaScript" to "JavaScript (/ˈdʒɑːvəskrɪpt/), often abbreviated as JS, is a programming language that is one of the core technologies of the World Wide Web, alongside HTML and CSS. As of 2022, 98% of websites use JavaScript on the client side for webpage behavior, often incorporating third-party libraries."
    )
    private val infoBox = Html("<div></div>")

    init {

        this.add(infoBox)
        this.setSizeFull()

        element.executeJs("""
            window.addEventListener("message", (event) => {
                ${'$'}0.${'$'}server.receiveFrontendEvent(event.data);
            });
        """.trimIndent(), element)
    }

    @ClientCallable
    fun receiveFrontendEvent(event: String) {
        log.info("Right-View received event from frontend: {}", event)
        val languageSelectedEvent = JSON.readValue<LanguageSelectedEvent>(event)
        val language = languageSelectedEvent.language
        val info = infos.entries.find { it.key == language }?.value
        infoBox.setHtmlContent("<div>$info</div>")
    }

    override fun setParameter(event: BeforeEvent, parameter: String?) {
        if (!parameter.isNullOrBlank()) {
            val language = parameter.trim()
            val info = infos.entries.find { it.key == language }?.value
            infoBox.setHtmlContent("<div>$info</div>")
        }
    }
}