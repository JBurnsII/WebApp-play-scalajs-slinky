import org.scalatestplus.play._
import models._
import collection.mutable
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashSet

class MessagesInMemorySpec extends PlaySpec{
    "MessagesInMemoryModel" must {
        "Do valid login for default user" in {
            MessagesInMemoryModel.validateUser("mlewis", "prof") mustBe (true)
        }

        "reject login with wrong password" in {
            MessagesInMemoryModel.validateUser("mlewis","proof") mustBe (false)
        }

        "reject login with wrong username" in {
            MessagesInMemoryModel.validateUser("lmewis", "prof") mustBe (false)
        }

        "reject login with wrong username and password" in {
            MessagesInMemoryModel.validateUser("lmewis", "proof") mustBe (false)
        }

        "general messages starts empty" in {
            MessagesInMemoryModel.getGeneralMessages() mustBe (ListBuffer[(String,String)]())
        }

        "post general message" in {
            MessagesInMemoryModel.postGM("mlewis", "hello world") mustBe (ListBuffer[(String,String)](("mlewis", "hello world")))
        }

        "get Dms when no DMs sent" in {
            MessagesInMemoryModel.getDMs(HashSet("mlewis","web")) mustBe (ListBuffer[(String,String)]())
        }
        
        "send DM" in {
            MessagesInMemoryModel.sendDM("mlewis", "web", "Hello Web") /*mustBe (mutable.Map[HashSet[String],ListBuffer[(String,String)]](HashSet("mlewis","web")->ListBuffer[(String,String)](("mlewis","Hello Web"))))*/
            MessagesInMemoryModel.getprivateConvos("mlewis") mustBe (ListBuffer[String]("web"))
            MessagesInMemoryModel.getprivateConvos("web") mustBe (ListBuffer[String]("mlewis"))
            MessagesInMemoryModel.getDMs(HashSet("mlewis","web")) mustBe (ListBuffer[(String,String)](("mlewis","Hello Web")))
        }

        "Get users before creating new user" in {
            MessagesInMemoryModel.getUsers() mustBe (List[String]("mlewis","web"))
        }

        "create new user with no DMs" in {
            MessagesInMemoryModel.createUser("lmewis", "proof") mustBe (true)
            MessagesInMemoryModel.getprivateConvos("lmewis") mustBe (ListBuffer[String]())
        }

        "reject creation with existing username" in {
            MessagesInMemoryModel.createUser("lmewis", "proof") mustBe (false)
        }

        "Get users after creating new user" in {
            MessagesInMemoryModel.getUsers() mustBe (List[String]("mlewis","web","lmewis"))
        }

        "Let new user send general message" in {
            MessagesInMemoryModel.postGM("lmewis", "hi") mustBe (ListBuffer[(String,String)](("mlewis", "hello world"), ("lmewis", "hi")))
        }

        "Let new user start with no DMs" in {
            MessagesInMemoryModel.getprivateConvos("lmewis") mustBe (ListBuffer[String]())
        }

        "let new user send DM" in {
            MessagesInMemoryModel.sendDM("lmewis", "web", "hi Web") /*mustBe (mutable.Map[HashSet[String],ListBuffer[(String,String)]](HashSet("lmewis","web")->ListBuffer[(String,String)](("lmewis","hi Web"))))*/
            MessagesInMemoryModel.getprivateConvos("lmewis") mustBe (ListBuffer[String]("web"))
            MessagesInMemoryModel.getprivateConvos("web") mustBe (ListBuffer[String]("mlewis", "lmewis"))
            MessagesInMemoryModel.getDMs(HashSet("lmewis","web")) mustBe (ListBuffer[(String,String)](("lmewis","hi Web")))
        }


    }
}