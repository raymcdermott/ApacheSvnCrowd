package be.cqd.integration.admin.domain

import be.cqd.integration.admin.crowd.{CrowdUser, CrowdGroup, Authority}

class AuthorityWithPermissionSetting(auth: Authority, perm: Permission.Value = Permission.READ_ONLY) {
  def authority = auth
  def permission = perm

  override def toString = authority + " = " + permission
}

object AuthorityWithPermissionSetting {

  def unapply(setting: String): Option[(Authority, Permission.Value)] = {
    val settings = setting.split("=").toList.map(_.trim)

    if (settings.length != 2)
      None
    else {
      val principal = setting match { // Must pass the whole line to allow extractor to disambiguate
        case CrowdGroup(group) => new CrowdGroup(group)
        case CrowdUser(user) => new CrowdUser(user)
        case _ => throw new IllegalArgumentException("Invalid user: " + setting)
      }

      val rights = settings.tail.head match {
        case Permission(permission) => permission
        case _ => Permission.NONE
      }

      Some(principal, rights)
    }
  }

}