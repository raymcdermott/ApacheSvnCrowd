package be.cqd.integration.admin.domain

// [/project/tags]

class SvnTags(project: String) extends SvnConfigurationElement {
  def parts: List[String] = List(project, SvnConfigurationElement.TAGS)
}

object SvnTags {
  def unapply(project: String): Option[String] = {
    val elements = SvnConfigurationElement.splitOutElements(project)
    if (elements.size == 2 && elements.tail.head.equals(SvnConfigurationElement.TAGS))
      Some(elements.head)
    else
      None
  }
}