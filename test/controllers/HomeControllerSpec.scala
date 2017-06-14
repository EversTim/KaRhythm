package controllers

import nl.sogyo.kbd.db._
import nl.sogyo.kbd.db.patterns.PatternCollectionMock
import nl.sogyo.kbd.db.sounds.SoundCollectionMock
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest  {

  val sc = new SoundCollectionMock
  val pc = new PatternCollectionMock

  "HomeController GET" should {

    "render the index page fromPatternID a new instance of controller" in {
      val controller = new HomeController(pc, sc)
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }

    "render the index page fromPatternID the application" in {
      val controller = app.injector.instanceOf[HomeController]
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }

    "render the index page fromPatternID the router" in {
      // Need to specify Host header to get through AllowedHostsFilter
      val request = FakeRequest(GET, "/").withHeaders("Host" -> "localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }


    "render 8 checkboxes by default" in {
      val controller = new HomeController(pc, sc)
      val home = controller.index().apply(FakeRequest())
      val content = contentAsString(home)

      val required = "type=\'checkbox\'"
      val occurrences = content.sliding(required.length).count(_ == required)

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      occurrences mustBe 8
    }

    "have 4 of the default checkboxes checked" in {
      val controller = new HomeController(pc, sc)
      val home = controller.index().apply(FakeRequest())
      val content = contentAsString(home)

      val required = " checked "
      val occurrences = content.sliding(required.length).count(_ == required)

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      occurrences mustBe 4
    }
  }
}
