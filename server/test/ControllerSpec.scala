import org.scalatestplus.play.PlaySpec
import controllers.Application
import play.api.test.Helpers
import play.api.test.Helpers._
import play.api.test.FakeRequest


class ControllerSpec extends PlaySpec {
    "Application#index" must {
        "give back expected page" in {
            val controller = new Application(Helpers.stubControllerComponents())
            val result = controller.index.apply(FakeRequest())
            val bodyTest = contentAsString(result)
            bodyTest must include ("Play and Scala.js")
        }
    }

}