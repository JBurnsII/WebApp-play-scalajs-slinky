package controllers
import models.MessagesInMemoryModel
import javax.inject._
import play.api.mvc._
import java.lang.ProcessBuilder.Redirect
import scala.collection.mutable.HashSet

@Singleton
class Messager @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

    def createUser = Action{implicit request =>
        Ok(views.html.createUser())
    }

    def validateCreation = Action{ request =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map{args =>
            val username = args("username").head
            val password = args("password").head
            if(MessagesInMemoryModel.createUser(username,password)){
                Redirect(routes.Messager.home(username)).withSession("username"->username)
            }
            else {
                Redirect(routes.Messager.login)
            }

        }.getOrElse(Redirect(routes.Messager.login))
    }
    
    def login = Action { implicit request =>
        Ok(views.html.login())
    }
    
    def validateLogin = Action { request =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map{args =>
            val username = args("username").head
            val password = args("password").head
            if(MessagesInMemoryModel.validateUser(username,password)){
                Redirect(routes.Messager.home(username)).withSession("username"->username)
            }
            else {
                Redirect(routes.Messager.login)
            }

        }.getOrElse(Redirect(routes.Messager.login))
    }

    def home(user:String) = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map{ username =>
        val privateConvos = MessagesInMemoryModel.getprivateConvos(username)
        Ok(views.html.home(user, privateConvos, "web"))
        }.getOrElse(Redirect(routes.Messager.login))
    }

    def search = Action{ implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map{ username =>
                val postVals = request.body.asFormUrlEncoded
             postVals.map{args =>
                val privateConvos = MessagesInMemoryModel.getprivateConvos(username)
                val searchTerm = args("searchValue").head
                Ok(views.html.home(username, privateConvos, searchTerm))
            }.getOrElse(Redirect(routes.Messager.home(username)))
        }.getOrElse(Redirect(routes.Messager.login))

    }

    def goBack = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map{ username =>
            val privateConvos = MessagesInMemoryModel.getprivateConvos(username)
        Ok(views.html.home(username, privateConvos, ""))
        }.getOrElse(Redirect(routes.Messager.login))
    }

    def general = Action {implicit request =>
        val posts = MessagesInMemoryModel.getGeneralMessages()
        Ok(views.html.general(posts))
    }

    def postGM = Action {implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map{username =>
            val postVals = request.body.asFormUrlEncoded
            postVals.map{args =>
                val message = args("newPost").head
                Redirect(routes.Messager.general)
                MessagesInMemoryModel.postGM(username, message);
                Redirect(routes.Messager.general)
            }.getOrElse(Redirect(routes.Messager.general))
        }.getOrElse(Redirect(routes.Messager.login))
    }

    def logout = Action {
        Redirect(routes.Messager.login).withNewSession
    }

    def openDM(talkingTo: String) = Action {implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map{username =>
        Ok(views.html.privateConversation(talkingTo,MessagesInMemoryModel.getDMs(HashSet(talkingTo,username))))
        }.getOrElse(Redirect(routes.Messager.login))
    }

    def sendDM(to: String) = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map{username =>
            val postVals = request.body.asFormUrlEncoded
            postVals.map{args =>
                val message = args("newDM").head
                Redirect(routes.Messager.openDM(to))
                MessagesInMemoryModel.sendDM(username,to,message);
                Redirect(routes.Messager.openDM(to))
            }.getOrElse(Redirect(routes.Messager.general))
        }.getOrElse(Redirect(routes.Messager.login))

    }

} 