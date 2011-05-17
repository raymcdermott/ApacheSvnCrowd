package be.cqd.integration.admin.crowd

class CrowdGroup(group: String) extends Authority {
  def name = group

  override def toString = "@" + name
}

object CrowdGroup {
  def unapply(input: String): Option[String] = {

    val settings = input.split("=").toList.map(_.trim)

    // First case from groups section in authzn file:
    // groupName =

    // Second case from any other configuration section
    // @groupName = rw

    if (settings.length == 1 || (settings.length == 2 && settings.head.startsWith("@")))
      Some(settings.head.replaceAll("@", "")) // Groups must not have '@' in their names
    else
      None

  }
}