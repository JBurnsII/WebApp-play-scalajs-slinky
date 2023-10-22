package models

import collection.mutable
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashSet

object MessagesInMemoryModel {
    private val users = mutable.Map[String, String]("web"->"apps","mlewis"->"prof")
    private val generalMessages = ListBuffer[(String,String)]()
    private val privateConversations = mutable.Map[String, ListBuffer[String]]("mlewis"->ListBuffer("web"), "web"->ListBuffer("mlewis"))

    private val directMessages = mutable.Map[HashSet[String],ListBuffer[(String,String)]]()

    def validateUser(username:String, password:String): Boolean = {
        users.get(username).map(_ == password).getOrElse(false)
    }

    def createUser(username: String, password: String): Boolean = {
        if (users.contains(username)) false else {
        users(username) = password
        privateConversations(username) = ListBuffer[String]()
        true
        }
    }

    def getGeneralMessages(): ListBuffer[(String,String)] = {
         generalMessages
    }

    def postGM(username: String, message: String) ={
        generalMessages += ((username,message))
    }

    def getprivateConvos(username: String): ListBuffer[String] = {
        privateConversations.get(username).getOrElse(ListBuffer[String]())
    }

    def sendDM(from: String,to: String, message: String) = {
        if(privateConversations(from).contains(to) == false) {
            privateConversations(from) = privateConversations(from) += to
        }
        if(privateConversations(to).contains(from) == false) {
            privateConversations(to) = privateConversations(to) += from
        }
        directMessages.get(HashSet(from, to)).map(_ += ((from,message))).getOrElse(directMessages += (HashSet(from, to) -> ListBuffer[(String,String)]((from,message)))) 
    }

    def getDMs(between: HashSet[String]): ListBuffer[(String,String)] = {
            directMessages.get(between).getOrElse(ListBuffer[(String,String)]())
    }

    def getUsers(): List[String] = {
        users.keySet.toList
    }



}
