import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatestplus.play.OneBrowserPerSuite
import org.scalatestplus.play.HtmlUnitFactory

class MessagerSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory{
    "Messager" must {
        "login and access functions" in {
            go to s"http://localhost:$port/login"
            
                pageTitle mustBe "Login"
                find(cssSelector("h2")).isEmpty mustBe false
                find(cssSelector("h2")).foreach(e => e.text mustBe "Login")

                click on "loginUsername"
                textField("loginUsername").value = "mlewis"
                click on "loginPassword"
                pwdField(id("loginPassword")).value = "prof"
                submit()
           eventually {
                pageTitle mustBe "Home"
                find(cssSelector("h1")).isEmpty mustBe false
                find(cssSelector("h1")).foreach(e => e.text mustBe "mlewis's Conversations")
            }
        }

        "send General Message" in {
            click on "generalLink"
            pageTitle mustBe "General Chat"
            find(cssSelector("h1")).isEmpty mustBe false
            find(cssSelector("h1")).foreach(e => e.text mustBe "All Messages")

            click on "generalInput"
            textField("generalInput").value = "hi world"
            submit()

            eventually{
                pageTitle mustBe "General Chat"
                find(cssSelector("h1")).isEmpty mustBe false
                find(cssSelector("h1")).foreach(e => e.text mustBe "All Messages")

                findAll(cssSelector("li")).toList.map(_.text) mustBe List("mlewis: hello world","lmewis: hi", "mlewis: hi world")
                //this is not isolated from the tests in MessagesinMemorySpec; testing everything together has a different result than testOnly MessagerSpec
            }
        }

        "find a user to DM" in {
            click on "returnHomeLink"
            pageTitle mustBe "Home"
            find(cssSelector("h1")).isEmpty mustBe false
            find(cssSelector("h1")).foreach(e => e.text mustBe "mlewis's Conversations")

            click on "userSearchInput"
            textField("userSearchInput").value = "lmewis"
            submit()

            eventually{
                click on "lmewis"
                pageTitle mustBe "lmewis"
                find(cssSelector("h1")).isEmpty mustBe false
                find(cssSelector("h1")).foreach(e => e.text mustBe "Chatting with lmewis")    
            }
        }

        "send a DM" in {
            click on "dmInput"
            textField("dmInput").value = "hello lmewis"
            submit()

            eventually{
                pageTitle mustBe "lmewis"
                find(cssSelector("h1")).isEmpty mustBe false
                find(cssSelector("h1")).foreach(e => e.text mustBe "Chatting with lmewis")

                findAll(cssSelector("li")).toList.map(_.text) mustBe List("mlewis: hello lmewis")

            }
        }

        "logout" in {
            click on "returnHome"
            pageTitle mustBe "Home"
            find(cssSelector("h1")).isEmpty mustBe false
            find(cssSelector("h1")).foreach(e => e.text mustBe "mlewis's Conversations")

            click on "logoutLink"
            pageTitle mustBe "Login"
        }

        "create new user" in {
            click on "newUserLink"
            pageTitle mustBe "User Creation"
           
            click on "createUsername"
            textField("createUsername").value = "j"
            click on "createPassword"
            pwdField(id("createPassword")).value = "password"
            submit()
            
           eventually {
                pageTitle mustBe "Home"
                find(cssSelector("h1")).isEmpty mustBe false
                find(cssSelector("h1")).foreach(e => e.text mustBe "j's Conversations")
           }
        }

        "let new user send General Message" in {
            click on "generalLink"
            pageTitle mustBe "General Chat"
            find(cssSelector("h1")).isEmpty mustBe false
            find(cssSelector("h1")).foreach(e => e.text mustBe "All Messages")

            click on "generalInput"
            textField("generalInput").value = "how are you"
            submit()

            eventually{
                pageTitle mustBe "General Chat"
                find(cssSelector("h1")).isEmpty mustBe false
                find(cssSelector("h1")).foreach(e => e.text mustBe "All Messages")

                findAll(cssSelector("li")).toList.map(_.text) mustBe List("mlewis: hello world","lmewis: hi", "mlewis: hi world","j: how are you")
                //this is not isolated from the tests in MessagesinMemorySpec; testing everything together has a different result than testOnly MessagerSpec
            }
        }

        "let new user find a user to DM" in {
            click on "returnHomeLink"
            pageTitle mustBe "Home"
            find(cssSelector("h1")).isEmpty mustBe false
            find(cssSelector("h1")).foreach(e => e.text mustBe "j's Conversations")

            click on "userSearchInput"
            textField("userSearchInput").value = "lmewis"
            submit()

            eventually{
                click on "lmewis"
                pageTitle mustBe "lmewis"
                find(cssSelector("h1")).isEmpty mustBe false
                find(cssSelector("h1")).foreach(e => e.text mustBe "Chatting with lmewis")    
            }
        }

        "let new user send a DM" in {
            click on "dmInput"
            textField("dmInput").value = "hi lmewis"
            submit()

            eventually{
                pageTitle mustBe "lmewis"
                find(cssSelector("h1")).isEmpty mustBe false
                find(cssSelector("h1")).foreach(e => e.text mustBe "Chatting with lmewis")

                findAll(cssSelector("li")).toList.map(_.text) mustBe List("j: hi lmewis")

            }
        }

        "let new user logout" in {
            click on "returnHome"
            pageTitle mustBe "Home"
            find(cssSelector("h1")).isEmpty mustBe false
            find(cssSelector("h1")).foreach(e => e.text mustBe "j's Conversations")

            click on "logoutLink"
            pageTitle mustBe "Login"
        }


    }
}