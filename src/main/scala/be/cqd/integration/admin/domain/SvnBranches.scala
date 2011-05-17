package be.cqd.integration.admin.domain

// [/project/branches]

class SvnBranches(project: String) extends SvnConfigurationElement {
  def parts: List[String] = List(project, SvnConfigurationElement.BRANCHES)
}

object SvnBranches {
  def unapply(input: String): Option[String] = {
    val elements = SvnConfigurationElement.splitOutElements(input)
    if (elements.size == 2 && elements.tail.head.equals(SvnConfigurationElement.BRANCHES)) {
      Some(elements.head)
    }
    else None
  }
}
