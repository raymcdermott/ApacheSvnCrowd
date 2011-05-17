package be.cqd.integration.admin.domain

class SvnGroups(groups: String) extends SvnConfigurationElement {
  def parts: List[String] = List(SvnGroups.GROUPS)
}

object SvnGroups {

  val GROUPS = "groups"

  def unapply(input: String): Option[String] = {
    val elements = SvnConfigurationElement.splitOutElements(input)
    if (elements.size == 1 && elements.head.equals(GROUPS))
      Some(elements.head)
    else None
  }
}