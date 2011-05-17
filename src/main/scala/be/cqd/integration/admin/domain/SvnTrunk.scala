package be.cqd.integration.admin.domain

class SvnTrunk(project: String) extends SvnConfigurationElement {

  def this(parts: List[String]) = this (parts.head)

  def this(configurationElement: SvnConfigurationElement) = this (configurationElement.parts.head)

  def parts: List[String] = List(project, SvnConfigurationElement.TRUNK)
}

object SvnTrunk {

  def unapply(project: String): Option[String] = {
    val elements = SvnConfigurationElement.splitOutElements(project)
    if (elements.size >= 2 && elements.tail.head.equals(SvnConfigurationElement.TRUNK))
      Some(elements.head)
    else None
  }
}

