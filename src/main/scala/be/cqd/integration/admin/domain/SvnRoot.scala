package be.cqd.integration.admin.domain

class SvnRoot(root: String) extends SvnConfigurationElement {
  def parts: List[String] = List.empty[String] // TODO: use "/" ?
}

object SvnRoot {
  def unapply(root: String) : Option[String] = {
    if (root.trim.equals("[/]")) Some(root)
    else None
  }
}