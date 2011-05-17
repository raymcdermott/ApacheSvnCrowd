package be.cqd.integration.admin.domain

// [projectName/branches/branchName]

class SvnBranch(project: String, branch: String) extends SvnConfigurationElement {
  def parts: List[String] = List(project, SvnConfigurationElement.BRANCHES, branch)
}

object SvnBranch {
  def unapply(input: String): Option[(String, String)] = {
    val elements = SvnConfigurationElement.splitOutElements(input)
    if (elements.size >= 3 && elements.tail.head.equals(SvnConfigurationElement.BRANCHES)) {
      val projectName = elements.head
      val branch = elements.tail.tail.head
      Some(projectName, branch)
    }
    else None
  }
}
