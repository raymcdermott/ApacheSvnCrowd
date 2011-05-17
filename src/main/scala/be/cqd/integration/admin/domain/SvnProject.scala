package be.cqd.integration.admin.domain

class SvnProject(project: String) extends SvnConfigurationElement {

  def this(parts: List[String]) = this(parts.head)
  def this(configurationElement: SvnConfigurationElement) = this(configurationElement.parts.head)

  def parts: List[String] = List(project)
}

object SvnProject {

  def unapply(project: String): Option[String] = {
    val elements = SvnConfigurationElement.splitOutElements(project)
    if (elements.size >= 1)
      Some(elements.head)
    else None
  }
}

