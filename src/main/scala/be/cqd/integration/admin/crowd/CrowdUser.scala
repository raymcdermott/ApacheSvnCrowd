package be.cqd.integration.admin.crowd

class CrowdUser(user: String) extends Authority {
  def name = user
  override def toString = name
}

object CrowdUser {
  def unapply(user: String) : Option[String] = {
    val settings = user.split("=").toList.map(_.trim)

    if (settings.length == 2 && ! settings.head.startsWith("@"))
      Some(settings.head)
    else
      None
  }

}

