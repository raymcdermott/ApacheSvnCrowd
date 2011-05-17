package be.cqd.integration.admin.domain

abstract class SvnConfigurationElement {

  def parts: List[String]

  override def toString =
    if (!parts.isEmpty && parts.head.equals("[groups]"))
      parts.tail.mkString("[groups]\n", " =\n", " =\n")
    else if (!parts.isEmpty && parts.head.equals("groups"))
      "[groups]\n"
    else
      parts.mkString("[/", "/", "]\n")
}

object SvnConfigurationElement {
  val TRUNK = "trunk"
  val BRANCHES = "branches"
  val TAGS = "tags"

  def splitOutElements(elementConfiguration: String): List[String] = {
    elementConfiguration.replace("[", "").replace("]", "").split("/").filter(_.length > 0).toList.map(_.trim)
  }
}

