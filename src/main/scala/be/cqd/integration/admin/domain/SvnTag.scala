package be.cqd.integration.admin.domain

// [/project/tags/tagName]

class SvnTag(project: String, tagName: String) extends SvnConfigurationElement {
  def parts: List[String] = List(project, SvnConfigurationElement.TAGS, tagName)
}

object SvnTag {
  def unapply(project: String): Option[(String, String)] = {
    val elements = SvnConfigurationElement.splitOutElements(project)
    if (elements.size >= 3 && elements.tail.head.equals(SvnConfigurationElement.TAGS)) {
      val projectName = elements.head
      val tag = elements.tail.tail.head
      Some(projectName, tag)
    }
    else None
  }
}