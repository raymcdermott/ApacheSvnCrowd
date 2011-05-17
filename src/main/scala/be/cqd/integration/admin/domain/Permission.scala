package be.cqd.integration.admin.domain


// Hmmm... seems a little wordy
abstract class Permission extends Enumeration {}

object Permission extends Enumeration {
  val READ_ONLY = Value("r")
  val READ_WRITE = Value("rw")
  val NONE = Value("") // For groups... bit of a hack so may need refactoring

  def unapply(permission: String): Option[Permission.Value] = {
    if (permission.equals(READ_ONLY.toString))
      Some(Permission.READ_ONLY)
    else if (permission.equals(READ_WRITE.toString))
      Some(Permission.READ_WRITE)
    else
      None
  }
}